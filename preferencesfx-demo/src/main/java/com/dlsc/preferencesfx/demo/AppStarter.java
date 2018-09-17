package com.dlsc.preferencesfx.demo;

import com.dlsc.preferencesfx.demo.extended.ExtendedExample;
import com.dlsc.preferencesfx.demo.i18n.InternationalizedExample;
import com.dlsc.preferencesfx.demo.node.NodeExample;
import com.dlsc.preferencesfx.demo.oneCategory.OneCategoryExample;
import com.dlsc.preferencesfx.demo.standard.StandardExample;
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
    Pane[] examples = new Pane[]{new StandardExample(), new InternationalizedExample(), new OneCategoryExample(), new ExtendedExample(),new NodeExample()};
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
