package com.dlsc.preferencesfx;

import com.dlsc.preferencesfx.pages.Page;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Created by François Martin on 30.10.17.
 */
public class RootPane extends VBox {

  RootPane(){
    Slider slider = new Slider(0,100,0);
    TextField textField = new TextField("bla");
    setMargin(textField, new Insets(20, 20, 0, 20));
    getChildren().add(textField);

    PreferencesFX preferencesFX = PreferencesFX.of(
        Category.of("Bildschirm",
            Setting.of("Nachtmodus", Type.BOOLEAN),
            Setting.of("Skalierung", Type.INTEGER),
            Setting.of("Custom", slider, slider.valueProperty())
        ),
        Category.of("Benachrichtigungen")
            .withSubCategories(
                Category.of("Allgemein"),
                Category.of("Farbe"),
                Category.of("Schrift")
            ),
        Category.of("Netzbetrieb")
            .withSubCategories(
                Category.of("Allgemein")
            )
    );

    Setting s1 = Setting.of("Nachtmodus", Type.STRING);
    getChildren().add(new Page(new Setting[]{s1}));

    textField.textProperty().bindBidirectional(s1.getWidget().valueProperty());
  }

}
