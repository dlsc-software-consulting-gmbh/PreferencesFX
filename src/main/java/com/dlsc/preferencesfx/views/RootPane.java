package com.dlsc.preferencesfx.views;

import com.dlsc.preferencesfx.Category;
import com.dlsc.preferencesfx.Group;
import com.dlsc.preferencesfx.PreferencesFx;
import com.dlsc.preferencesfx.Setting;
import com.google.common.collect.Lists;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.StackPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by François Martin on 30.10.17.
 */
public class RootPane extends StackPane {
  private static final Logger LOGGER =
      LogManager.getLogger(RootPane.class.getName());

  // General
  IntegerProperty brightness = new SimpleIntegerProperty(50);
  BooleanProperty nightMode = new SimpleBooleanProperty(true);

  // Screen
  DoubleProperty scaling = new SimpleDoubleProperty(1);
  StringProperty screenName = new SimpleStringProperty("PreferencesFx Monitor");

  ObservableList<String> resolutionItems = FXCollections.observableArrayList(Lists.newArrayList(
      "1024x768", "1280x1024", "1440x900", "1920x1080")
  );
  ObjectProperty<String> resolutionSelection = new SimpleObjectProperty<>("1024x768");

  ListProperty<String> orientationItems = new SimpleListProperty<>(
      FXCollections.observableArrayList(Lists.newArrayList("Vertical", "Horizontal"))
  );
  ObjectProperty<String> orientationSelection = new SimpleObjectProperty<>("Vertical");

  IntegerProperty fontSize = new SimpleIntegerProperty(12);
  DoubleProperty lineSpacing = new SimpleDoubleProperty(1.5);

  // Favorites
  ListProperty<String> favoritesItems = new SimpleListProperty<>(
      FXCollections.observableArrayList(Lists.newArrayList(
          "eMovie", "Eboda Phot-O-Shop", "Mikesoft Text",
          "Mikesoft Numbers", "Mikesoft Present", "IntelliG"
          )
      )
  );
  ListProperty<String> favoritesSelection = new SimpleListProperty<>(
      FXCollections.observableArrayList(Lists.newArrayList("Eboda Phot-O-Shop", "Mikesoft Text"))
  );

  public RootPane() {
    getChildren().add(new DemoView(createPreferences(), this));
  }

  private PreferencesFx createPreferences() {
    return PreferencesFx.of(
        Category.of("General",
            Setting.of("Change Brightness", brightness),
            Setting.of("Night mode", nightMode)
        ),
        Category.of("Screen")
            .subCategories(
                Category.of("Scaling & Ordering",
                    Group.of(
                        Setting.of("Scaling", scaling),
                        Setting.of("Screen name", screenName),
                        Setting.of("Resolution", resolutionItems, resolutionSelection),
                        Setting.of("Orientation", orientationItems, orientationSelection)
                    ).description("Screen Options"),
                    Group.of(
                        Setting.of("Font Size", fontSize, 6, 36),
                        Setting.of("Line Spacing", lineSpacing, 0, 3)
                    )
                )
            ),
        Category.of("Favorites",
            Setting.of("Favorites", favoritesItems, favoritesSelection)
        )
    );
  }
}
