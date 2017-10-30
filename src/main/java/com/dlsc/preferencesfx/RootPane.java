package com.dlsc.preferencesfx;

import javafx.scene.control.Slider;
import javafx.scene.layout.StackPane;

/**
 * Created by François Martin on 30.10.17.
 */
public class RootPane extends StackPane {
  Slider slider = new Slider();

  PreferencesFX preferencesFX = PreferencesFX.of(
      Category.of("Bildschirm",
          Setting.of("Nachtmodus", Setting.Type.BOOLEAN),
          Setting.of("Skalierung", Setting.Type.INTEGER),
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
}
