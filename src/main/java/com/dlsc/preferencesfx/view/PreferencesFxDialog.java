package com.dlsc.preferencesfx.view;

import com.dlsc.preferencesfx.history.view.HistoryDialog;
import com.dlsc.preferencesfx.model.PreferencesFxModel;
import com.dlsc.preferencesfx.util.Constants;
import com.dlsc.preferencesfx.util.PreferencesFxUtils;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PreferencesFxDialog extends DialogPane {
  private static final Logger LOGGER =
      LogManager.getLogger(PreferencesFxDialog.class.getName());

  private PreferencesFxModel model;
  private PreferencesFxView preferenceView;

  private Dialog dialog = new Dialog();
  private StorageHandler storageHandler;
  private boolean persistWindowState;
  private ButtonType closeWindowBtnType = ButtonType.CLOSE;
  private ButtonType cancelBtnType = ButtonType.CANCEL;

  public PreferencesFxDialog(PreferencesFxModel model, PreferencesFxView preferenceView) {
    this.model = model;
    this.preferenceView = preferenceView;
    persistWindowState = model.isPersistWindowState();
    storageHandler = model.getStorageHandler();
    layoutForm();
    saveOnCloseRequest();
    loadLastWindowState();
    setupButtons();
    dialog.show();
    if (model.getHistoryDebugState()) {
      setupDebugHistoryTable();
    }
  }

  private void layoutForm() {
    dialog.setTitle("PreferencesFx");
    dialog.setResizable(true);
    getButtonTypes().addAll(closeWindowBtnType, cancelBtnType);
    dialog.initModality(Modality.NONE);
    dialog.setDialogPane(this);
    setContent(preferenceView);
  }

  private void saveOnCloseRequest() {
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
  private void loadLastWindowState() {
    if (persistWindowState) {
      setPrefSize(storageHandler.loadWindowWidth(), storageHandler.loadWindowHeight());
      getScene().getWindow().setX(storageHandler.loadWindowPosX());
      getScene().getWindow().setY(storageHandler.loadWindowPosY());
    } else {
      setPrefSize(Constants.DEFAULT_PREFERENCES_WIDTH, Constants.DEFAULT_PREFERENCES_HEIGHT);
      getScene().getWindow().centerOnScreen();
    }
  }

  private void setupDebugHistoryTable() {
    final KeyCombination keyCombination =
        new KeyCodeCombination(KeyCode.H, KeyCombination.SHORTCUT_DOWN, KeyCombination.SHIFT_DOWN);
    preferenceView.getScene().addEventHandler(KeyEvent.KEY_RELEASED, event -> {
      if (keyCombination.match(event)) {
        LOGGER.trace("Opened History Debug View");
        new HistoryDialog(model.getHistory());
      }
    });
  }

  private void setupButtons() {
    final Button closeBtn = (Button) lookupButton(closeWindowBtnType);
    final Button cancelBtn = (Button) lookupButton(cancelBtnType);

    cancelBtn.setOnAction(event -> model.getHistory().undoAll());

    cancelBtn.visibleProperty().bind(model.buttonsVisibleProperty());
    closeBtn.visibleProperty().bind(model.buttonsVisibleProperty());
  }
}
