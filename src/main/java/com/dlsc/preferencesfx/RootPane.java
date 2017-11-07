package com.dlsc.preferencesfx;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by François Martin on 30.10.17.
 */
public class RootPane extends VBox {
  private static final Logger LOGGER =
      LogManager.getLogger(RootPane.class.getName());

  private PreferencesFX preferencesFX;
  BooleanProperty nachtmodus = new SimpleBooleanProperty(true);
  StringProperty systemName = new SimpleStringProperty("PreferencesFX");
  IntegerProperty helligkeit = new SimpleIntegerProperty(50);

  MenuBar menuBar;
  Menu menu;
  MenuItem preferencesMenuItem;

  RootPane() {
    setupMenuBar();
    setupPreferences();
    setupLabels();
  }

  private void setupMenuBar() {
    menuBar = new MenuBar();
    menu = new Menu("Edit");
    preferencesMenuItem = new MenuItem("Preferences");
    menu.getItems().add(preferencesMenuItem);
    menuBar.getMenus().add(menu);
    getChildren().add(menuBar);
    preferencesMenuItem.setOnAction(e -> new PreferenceDialog(preferencesFX));
  }

  private void setupPreferences() {
    preferencesFX = PreferencesFX.of(
        Category.of("Bildschirm",
            Group.of(
              Setting.of("Nachtmodus", Type.BOOLEAN, nachtmodus),
              Setting.of("Systemname", Type.STRING, systemName),
              Setting.of("Helligkeit", Type.INTEGER, helligkeit)
            ).description("Bildschirmspezifische Einstellungen"),
            Group.of(
                Setting.of("Nachtmodus2", Type.BOOLEAN, nachtmodus),
                Setting.of("Systemname2", Type.STRING, systemName),
                Setting.of("Helligkeit2", Type.INTEGER, helligkeit)
            ).description("Bildschirmspezifische Einstellungen2")
        )
    );
    LOGGER.info("Preferences generated");
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
