package com.dlsc.preferencesfx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelloWorld extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    RootPane rootPane = new RootPane();

    Scene myScene = new Scene(rootPane);

    primaryStage.setTitle("JavaFX App");
    primaryStage.setScene(myScene);
    primaryStage.setWidth(600);
    primaryStage.setHeight(600);
    primaryStage.show();
    primaryStage.centerOnScreen();
  }
}
