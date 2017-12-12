package com.dlsc.preferencesfx2.view;

import static com.dlsc.preferencesfx.PreferencesFx.DEFAULT_PREFERENCES_HEIGHT;
import static com.dlsc.preferencesfx.PreferencesFx.DEFAULT_PREFERENCES_WIDTH;

import com.dlsc.preferencesfx.util.PreferencesFxUtils;
import com.dlsc.preferencesfx.util.StorageHandler;
import com.dlsc.preferencesfx2.model.PreferencesModel;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;

public class PreferenceDialog extends DialogPane {
  private PreferencesModel preferencesModel;
  private PreferenceView preferenceView;

  private Dialog dialog = new Dialog();
  private StorageHandler storageHandler;
  private boolean persistWindowState;

  public PreferenceDialog(PreferencesModel preferencesModel, PreferenceView preferenceView) {
    this.preferencesModel = preferencesModel;
    this.preferenceView = preferenceView;
    persistWindowState = preferencesModel.isPersistWindowState();
    storageHandler = preferencesModel.getStorageHandler();
    layoutForm();
    savePreferencesOnCloseRequest();
    loadLastState();
    setupClose();
    dialog.show();
  }

  private void layoutForm() {
    dialog.setTitle("PreferencesFx");
    dialog.setResizable(true);

    dialog.setDialogPane(this);
    setContent(preferenceView);
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

  private void savePreferencesOnCloseRequest() {
    dialog.setOnCloseRequest(e -> {
      if (persistWindowState) {
        // Save window state
        storageHandler.saveWindowWidth(widthProperty().get());
        storageHandler.saveWindowHeight(heightProperty().get());
        storageHandler.saveWindowPosX(getScene().getWindow().getX());
        storageHandler.saveWindowPosY(getScene().getWindow().getY());
        preferencesModel.saveSelectedCategory(preferenceView.getCategoryTree());
      }
      // Save setting values
      PreferencesFxUtils.categoriesToSettings(
          preferenceView.getCategoryTree().getAllCategoriesFlatAsList()
      ).forEach(setting -> setting.saveSettingValue(storageHandler));
    });
  }

}
