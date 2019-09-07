package com.dlsc.preferencesfx.view;

import com.dlsc.preferencesfx.PreferencesFx;
import com.dlsc.preferencesfx.history.view.HistoryDialog;
import com.dlsc.preferencesfx.model.PreferencesFxModel;
import com.dlsc.preferencesfx.util.Constants;
import com.dlsc.preferencesfx.util.StorageHandler;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents the dialog which is used to show the PreferencesFX window.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class PreferencesFxDialog extends DialogPane {
  private static final Logger LOGGER =
      LoggerFactory.getLogger(PreferencesFxDialog.class.getName());

  private PreferencesFxModel model;
  private PreferencesFxView preferencesFxView;

  private Dialog dialog = new Dialog();
  private StorageHandler storageHandler;
  private boolean persistWindowState;
  private boolean saveSettings;
  private boolean modalityInitialized;
  private ButtonType closeWindowBtnType = ButtonType.CLOSE;
  private ButtonType cancelBtnType = ButtonType.CANCEL;
  private ButtonType okBtnType = ButtonType.OK;
  private ButtonType applyBtnType = ButtonType.APPLY;
  private Button applyWithEventBtn;

  /**
   * Initializes the {@link DialogPane} which shows the PreferencesFX window.
   *
   * @param model             the model of PreferencesFX
   * @param preferencesFxView the master view to be display in this {@link DialogPane}
   */
  public PreferencesFxDialog(PreferencesFxModel model, PreferencesFxView preferencesFxView) {
    this.model = model;
    this.preferencesFxView = preferencesFxView;
    persistWindowState = model.isPersistWindowState();
    saveSettings = model.isSaveSettings();
    storageHandler = model.getStorageHandler();
    model.loadSettingValues();
    layoutForm();
    setupDialogClose();
    loadLastWindowState();
    setupButtons();
    if (model.getHistoryDebugState()) {
      setupDebugHistoryTable();
    }
  }

  /**
   * Opens {@link PreferencesFx} in a non-modal dialog window.
   * A non-modal dialog window means the user is able to interact with the original application
   * while the dialog is open.
   */
  public void show() {
    show(false);
  }

  /**
   * Opens {@link PreferencesFx} in a dialog window.
   *
   * @param modal if true, will not allow the user to interact with any other window than
   *              the {@link PreferencesFxDialog}, as long as it is open.
   */
  public void show(boolean modal) {
    if (!modalityInitialized) {
      // only set modality once to avoid exception:
      // java.lang.IllegalStateException: Cannot set modality once stage has been set visible
      Modality modality = modal ? Modality.APPLICATION_MODAL : Modality.NONE;
      dialog.initModality(modality);
      modalityInitialized = true;
    }

    if (modal) {
      dialog.showAndWait();
    } else {
      dialog.show();
    }
  }

  private void layoutForm() {
    dialog.setTitle("Preferences");
    dialog.setResizable(true);
    addButtons();
    dialog.setDialogPane(this);
    setContent(preferencesFxView);
  }

  private void addButtons() {
    LOGGER.trace("Add dialog buttons for instant persistence: " + model.isInstantPersistent());
    if (model.isInstantPersistent()) {
      getButtonTypes().addAll(closeWindowBtnType, cancelBtnType);
    } else {
      getButtonTypes().addAll(cancelBtnType, applyBtnType, okBtnType);
    }
  }

  private void setupDialogClose() {
    dialog.setOnCloseRequest(e -> {
      LOGGER.trace("Closing because of dialog close request");
      ButtonType resultButton = (ButtonType) dialog.resultProperty().getValue();
      if (ButtonType.CANCEL.equals(resultButton)) {
        LOGGER.trace("Dialog - Cancel Button was pressed");
        model.discardChanges();
      } else {
        LOGGER.trace("Dialog - Close Button or 'x' was pressed");
        if (persistWindowState) {
          saveWindowState();
        }
        model.saveSettings();
      }
    });
  }

  private void saveWindowState() {
    storageHandler.saveWindowWidth(widthProperty().get());
    storageHandler.saveWindowHeight(heightProperty().get());
    storageHandler.saveWindowPosX(getScene().getWindow().getX());
    storageHandler.saveWindowPosY(getScene().getWindow().getY());
    storageHandler.saveDividerPosition(model.getDividerPosition());
    model.saveSelectedCategory();
  }

  /**
   * Loads last saved size and position of the window.
   */
  private void loadLastWindowState() {
    if (persistWindowState) {
      setPrefSize(storageHandler.loadWindowWidth(), storageHandler.loadWindowHeight());
      getScene().getWindow().setX(storageHandler.loadWindowPosX());
      getScene().getWindow().setY(storageHandler.loadWindowPosY());
      model.setDividerPosition(storageHandler.loadDividerPosition());
      model.setDisplayedCategory(model.loadSelectedCategory());
    } else {
      setPrefSize(Constants.DEFAULT_PREFERENCES_WIDTH, Constants.DEFAULT_PREFERENCES_HEIGHT);
      getScene().getWindow().centerOnScreen();
    }
  }

  private void setupDebugHistoryTable() {
    final KeyCombination keyCombination =
        new KeyCodeCombination(KeyCode.H, KeyCombination.SHORTCUT_DOWN, KeyCombination.SHIFT_DOWN);
    preferencesFxView.getScene().addEventHandler(KeyEvent.KEY_RELEASED, event -> {
      if (keyCombination.match(event)) {
        LOGGER.trace("Opened History Debug View");
        new HistoryDialog(model.getHistory());
      }
    });
  }

  private void setupButtons() {
    LOGGER.trace("Setting Buttons up");
    final Button closeBtn = (Button) lookupButton(closeWindowBtnType);
    final Button cancelBtn = (Button) lookupButton(cancelBtnType);
    final Button applyBtn = (Button) lookupButton(applyBtnType);

    // make sure if visibleProperty was bound in a previous call to unbind it first
    if (closeBtn != null) {
      closeBtn.visibleProperty().unbind();
    }
    if (cancelBtn != null) {
      cancelBtn.visibleProperty().unbind();
    }

    if (model.isInstantPersistent()) {
      cancelBtn.visibleProperty().bind(model.buttonsVisibleProperty());
      closeBtn.visibleProperty().bind(model.buttonsVisibleProperty());
    } else {
      // check if we already added an event filter to the apply button, to avoid adding it twice
      if (applyBtn != applyWithEventBtn) {
        LOGGER.trace("Adding event filter to apply button");
        applyBtn.addEventFilter(
            ActionEvent.ACTION,
            event -> {
              event.consume();
              model.saveSettings();
            });

        applyWithEventBtn = applyBtn;
      } else {
        LOGGER.trace("Event filter was already added previously to apply button");
      }
    }
  }

  /**
   * Sets the dialog title.
   *
   * @param title the dialog title
   */
  public void setDialogTitle(String title) {
    dialog.setTitle(title);
  }

  /**
   * Sets the dialog icon.
   *
   * @param image the image to be used as the dialog icon.
   */
  public void setDialogIcon(Image image) {
    ((Stage) dialog.getDialogPane().getScene().getWindow()).getIcons().add(image);
  }
}
