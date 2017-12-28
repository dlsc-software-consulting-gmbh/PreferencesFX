package com.dlsc.preferencesfx;

import static com.dlsc.preferencesfx.PreferencesFx.DEFAULT_PREFERENCES_HEIGHT;
import static com.dlsc.preferencesfx.PreferencesFx.DEFAULT_PREFERENCES_WIDTH;

import com.dlsc.preferencesfx.util.PreferencesFxUtils;
import com.dlsc.preferencesfx.util.StorageHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;

public class PreferencesDialog extends DialogPane {

  private PreferencesFx preferencesFx;
  private StorageHandler storageHandler;
  private Dialog dialog = new Dialog();
  private boolean persistWindowState;
  private ButtonType closeWindowBtnType = ButtonType.CLOSE;
  private ButtonType cancelBtnType = ButtonType.CANCEL;


  public PreferencesDialog(PreferencesFx preferencesFx, boolean persistWindowState) {
    this.preferencesFx = preferencesFx;
    storageHandler = preferencesFx.getStorageHandler();
    this.persistWindowState = persistWindowState;

    layoutForm();
    savePreferencesOnCloseRequest();
    loadLastState();
    setupEventHandler();
    dialog.show();
  }

  public PreferencesDialog(PreferencesFx preferencesFx) {
    this(preferencesFx, false);
  }

  private void layoutForm() {
    dialog.setTitle("PreferencesFx");
    dialog.setResizable(true);
    getButtonTypes().addAll(closeWindowBtnType, cancelBtnType);
    dialog.setDialogPane(this);
    setContent(preferencesFx);
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
      setPrefSize(DEFAULT_PREFERENCES_WIDTH, DEFAULT_PREFERENCES_HEIGHT);
      getScene().getWindow().centerOnScreen();
    }

  }

  private void savePreferencesOnCloseRequest() {
    dialog.setOnCloseRequest(e -> {
      if (persistWindowState) {
        // Save window state
        storageHandler.saveWindowWidth(widthProperty().get());
        storageHandler.saveWindowHeight(heightProperty().get());
        storageHandler.saveWindowPosX(getScene().getWindow().getX());
        storageHandler.saveWindowPosY(getScene().getWindow().getY());
        preferencesFx.saveSelectedCategory();
      }
      // Save setting values
      PreferencesFxUtils.categoriesToSettings(
          preferencesFx.getCategoryTree().getAllCategoriesFlatAsList()
      ).forEach(setting -> setting.saveSettingValue(storageHandler));
    });

  }

  private void setupEventHandler() {
    final Button cancelBtn = (Button) lookupButton(cancelBtnType);
    cancelBtn.setOnAction(event -> preferencesFx.getHistory().undoAll());
  }

}
