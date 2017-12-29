package com.dlsc.preferencesfx2.view;

import static com.dlsc.preferencesfx2.util.Constants.DEFAULT_PREFERENCES_HEIGHT;
import static com.dlsc.preferencesfx2.util.Constants.DEFAULT_PREFERENCES_WIDTH;

import com.dlsc.preferencesfx2.model.PreferencesModel;
import com.dlsc.preferencesfx2.util.PreferencesFxUtils;
import com.dlsc.preferencesfx2.util.StorageHandler;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.stage.Modality;

public class PreferencesDialog extends DialogPane {
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
    dialog.initModality(Modality.NONE);
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
        model.saveSelectedCategory();
      }
      // Save setting values
      PreferencesFxUtils.categoriesToSettings(
          model.getFlatCategoriesLst()
      ).forEach(setting -> setting.saveSettingValue(storageHandler));
    });
  }

}
