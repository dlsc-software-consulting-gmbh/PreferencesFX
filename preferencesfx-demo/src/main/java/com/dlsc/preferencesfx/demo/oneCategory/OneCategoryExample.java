package com.dlsc.preferencesfx.demo.oneCategory;

import com.dlsc.formsfx.model.validators.DoubleRangeValidator;
import com.dlsc.preferencesfx.PreferencesFx;
import com.dlsc.preferencesfx.model.Category;
import com.dlsc.preferencesfx.model.Group;
import com.dlsc.preferencesfx.model.Setting;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.layout.StackPane;

public class OneCategoryExample extends StackPane {

  public PreferencesFx preferencesFx;

  // General
  StringProperty welcomeText = new SimpleStringProperty("Hello World");
  IntegerProperty brightness = new SimpleIntegerProperty(50);
  BooleanProperty nightMode = new SimpleBooleanProperty(true);
  DoubleProperty scaling = new SimpleDoubleProperty(1);

  public OneCategoryExample() {
    preferencesFx = createPreferences();
    getChildren().add(new DemoView(preferencesFx, this));
  }

  private PreferencesFx createPreferences() {
    return PreferencesFx.of(OneCategoryExample.class,
        Category.of("General",
            Group.of("Greeting",
                Setting.of("Welcome Text", welcomeText)
            ),
            Group.of("Display",
                Setting.of("Brightness", brightness),
                Setting.of("Night mode", nightMode),
                Setting.of("Scaling", scaling)
                    .validate(DoubleRangeValidator.atLeast(1, "Scaling needs to be at least 1"))
            )
        )
    ).persistWindowState(false)
     .saveSettings(true)
     .debugHistoryMode(false)
     .buttonsVisibility(true)
     .instantPersistent(false);
  }
}
