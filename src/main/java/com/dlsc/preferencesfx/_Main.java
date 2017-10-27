package com.dlsc.preferencesfx;

import static com.dlsc.preferencesfx.Setting.Type;

import com.dlsc.preferencesfx.widgets.CustomWidget;
import com.dlsc.preferencesfx.widgets.TextField;
import com.dlsc.preferencesfx.widgets.Widget;
import javafx.scene.Node;
import javafx.scene.control.Slider;

public class _Main {
  public static void main(String[] args) {
    Slider slider = new Slider();

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
  }
}
