package com.dlsc.preferencesfx;

import com.dlsc.preferencesfx.util.StorageHandler;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;

public class PreferencesDialog extends DialogPane {

  private PreferencesFx preferencesFx;
  private StorageHandler storageHandler;
  private Dialog dialog = new Dialog();

  public PreferencesDialog(PreferencesFx preferencesFx) {
    this.preferencesFx = preferencesFx;
    storageHandler = preferencesFx.getStorageHandler();

    layoutForm();
    setupClose();
    savePreferencesOnCloseRequest();
    dialog.show();
  }

  private void layoutForm() {
    dialog.setTitle("PreferencesFx");
    dialog.setResizable(true);

    dialog.setDialogPane(this);
    setContent(preferencesFx);
    loadLastState();
  }

  /**
   * Loads last saved size and position of the window.
   */
  private void loadLastState() {
    setPrefSize(storageHandler.getWindowWidth(), storageHandler.getWindowHeight());
    getScene().getWindow().setX(storageHandler.getWindowPosX());
    getScene().getWindow().setY(storageHandler.getWindowPosY());
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
      storageHandler.putWindowWidth(widthProperty().get());
      storageHandler.putWindowHeight(heightProperty().get());
      storageHandler.putWindowPosX(getScene().getWindow().getX());
      storageHandler.putWindowPosY(getScene().getWindow().getY());
      preferencesFx.saveSelectedCategory();
    });
  }
}
