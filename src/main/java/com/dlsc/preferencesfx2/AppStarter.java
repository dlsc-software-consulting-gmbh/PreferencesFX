package com.dlsc.preferencesfx2;

import com.dlsc.preferencesfx2.view.RootPane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppStarter extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    RootPane rootPane = new RootPane();
    Scene myScene = new Scene(rootPane);

    primaryStage.setTitle("PreferencesFx Demo");
    primaryStage.setScene(myScene);
    primaryStage.setWidth(1000);
    primaryStage.setHeight(600);
    primaryStage.show();
    primaryStage.centerOnScreen();
  }
}
