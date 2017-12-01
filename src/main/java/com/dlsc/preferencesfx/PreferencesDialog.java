package com.dlsc.preferencesfx;

import static com.dlsc.preferencesfx.PreferencesFx.DEFAULT_PREFERENCES_HEIGHT;
import static com.dlsc.preferencesfx.PreferencesFx.DEFAULT_PREFERENCES_WIDTH;

import com.dlsc.preferencesfx.util.StorageHandler;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;

public class PreferencesDialog extends DialogPane {

  private PreferencesFx preferencesFx;
  private StorageHandler storageHandler;
  private Dialog dialog = new Dialog();
  private boolean persistWindowState;

  public PreferencesDialog(PreferencesFx preferencesFx, boolean persistWindowState) {
    this.preferencesFx = preferencesFx;
    storageHandler = preferencesFx.getStorageHandler();
    this.persistWindowState = persistWindowState;

    layoutForm();
    savePreferencesOnCloseRequest();
    loadLastState();
    setupClose();
    dialog.show();
  }

  public PreferencesDialog(PreferencesFx preferencesFx) {
    this(preferencesFx, false);
  }

  private void layoutForm() {
    dialog.setTitle("PreferencesFx");
    dialog.setResizable(true);

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
        storageHandler.saveWindowWidth(widthProperty().get());
        storageHandler.saveWindowHeight(heightProperty().get());
        storageHandler.saveWindowPosX(getScene().getWindow().getX());
        storageHandler.saveWindowPosY(getScene().getWindow().getY());
        preferencesFx.saveSelectedCategory();
      }
      preferencesFx.getCategoryTree()
          .getAllCategoriesFlatAsList()
          .stream()
          .map(Category::getGroups)     // get groups from categories
          .filter(Objects::nonNull)     // remove all null
          .flatMap(Collection::stream)
          .map(Group::getSettings)      // get settings from groups
          .filter(Objects::nonNull)     // remove all null
          .flatMap(Collection::stream)
          .collect(Collectors.toList())
          .forEach(setting -> setting.saveSettingValue(storageHandler));
    });
  }

}
