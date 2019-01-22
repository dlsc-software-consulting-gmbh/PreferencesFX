package com.dlsc.preferencesfx.view;

import com.dlsc.preferencesfx.PreferencesFx;
import com.dlsc.preferencesfx.history.History;
import com.dlsc.preferencesfx.history.view.HistoryDialog;
import com.dlsc.preferencesfx.model.PreferencesFxModel;
import com.dlsc.preferencesfx.util.Constants;
import com.dlsc.preferencesfx.util.StorageHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
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
  private ButtonType closeWindowBtnType = ButtonType.CLOSE;
  private ButtonType cancelBtnType = ButtonType.CANCEL;

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
    if (modal) {
      dialog.initModality(Modality.APPLICATION_MODAL);
      dialog.showAndWait();
    } else {
      dialog.initModality(Modality.NONE);
      dialog.show();
    }
  }

  private void layoutForm() {
    dialog.setTitle("PreferencesFx");
    dialog.setResizable(true);
    getButtonTypes().addAll(closeWindowBtnType, cancelBtnType);
    dialog.setDialogPane(this);
    setContent(preferencesFxView);
  }

  private void setupDialogClose() {
    dialog.setOnCloseRequest(e -> {
      if (persistWindowState) {
        saveWindowState();
      }
      model.saveSettings();
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

    History history = model.getHistory();
    cancelBtn.setOnAction(event -> {
      LOGGER.trace("Cancel Button was pressed");
      model.discardChanges();
    });
    closeBtn.setOnAction(event -> {
      LOGGER.trace("Close Button was pressed");
      history.clear(false);
    });

    cancelBtn.visibleProperty().bind(model.buttonsVisibleProperty());
    closeBtn.visibleProperty().bind(model.buttonsVisibleProperty());
  }
}
