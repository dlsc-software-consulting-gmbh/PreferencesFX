package com.dlsc.preferencesfx;

import static com.dlsc.preferencesfx.PreferencesFx.DEFAULT_PREFERENCES_HEIGHT;
import static com.dlsc.preferencesfx.PreferencesFx.DEFAULT_PREFERENCES_POS_X;
import static com.dlsc.preferencesfx.PreferencesFx.DEFAULT_PREFERENCES_POS_Y;
import static com.dlsc.preferencesfx.PreferencesFx.DEFAULT_PREFERENCES_WIDTH;
import static com.dlsc.preferencesfx.PreferencesFx.WINDOW_HEIGHT;
import static com.dlsc.preferencesfx.PreferencesFx.WINDOW_POS_X;
import static com.dlsc.preferencesfx.PreferencesFx.WINDOW_POS_Y;
import static com.dlsc.preferencesfx.PreferencesFx.WINDOW_WIDTH;

import java.util.prefs.Preferences;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;

public class PreferencesDialog extends DialogPane {

  private PreferencesFx preferencesFx;
  private Preferences preferences;
  private Dialog dialog = new Dialog();

  public PreferencesDialog(PreferencesFx preferencesFx) {
    this.preferencesFx = preferencesFx;
    preferences = preferencesFx.getPreferences();

    layoutForm();
    setupClose();
    savePreferencesOnCloseRequest();
    dialog.show();
  }

  private void layoutForm() {
    dialog.setTitle("PreferencesFx");
    dialog.setResizable(true);

    dialog.setDialogPane(this);
    this.setContent(preferencesFx);
    this.setPrefSize(
        preferences.getDouble(WINDOW_WIDTH, DEFAULT_PREFERENCES_WIDTH),
        preferences.getDouble(WINDOW_HEIGHT, DEFAULT_PREFERENCES_HEIGHT)
    );
    getScene().getWindow().setX(preferences.getDouble(WINDOW_POS_X, DEFAULT_PREFERENCES_POS_X));
    getScene().getWindow().setY(preferences.getDouble(WINDOW_POS_Y, DEFAULT_PREFERENCES_POS_Y));
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
      preferences.putDouble(WINDOW_WIDTH, this.widthProperty().get());
      preferences.putDouble(WINDOW_HEIGHT, this.heightProperty().get());
      preferences.putDouble(WINDOW_POS_X, getScene().getWindow().getX());
      preferences.putDouble(WINDOW_POS_Y, getScene().getWindow().getY());
      preferencesFx.save();
    });
  }
}
