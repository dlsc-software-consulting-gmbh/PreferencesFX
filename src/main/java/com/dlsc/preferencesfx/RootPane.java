package com.dlsc.preferencesfx;

import com.dlsc.preferencesfx.pages.Page;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

/**
 * Created by François Martin on 30.10.17.
 */
public class RootPane extends StackPane {

  RootPane(){
    Slider slider = new Slider(0,100,0);
    TextField tf = new TextField("bla");
    getChildren().add(tf);

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
    Setting s2 = Setting.of("Nachtmodus2", Type.STRING);
    getChildren().add(new Page(new Setting[]{s1, s2}));

    tf.textProperty().bind(s1.getWidget().valueProperty());
  }

}
