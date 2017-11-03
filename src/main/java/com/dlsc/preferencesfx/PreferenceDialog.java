package com.dlsc.preferencesfx;

import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;

public class PreferenceDialog extends DialogPane {

  PreferencesFX preferencesFX;
  Dialog dialog = new Dialog();

  PreferenceDialog(PreferencesFX preferencesFX) {
    this.preferencesFX = preferencesFX;

    initFieldData();
    layoutForm();
    attachEvents();

    dialog.show();
//
//    Optional<ButtonType> result = dialog.showAndWait();
//    if (result.isPresent()) {
//      if (result.get() == ButtonType.APPLY) {
//        save();
//      }
//    }
  }

  private void initFieldData() {

  }

  private void layoutForm() {
    dialog.setResizable(true);
    dialog.setHeight(500);
    dialog.setWidth(500);
    dialog.setTitle("PreferencesFX");
    dialog.setResizable(true);

    this.getStyleClass().add("expertDialog");

    this.setContent(preferencesFX);
    dialog.setDialogPane(this);
  }

  private void attachEvents() {
//    final Button loadDefaultsBtn = (Button) lookupButton(loadDefaultsBtnType);
//    loadDefaultsBtn.addEventFilter(
//        ActionEvent.ACTION,
//        event -> {
////           stops the dialog window from closing
//          event.consume();
//          defaultUserPreferences();
//        }
//    );
  }

  private void defaultUserPreferences() {

  }

  private void save() {

  }

}
