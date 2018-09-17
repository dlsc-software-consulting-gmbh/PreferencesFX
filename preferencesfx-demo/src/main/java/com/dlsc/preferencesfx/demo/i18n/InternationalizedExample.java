package com.dlsc.preferencesfx.demo.i18n;

import com.dlsc.formsfx.model.structure.Field;
import com.dlsc.formsfx.model.structure.IntegerField;
import com.dlsc.formsfx.model.util.ResourceBundleService;
import com.dlsc.formsfx.model.validators.DoubleRangeValidator;
import com.dlsc.preferencesfx.demo.AppStarter;
import com.dlsc.preferencesfx.PreferencesFx;
import com.dlsc.preferencesfx.formsfx.view.controls.IntegerSliderControl;
import com.dlsc.preferencesfx.model.Category;
import com.dlsc.preferencesfx.model.Group;
import com.dlsc.preferencesfx.model.Setting;
import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;
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

public class InternationalizedExample extends StackPane {

  public PreferencesFx preferencesFx;

  // General
  StringProperty welcomeText = new SimpleStringProperty("Hello World");
  IntegerProperty brightness = new SimpleIntegerProperty(50);
  BooleanProperty nightMode = new SimpleBooleanProperty(true);

  // Screen
  DoubleProperty scaling = new SimpleDoubleProperty(1);
  StringProperty screenName = new SimpleStringProperty("PreferencesFx Monitor");

  ObservableList<String> resolutionItems = FXCollections.observableArrayList(Arrays.asList(
      "1024x768", "1280x1024", "1440x900", "1920x1080")
  );
  ObjectProperty<String> resolutionSelection = new SimpleObjectProperty<>("1024x768");

  ListProperty<String> orientationItems = new SimpleListProperty<>(
      FXCollections.observableArrayList(Arrays.asList("Vertical", "Horizontal"))
  );
  ObjectProperty<String> orientationSelection = new SimpleObjectProperty<>("Vertical");

  IntegerProperty fontSize = new SimpleIntegerProperty(12);
  DoubleProperty lineSpacing = new SimpleDoubleProperty(1.5);

  // Favorites
  ListProperty<String> favoritesItems = new SimpleListProperty<>(
      FXCollections.observableArrayList(Arrays.asList(
          "eMovie", "Eboda Phot-O-Shop", "Mikesoft Text",
          "Mikesoft Numbers", "Mikesoft Present", "IntelliG"
          )
      )
  );
  ListProperty<String> favoritesSelection = new SimpleListProperty<>(
      FXCollections.observableArrayList(Arrays.asList(
          "Eboda Phot-O-Shop", "Mikesoft Text"))
  );

  // Custom Control
  IntegerProperty customControlProperty = new SimpleIntegerProperty(42);
  IntegerField customControl = setupCustomControl();

  // i18n
  ResourceBundle rbDE = ResourceBundle.getBundle("preferencesfxx.preferencesfx.demo-locale", new Locale("de", "CH"));
  ResourceBundle rbEN = ResourceBundle.getBundle("preferencesfxx.preferencesfx.demo-locale", new Locale("en", "US"));
  ResourceBundleService rbs = new ResourceBundleService(rbEN);

  public InternationalizedExample() {
    preferencesFx = createPreferences();
    getChildren().add(new DemoView(preferencesFx, this));
  }

  private IntegerField setupCustomControl() {
    return Field.ofIntegerType(customControlProperty).render(
        new IntegerSliderControl(0, 42));
  }

  private PreferencesFx createPreferences() {
    return PreferencesFx.of(AppStarter.class,
        Category.of("general",
            Group.of("greeting",
                Setting.of("welcome", welcomeText)
            ),
            Group.of("display",
                Setting.of("brightness", brightness),
                Setting.of("night_mode", nightMode)
            )
        ),
        Category.of("screen")
            .subCategories(
                Category.of("scaling_ordering",
                    Group.of(
                        Setting.of("scaling", scaling)
                            .validate(DoubleRangeValidator.atLeast(1, "scaling_validate")),
                        Setting.of("screen_name", screenName),
                        Setting.of("resolution", resolutionItems, resolutionSelection),
                        Setting.of("orientation", orientationItems, orientationSelection)
                    ).description("screen_options"),
                    Group.of(
                        Setting.of("font_size", fontSize, 6, 36),
                        Setting.of("line_spacing", lineSpacing, 0, 3, 1)
                    )
                )
            ),
        Category.of("favorites",
            Setting.of("favorites", favoritesItems, favoritesSelection),
            Setting.of("favorite_number", customControl, customControlProperty)
        )
    ).i18n(rbs).persistWindowState(false).saveSettings(true).debugHistoryMode(false).buttonsVisibility(true);
  }

}
