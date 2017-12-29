package com.dlsc.preferencesfx.view;

import com.dlsc.preferencesfx.history.view.HistoryDialog;
import com.dlsc.preferencesfx.model.PreferencesModel;
import com.dlsc.preferencesfx.util.Constants;
import com.dlsc.preferencesfx.util.PreferencesFxUtils;
import com.dlsc.preferencesfx.util.StorageHandler;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PreferencesDialog extends DialogPane {
  private static final Logger LOGGER =
      LogManager.getLogger(PreferencesDialog.class.getName());

  private PreferencesModel model;
  private PreferencesView preferenceView;

  private Dialog dialog = new Dialog();
  private StorageHandler storageHandler;
  private boolean persistWindowState;

  public PreferencesDialog(PreferencesModel model, PreferencesView preferenceView) {
    this.model = model;
    this.preferenceView = preferenceView;
    persistWindowState = model.isPersistWindowState();
    storageHandler = model.getStorageHandler();
    layoutForm();
    savePreferencesOnCloseRequest();
    loadLastState();
    setupClose();
    dialog.show();
    if (model.getHistoryDebugState()) {
      setupDebugHistoryTable();
    }
  }

  private void layoutForm() {
    dialog.setTitle("PreferencesFx");
    dialog.setResizable(true);
    dialog.initModality(Modality.NONE);
    dialog.setDialogPane(this);
    setContent(preferenceView);
  }

  private void savePreferencesOnCloseRequest() {
    dialog.setOnCloseRequest(e -> {
      if (persistWindowState) {
        // Save window state
        storageHandler.saveWindowWidth(widthProperty().get());
        storageHandler.saveWindowHeight(heightProperty().get());
        storageHandler.saveWindowPosX(getScene().getWindow().getX());
        storageHandler.saveWindowPosY(getScene().getWindow().getY());
        model.saveSelectedCategory();
      }
      // Save setting values
      PreferencesFxUtils.categoriesToSettings(
          model.getFlatCategoriesLst()
      ).forEach(setting -> setting.saveSettingValue(storageHandler));
    });
  }

  /**
   * Loads last saved size and position of the window.
   */
  private void loadLastState() {
    if (persistWindowState) {
      setPrefSize(storageHandler.loadWindowWidth(), storageHandler.loadWindowHeight());
      getScene().getWindow().setX(storageHandler.loadWindowPosX());
      getScene().getWindow().setY(storageHandler.loadWindowPosY());
    } else {
      setPrefSize(Constants.DEFAULT_PREFERENCES_WIDTH, Constants.DEFAULT_PREFERENCES_HEIGHT);
      getScene().getWindow().centerOnScreen();
    }
  }

  /**
   * Instantiates a close button and makes it invisible.
   * Dialog requires at least one button to close the dialog window.
   *
   * @see <a href="https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/Dialog.html">
   * javafx.scene.control.Dialog</a>
   * Chapter: Dialog Closing Rules
   */
  private void setupClose() {
    this.getButtonTypes().add(ButtonType.CLOSE);
    Node closeButton = dialog.getDialogPane().lookupButton(ButtonType.CLOSE);
    closeButton.managedProperty().bind(closeButton.visibleProperty());
    closeButton.setVisible(false);
  }


  private void setupDebugHistoryTable() {
    final KeyCombination keyCombination =
        new KeyCodeCombination(KeyCode.H, KeyCombination.SHORTCUT_DOWN, KeyCombination.SHIFT_DOWN);
    preferenceView.getScene().addEventHandler(KeyEvent.KEY_RELEASED, event -> {
          if (keyCombination.match(event)) {
            LOGGER.trace("Opened History Debug View");
            new HistoryDialog(model.getHistory());
          }
        }
    );
  }
}
