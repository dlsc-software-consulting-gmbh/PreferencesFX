package com.dlsc.preferencesfx.demo.visibility;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class VisibilityDemoAppStarter extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    TabPane tabPane = new TabPane();
    Pane[] panels = new Pane[] {new VisibilityNodeExample()};

    for (Pane pane : panels) {
      tabPane.getTabs().add(new Tab(pane.getClass().getSimpleName().replace("Visibility Example", ""), pane));
    }

    Scene mainScene = new Scene(tabPane);

    primaryStage.setTitle("Visibility PreferencesFX Demo");
    primaryStage.setScene(mainScene);
    primaryStage.setWidth(1000);
    primaryStage.setHeight(700);
    primaryStage.show();
    primaryStage.centerOnScreen();
  }

}
