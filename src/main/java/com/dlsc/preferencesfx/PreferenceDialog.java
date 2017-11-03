package com.dlsc.preferencesfx;

import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;

public class PreferenceDialog extends DialogPane {

  PreferencesFX preferencesFX;
  Dialog dialog = new Dialog();

  PreferenceDialog(PreferencesFX preferencesFX) {
    this.preferencesFX = preferencesFX;

    layoutForm();
    setupClose();

    dialog.show();
  }

  private void layoutForm() {
    dialog.setTitle("PreferencesFX");
    dialog.setResizable(true);

    dialog.setDialogPane(this);
    this.setContent(preferencesFX);
    this.setPrefSize(1000, 700);
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

}
