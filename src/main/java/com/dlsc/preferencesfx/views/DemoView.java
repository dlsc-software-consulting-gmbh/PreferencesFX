package com.dlsc.preferencesfx.views;

import com.dlsc.preferencesfx.PreferencesDialog;
import com.dlsc.preferencesfx.PreferencesFX;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class DemoView extends VBox {
  private PreferencesFX preferencesFX;
  private MenuBar menuBar;
  private Menu menu;
  private MenuItem preferencesMenuItem;
  private RootPane rootPane;

  private Label brightnessLbl;
  private Label nightModeLbl;
  private Label screenNameLbl;
  private Label resolutionLbl;
  private Label orientationLbl;


  public DemoView(PreferencesFX preferencesFX, RootPane rootPane) {
    this.preferencesFX = preferencesFX;
    this.rootPane = rootPane;

    initializeParts();
    layoutParts();
    setupBindings();
    setupEventHandlers();
  }

  private void initializeParts() {
    menuBar = new MenuBar();
    menu = new Menu("Edit");
    preferencesMenuItem = new MenuItem("Preferences");

    brightnessLbl = new Label();
    nightModeLbl = new Label();
    screenNameLbl = new Label();
    resolutionLbl = new Label();
    orientationLbl = new Label();
  }

  private void layoutParts() {
    // MenuBar
    menu.getItems().add(preferencesMenuItem);
    menuBar.getMenus().add(menu);

    // VBox with values
    VBox valueBox = new VBox(
        brightnessLbl,
        nightModeLbl,
        screenNameLbl,
        resolutionLbl,
        orientationLbl
    );
    valueBox.setSpacing(20);
    valueBox.setPadding(new Insets(20, 0, 0, 20));

    // VBox with descriptions
    VBox descriptionBox = new VBox(
        new Label("Brightness:"),
        new Label("Night mode:"),
        new Label("Screen name:"),
        new Label("Resolution:"),
        new Label("Orientation:")
    );
    descriptionBox.setSpacing(20);
    descriptionBox.setPadding(new Insets(20, 0, 0, 20));

    // Put everything together
    getChildren().addAll(
        menuBar,
        new HBox(
            descriptionBox,
            valueBox
        )
    );
  }

  private void setupBindings() {
    brightnessLbl.textProperty().bind(rootPane.brightness.asString().concat("%"));
    nightModeLbl.textProperty().bind(rootPane.nightMode.asString());
    screenNameLbl.textProperty().bind(rootPane.screenName);
    resolutionLbl.textProperty().bind(rootPane.resolutionSelection);
    orientationLbl.textProperty().bind(rootPane.orientationSelection);
  }

  private void setupEventHandlers() {
    preferencesMenuItem.setOnAction(e -> new PreferencesDialog(preferencesFX));

  }
}
