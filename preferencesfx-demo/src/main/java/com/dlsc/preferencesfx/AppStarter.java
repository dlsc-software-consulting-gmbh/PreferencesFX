package com.dlsc.preferencesfx;

import com.dlsc.preferencesfx.extended.ExtendedExample;
import com.dlsc.preferencesfx.i18n.InternationalizedExample;
import com.dlsc.preferencesfx.oneCategory.OneCategoryExample;
import com.dlsc.preferencesfx.standard.StandardExample;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class AppStarter extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    TabPane tabPane = new TabPane();
    Pane[] examples = new Pane[]{new StandardExample(), new InternationalizedExample(), new OneCategoryExample(), new ExtendedExample()};
    for(Pane pane : examples)
      tabPane.getTabs().add(new Tab(pane.getClass().getSimpleName().replace("Example", ""), pane));
    Scene myScene = new Scene(tabPane);
    primaryStage.setTitle("PreferencesFx Demo");
    primaryStage.setScene(myScene);
    primaryStage.setWidth(1000);
    primaryStage.setHeight(700);
    primaryStage.show();
    primaryStage.centerOnScreen();
  }

}
