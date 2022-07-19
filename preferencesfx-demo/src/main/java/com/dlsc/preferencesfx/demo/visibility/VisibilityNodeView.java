package com.dlsc.preferencesfx.demo.visibility;

import com.dlsc.preferencesfx.PreferencesFx;
import com.dlsc.preferencesfx.demo.AppStarter;
import com.dlsc.preferencesfx.view.PreferencesFxView;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class VisibilityNodeView extends VBox {
  private PreferencesFx preferencesFx;
  private VisibilityNodeExample rootPane;

  private Label brightnessLabel;
  private Label nightModeLabel;

  public VisibilityNodeView(PreferencesFx preferencesFx, VisibilityNodeExample rootPane) {
    this.preferencesFx = preferencesFx;
    this.rootPane = rootPane;

    initializeParts();
    layoutParts();
    setupBindings();
    setupListeners();
  }

  private void initializeParts() {
    brightnessLabel = new Label();
    nightModeLabel = new Label();
  }

  private void layoutParts() {
    // VBox with values
    VBox valueBox = new VBox(
            brightnessLabel,
            nightModeLabel
    );

    valueBox.setSpacing(20);
    valueBox.setPadding(new Insets(20, 0, 0, 20));
    Button saveSettingsButton = new Button("Save Settings");
    saveSettingsButton.setOnAction(event -> preferencesFx.saveSettings());
    Button discardChangesButton = new Button("Discard Changes");
    discardChangesButton.setOnAction(event -> preferencesFx.discardChanges());
    // VBox with descriptions
    VBox descriptionBox = new VBox(
        new Label("Brightness:"),
        new Label("Night mode:"),
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
    getStyleClass().add("demo-view");
    if (rootPane.nightMode.get()) {
      getStylesheets().add(AppStarter.class.getResource("darkTheme.css").toExternalForm());
    }
  }

  private void setupBindings() {
    brightnessLabel.textProperty().bind(rootPane.brightness.asString().concat("%"));
    nightModeLabel.textProperty().bind(rootPane.nightMode.asString());
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
