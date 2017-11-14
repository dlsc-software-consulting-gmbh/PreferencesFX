package com.dlsc.preferencesfx;

import com.google.common.collect.Lists;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

  // Group: Brightness & Color
  IntegerProperty brightness = new SimpleIntegerProperty(50);
  BooleanProperty nightMode = new SimpleBooleanProperty(true);
  // Group: Scaling & Ordering
  StringProperty screenName = new SimpleStringProperty("PreferencesFX Monitor");
  ObservableList<String> resolutionItems = FXCollections.observableArrayList(Lists.newArrayList("1024x768", "1280x1024", "1440x900", "1920x1080"));
  ObjectProperty<String> resolutionSelection = new SimpleObjectProperty<>("1024x768");
  ListProperty<String> orientationItems = new SimpleListProperty<>(FXCollections.observableArrayList(Lists.newArrayList("Vertical", "Horizontal")));
  ObjectProperty<String> orientationSelection = new SimpleObjectProperty<>("Vertical");

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
        Category.of("Screen",
            Group.of(
                Setting.of("Change Brightness", brightness),
                Setting.of("Night mode", nightMode)
            ).description("Brightness & Color")
        ),
        Category.of("Scaling & Ordering")
            .subCategories(
                Category.of("Screen",
                    Group.of(
                        Setting.of("Screen name", screenName),
                        Setting.of("Resolution", resolutionItems, resolutionSelection),
                        Setting.of("Orientation", orientationItems, orientationSelection)
                    ).description("Brightness & Color")
                )
            )
    );
    LOGGER.info("Preferences generated");
  }

  private void setupLabels() {
    Label brightnessLbl = new Label();
    brightnessLbl.textProperty().bind(brightness.asString().concat("%"));

    Label nightModeLbl = new Label();
    nightModeLbl.textProperty().bind(nightMode.asString());

    Label screenNameLbl = new Label();
    screenNameLbl.textProperty().bind(screenName);

    Label resolutionLbl = new Label();
    resolutionLbl.textProperty().bind(resolutionSelection);

    Label orientationLbl = new Label();
    orientationLbl.textProperty().bind(orientationSelection);

    HBox hBox = new HBox(brightnessLbl, nightModeLbl, screenNameLbl, resolutionLbl, orientationLbl);
    hBox.setSpacing(20);
    hBox.setPadding(new Insets(0, 0, 0, 20));
    getChildren().add(hBox);
  }
}
