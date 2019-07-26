package com.dlsc.preferencesfx.demo.extended;

import com.dlsc.formsfx.model.structure.Field;
import com.dlsc.formsfx.model.structure.IntegerField;
import com.dlsc.formsfx.model.validators.DoubleRangeValidator;
import com.dlsc.preferencesfx.demo.AppStarter;
import com.dlsc.preferencesfx.PreferencesFx;
import com.dlsc.preferencesfx.formsfx.view.controls.IntegerSliderControl;
import com.dlsc.preferencesfx.model.Category;
import com.dlsc.preferencesfx.model.Group;
import com.dlsc.preferencesfx.model.Setting;
import java.io.File;
import java.util.Arrays;
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
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class ExtendedExample extends StackPane {

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

  public ExtendedExample() {
    preferencesFx = createPreferences();
    getChildren().add(new DemoView(preferencesFx, this));
  }

  private IntegerField setupCustomControl() {
    return Field.ofIntegerType(customControlProperty).render(
        new IntegerSliderControl(0, 42));
  }

  //  -------------- Demo --------------
//  Theme
  ListProperty<String> themesLst = new SimpleListProperty<>(
      FXCollections.observableArrayList(
          Arrays.asList("IntelliJ", "Darkula", "Windows")
      )
  );
  ObjectProperty<String> themesObj = new SimpleObjectProperty<>("IntelliJ");

  //  IDE
  ListProperty<String> ideLst = new SimpleListProperty<>(
      FXCollections.observableArrayList(
          Arrays.asList("Subpixel", "Greyscale", "No Antializing")
      )
  );
  ObjectProperty<String> ideObj = new SimpleObjectProperty<>("Subpixel");

  //  Editor
  ListProperty<String> editorLst = new SimpleListProperty<>(
      FXCollections.observableArrayList(
          Arrays.asList("Subpixel", "Greyscale", "No Antializing")
      )
  );
  ObjectProperty<String> editorObj = new SimpleObjectProperty<>("Subpixel");

  //  Font size
  ListProperty<String> fontLst = new SimpleListProperty<>(
      FXCollections.observableArrayList(
          Arrays.asList("8", "10", "12", "14", "18", "20", "22", "24", "36", "72")
      )
  );
  ObjectProperty<String> fontObj = new SimpleObjectProperty<>("24");

  //  Project opening
  ListProperty<String> projectOpeningLst = new SimpleListProperty<>(
      FXCollections.observableArrayList(
          Arrays.asList("Open project in new window", "Open project in the same window", "Confirm window to open project in")
      )
  );
  ObjectProperty<String> projectOpeningObj = new SimpleObjectProperty<>("Open project in new window");

  //  Closing tool window
  ListProperty<String> closingToolLst = new SimpleListProperty<>(
      FXCollections.observableArrayList(
          Arrays.asList("Terminate process", "Disconnect (if available)", "Ask")
      )
  );
  ObjectProperty<String> closingToolObj = new SimpleObjectProperty<>("Ask");

  // Custom dialog icon
  Image dialogIcon = new Image(AppStarter.class.getResource("screen_icon.png").toExternalForm());

  // Color picker
  ObjectProperty<Color> colorProperty = new SimpleObjectProperty<>(Color.PAPAYAWHIP);

  // File Chooser
  ObjectProperty<File> fileProperty = new SimpleObjectProperty<>();
  ObjectProperty<File> fileDefaultProperty = new SimpleObjectProperty<>();

  // Directory Chooser
  ObjectProperty<File> directoryProperty = new SimpleObjectProperty<>();
  ObjectProperty<File> directoryDefaultProperty = new SimpleObjectProperty<>();


  private PreferencesFx createPreferences() {
    return PreferencesFx.of(ExtendedExample.class,
        Category.of("General",
            Group.of("Greeting",
                Setting.of("Welcome Text", welcomeText)
            ),
            Group.of("Display",
                Setting.of("Brightness", brightness),
                Setting.of("Night mode", nightMode)
            )
        ),
        Category.of("Screen")
            .subCategories(
                Category.of("Scaling & Ordering",
                    Group.of(
                        Setting.of("Scaling", scaling)
                            .validate(DoubleRangeValidator.atLeast(1, "Scaling needs to be at least 1")),
                        Setting.of("Screen name", screenName),
                        Setting.of("Resolution", resolutionItems, resolutionSelection),
                        Setting.of("Orientation", orientationItems, orientationSelection)
                    ).description("Screen Options"),
                    Group.of(
                        Setting.of("Font Size", fontSize, 6, 36),
                        Setting.of("Font Color", colorProperty),
                        Setting.of("Line Spacing", lineSpacing, 0, 3, 1),
                        Setting.of("File", fileProperty, false),
                        Setting.of("Folder", directoryProperty, "Browse", null, true),
                        Setting.of("File with Default", fileDefaultProperty, new File("/"), false),
                        Setting.of("Folder with Default", directoryDefaultProperty, new File("/"), true)
                        )
                )
            ),
        Category.of("Favorites",
            Setting.of("Favorites", favoritesItems, favoritesSelection),
            Setting.of("Favorite Number", customControl, customControlProperty)
        ),
        Category.of("Appearance & Behaviour")
            .subCategories(
                Category.of("Appearance",
                    Group.of("UI Options",
                        Setting.of("Theme", themesLst, themesObj),
                        Setting.of("Adjust colors for red-green vision defiency " +
                                "(protanopia, deuteranopia)",
                            new SimpleBooleanProperty()),
                        Setting.of("Override default fonts",
                            new SimpleBooleanProperty()),
                        Setting.of("Cyclic scrolling in list",
                            new SimpleBooleanProperty(true)),
                        Setting.of("Show icons in quick navigation",
                            new SimpleBooleanProperty(true)),
                        Setting.of("Automatically position mouse cursor on default button",
                            new SimpleBooleanProperty()),
                        Setting.of("Hide navigation popups on focus loss",
                            new SimpleBooleanProperty(true)),
                        Setting.of("Drag-n-Drop with ALT pressed only",
                            new SimpleBooleanProperty()),
                        Setting.of("Tooltip initial delay (ms)",
                            new SimpleIntegerProperty(1200), 0, 1200)
                    ),
                    Group.of("Antialiasing",
                        Setting.of("IDE", ideLst, ideObj),
                        Setting.of("Editor", editorLst, editorObj)
                    ),
                    Group.of("Window Options",
                        Setting.of("Animate windows",
                            new SimpleBooleanProperty(true)),
                        Setting.of("Show memory indicator",
                            new SimpleBooleanProperty()),
                        Setting.of("Disable mnemnonics in menu",
                            new SimpleBooleanProperty()),
                        Setting.of("Disable mnemnonics in controls",
                            new SimpleBooleanProperty()),
                        Setting.of("Display icons in menu items",
                            new SimpleBooleanProperty(true)),
                        Setting.of("Side-by-side layout on the left",
                            new SimpleBooleanProperty()),
                        Setting.of("Smooth scrolling",
                            new SimpleBooleanProperty()),
                        Setting.of("Show tool window bars",
                            new SimpleBooleanProperty(true)),
                        Setting.of("Show tool window numbers",
                            new SimpleBooleanProperty(true)),
                        Setting.of("Allow merging buttons on dialogs",
                            new SimpleBooleanProperty(true)),
                        Setting.of("Small labels in editor tabs",
                            new SimpleBooleanProperty()),
                        Setting.of("Widescreen tool window layout",
                            new SimpleBooleanProperty()),
                        Setting.of("Side-by-side layout on the right",
                            new SimpleBooleanProperty())
                    ),
                    Group.of("Presentation Mode",
                        Setting.of("Font size", fontLst, fontObj)
                    )
                ),
                Category.of("Menus and Toolbars"),
                Category.of("System Settings",
                    Group.of("Startup / Shutdown",
                        Setting.of("Reopen last project on startup",
                            new SimpleBooleanProperty(true)),
                        Setting.of("Confirm application exit",
                            new SimpleBooleanProperty(true))
                    ),
                    Group.of(
                        Setting.of("Project Opening", projectOpeningLst, projectOpeningObj)
                    ),
                    Group.of("Synchronization",
                        Setting.of("Synchronize files on frame or editor tab activation",
                            new SimpleBooleanProperty(true)),
                        Setting.of("Save files on frame deactivation",
                            new SimpleBooleanProperty(true)),
                        Setting.of("Saves files automatically if application is idle for 15 sec.",
                            new SimpleBooleanProperty()),
                        Setting.of("Use safe write (save changes to a temporary file first)",
                            new SimpleBooleanProperty(true))
                    ),
                    Group.of("Accessibility",
                        Setting.of("Support screen readers (requires restart)",
                            new SimpleBooleanProperty())
                    ),
                    Group.of(
                        Setting.of("On closing Tool Window with Running Process", closingToolLst, closingToolObj)
                    )
                )
                    .subCategories(
                        Category.of("Passwords"),
                        Category.of("HTTP Proxy"),
                        Category.of("Updates"),
                        Category.of("Usage Statistics"),
                        Category.of("Android SDK")
                    ),
                Category.of("File Colors"),
                Category.of("Scopes"),
                Category.of("Notifications"),
                Category.of("Quick Lists"),
                Category.of("Path Variables")
            ),
        Category.of("Keymap"),
        Category.of("Editor")
            .subCategories(
                Category.of("General")
                    .subCategories(
                        Category.of("Auto Import"),
                        Category.of("Appearance"),
                        Category.of("Breadcrumbs"),
                        Category.of("Code Completion"),
                        Category.of("Code Folding"),
                        Category.of("Console"),
                        Category.of("Editor Tabs"),
                        Category.of("Gutter Icons"),
                        Category.of("Postfix Completion"),
                        Category.of("Smart Keys")
                    ),
                Category.of("Font"),
                Category.of("Color Scheme"),
                Category.of("Code Style"),
                Category.of("Inspections"),
                Category.of("File and Code Templates"),
                Category.of("File Encodings"),
                Category.of("Live Templates"),
                Category.of("File Types"),
                Category.of("Android Layout Editor"),
                Category.of("Copyright")
                    .subCategories(
                        Category.of("Copyright Profiles"),
                        Category.of("Formatting")
                    ),
                Category.of("Andoid Data Binding"),
                Category.of("Emmet")
                    .subCategories(
                        Category.of("HTML"),
                        Category.of("CSS"),
                        Category.of("JSX")
                    ),
                Category.of("GUI Designer"),
                Category.of("Images"),
                Category.of("Intentions"),
                Category.of("Language Injections")
                    .subCategories(
                        Category.of("Advanced")
                    ),
                Category.of("Spelling"),
                Category.of("TODO")
            ),
        Category.of("Plugins"),
        Category.of("Version Control"),
        Category.of("Build, Execution, Deployment"),
        Category.of("Languages & Frameworks"),
        Category.of("Tools")
            .subCategories(
                Category.of("Web Browsers"),
                Category.of("External Tools"),
                Category.of("Terminal"),
                Category.of("Database")
                    .subCategories(
                        Category.of("Data Views"),
                        Category.of("User Parameters"),
                        Category.of("CSV Formats")
                    ),
                Category.of("SSH Terminal"),
                Category.of("Diagrams"),
                Category.of("Diff & Merge")
                    .subCategories(
                        Category.of("External Diff Tools")
                    ),
                Category.of("Remote SSH External Tools"),
                Category.of("Server Certificates"),
                Category.of("Settings Repository"),
                Category.of("Startup Tasks"),
                Category.of("Tasks")
                    .subCategories(
                        Category.of("Servers"),
                        Category.of("Time Tracking")
                    ),
                Category.of("Web Services"),
                Category.of("XPath Viewer")
            ),
        Category.of("Other Settings")
    ).persistWindowState(false).saveSettings(true).debugHistoryMode(false).buttonsVisibility(true)
        .dialogTitle("Settings").dialogIcon(dialogIcon);
  }
}
