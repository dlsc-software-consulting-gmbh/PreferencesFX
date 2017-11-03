package com.dlsc.preferencesfx;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Created by François Martin on 30.10.17.
 */
public class RootPane extends VBox {

  private PreferencesFX preferencesFX;
  BooleanProperty nachtmodus = new SimpleBooleanProperty(true);
  StringProperty systemName = new SimpleStringProperty("PreferencesFX");
  IntegerProperty helligkeit = new SimpleIntegerProperty(50);

  RootPane() {
    preferencesFX = PreferencesFX.of(
        Category.of("Bildschirm",
            Setting.of("Nachtmodus", Type.BOOLEAN, nachtmodus),
            Setting.of("Systemname", Type.STRING, systemName),
            Setting.of("Helligkeit", Type.INTEGER, helligkeit)
        )
    );

    getChildren().add(preferencesFX);

    setupLabels();
  }

  private void setupLabels() {
    Label label = new Label();
    label.textProperty().bind(nachtmodus.asString());

    Label label2 = new Label();
    label2.textProperty().bind(systemName);

    Label label3 = new Label();
    label3.textProperty().bind(helligkeit.asString().concat("%"));

    HBox hBox = new HBox(label, label2, label3);
    hBox.setSpacing(20);
    hBox.setPadding(new Insets(0, 0, 0, 20));
    getChildren().add(hBox);
  }
}
