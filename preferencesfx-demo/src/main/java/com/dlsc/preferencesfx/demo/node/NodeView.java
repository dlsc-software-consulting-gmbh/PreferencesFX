package com.dlsc.preferencesfx.demo.node;

import com.dlsc.preferencesfx.demo.AppStarter;
import com.dlsc.preferencesfx.PreferencesFx;
import com.dlsc.preferencesfx.view.PreferencesFxView;
import java.util.stream.Collectors;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class NodeView extends VBox {
  private PreferencesFx preferencesFx;
  private NodeExample rootPane;

  private Label welcomeLbl;
  private Label brightnessLbl;
  private Label nightModeLbl;
  private Label scalingLbl;
  private Label screenNameLbl;
  private Label resolutionLbl;
  private Label orientationLbl;
  private Label favoritesLbl;
  private Label fontSizeLbl;
  private Label lineSpacingLbl;
  private Label favoriteNumberLbl;

  public NodeView(PreferencesFx preferencesFx, NodeExample rootPane) {
    this.preferencesFx = preferencesFx;
    this.rootPane = rootPane;

    initializeParts();
    layoutParts();
    setupBindings();
    setupListeners();
  }

  private void initializeParts() {
    welcomeLbl = new Label();
    brightnessLbl = new Label();
    nightModeLbl = new Label();
    scalingLbl = new Label();
    screenNameLbl = new Label();
    resolutionLbl = new Label();
    orientationLbl = new Label();
    favoritesLbl = new Label();
    fontSizeLbl = new Label();
    lineSpacingLbl = new Label();
    favoriteNumberLbl = new Label();
  }

  private void layoutParts() {
    // VBox with values
    VBox valueBox = new VBox(
        welcomeLbl,
        brightnessLbl,
        nightModeLbl,
        scalingLbl,
        screenNameLbl,
        resolutionLbl,
        orientationLbl,
        favoritesLbl,
        fontSizeLbl,
        lineSpacingLbl,
        favoriteNumberLbl
    );
    valueBox.setSpacing(20);
    valueBox.setPadding(new Insets(20, 0, 0, 20));
    Button saveSettingsButton = new Button("Save Settings");
    saveSettingsButton.setOnAction(event -> preferencesFx.saveSettings());
    Button discardChangesButton = new Button("Discard Changes");
    discardChangesButton.setOnAction(event -> preferencesFx.discardChanges());
    // VBox with descriptions
    VBox descriptionBox = new VBox(
        new Label("Welcome Text:"),
        new Label("Brightness:"),
        new Label("Night mode:"),
        new Label("Scaling:"),
        new Label("Screen name:"),
        new Label("Resolution:"),
        new Label("Orientation:"),
        new Label("Favorites:"),
        new Label("Font Size:"),
        new Label("Line Spacing:"),
        new Label("Favorite Number:"),
        saveSettingsButton,
        discardChangesButton
    );
    descriptionBox.setSpacing(20);
    descriptionBox.setPadding(new Insets(20, 0, 0, 20));

    PreferencesFxView preferencesFxView = preferencesFx.getView();
    // Put everything together
    BorderPane pane = new BorderPane();
    HBox hBox = new HBox(descriptionBox, valueBox);
    pane.setLeft(hBox);
    hBox.setPadding(new Insets(0, 20, 0, 0));
    pane.setCenter(preferencesFxView);
    VBox.setVgrow(pane, Priority.ALWAYS);
    getChildren().addAll(
        pane
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
    screenNameLbl.textProperty().bind(rootPane.screenName);
    resolutionLbl.textProperty().bind(rootPane.resolutionSelection);
    orientationLbl.textProperty().bind(rootPane.orientationSelection);
    favoritesLbl.textProperty().bind(Bindings.createStringBinding(
        () -> rootPane.favoritesSelection.stream().collect(Collectors.joining(", ")),
        rootPane.favoritesSelection
    ));
    fontSizeLbl.textProperty().bind(rootPane.fontSize.asString());
    lineSpacingLbl.textProperty().bind(rootPane.lineSpacing.asString());
    favoriteNumberLbl.textProperty().bind(rootPane.customControlProperty.asString());
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
