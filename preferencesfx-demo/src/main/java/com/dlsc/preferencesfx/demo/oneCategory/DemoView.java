package com.dlsc.preferencesfx.demo.oneCategory;

import com.dlsc.preferencesfx.demo.AppStarter;
import com.dlsc.preferencesfx.PreferencesFx;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class DemoView extends VBox {
  private PreferencesFx preferencesFx;
  private MenuBar menuBar;
  private Menu menu;
  private MenuItem preferencesMenuItem;
  private OneCategoryExample rootPane;

  private Label welcomeLbl;
  private Label brightnessLbl;
  private Label nightModeLbl;
  private Label scalingLbl;

  public DemoView(PreferencesFx preferencesFx, OneCategoryExample rootPane) {
    this.preferencesFx = preferencesFx;
    this.rootPane = rootPane;

    initializeParts();
    layoutParts();
    setupBindings();
    setupEventHandlers();
    setupListeners();
  }

  private void initializeParts() {
    menuBar = new MenuBar();
    menu = new Menu("Edit");
    preferencesMenuItem = new MenuItem("Preferences");

    welcomeLbl = new Label();
    brightnessLbl = new Label();
    nightModeLbl = new Label();
    scalingLbl = new Label();
  }

  private void layoutParts() {
    // MenuBar
    menu.getItems().add(preferencesMenuItem);
    menuBar.getMenus().add(menu);

    // VBox with values
    VBox valueBox = new VBox(
        welcomeLbl,
        brightnessLbl,
        nightModeLbl,
        scalingLbl
    );
    valueBox.setSpacing(20);
    valueBox.setPadding(new Insets(20, 0, 0, 20));

    // VBox with descriptions
    VBox descriptionBox = new VBox(
        new Label("Welcome Text:"),
        new Label("Brightness:"),
        new Label("Night mode:"),
        new Label("Scaling:")
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

    // Styling
    getStyleClass().add("preferencesfxx.preferencesfx.demo-view");
    if (rootPane.nightMode.get()) {
      getStylesheets().add(AppStarter.class.getResource("darkTheme.css").toExternalForm());
    }
  }

  private void setupBindings() {
    welcomeLbl.textProperty().bind(rootPane.welcomeText);
    brightnessLbl.textProperty().bind(rootPane.brightness.asString().concat("%"));
    nightModeLbl.textProperty().bind(rootPane.nightMode.asString());
    scalingLbl.textProperty().bind(rootPane.scaling.asString());
  }

  private void setupEventHandlers() {
    preferencesMenuItem.setOnAction(e -> preferencesFx.show());
  }

  private void setupListeners() {
    rootPane.nightMode.addListener((observable, oldValue, newValue) -> {
      if (newValue) {
        getStylesheets().add(AppStarter.class.getResource("darkTheme.css").toExternalForm());
      } else {
        getStylesheets().remove(AppStarter.class.getResource("darkTheme.css").toExternalForm());
      }
    });
  }

}
