package com.dlsc.preferencesfx.demo.visibility;

import com.dlsc.preferencesfx.PreferencesFx;
import com.dlsc.preferencesfx.model.Category;
import com.dlsc.preferencesfx.model.Group;
import com.dlsc.preferencesfx.model.Setting;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.layout.StackPane;

public class VisibilityNodeExample extends StackPane {

  public PreferencesFx preferencesFx;

  IntegerProperty brightness = new SimpleIntegerProperty(50);

  BooleanProperty nightMode = new SimpleBooleanProperty(true);

  public VisibilityNodeExample() {
    preferencesFx = createPreferences();
    getChildren().add(new VisibilityNodeView(preferencesFx, this));
  }

  private PreferencesFx createPreferences() {
    SimpleBooleanProperty visibilityProperty = new SimpleBooleanProperty(true);
    brightness.addListener((observable, oldValue, newValue) -> { visibilityProperty.set(newValue.intValue() > 50); });

    return PreferencesFx.of(VisibilityNodeExample.class,
        Category.of("General",
            Group.of("Display",
                Setting.of("Brightness", brightness),
                Setting.of("Night mode", nightMode, visibilityProperty)
            )
        )
    ).persistWindowState(false).saveSettings(true).debugHistoryMode(false).buttonsVisibility(true);
  }
}
