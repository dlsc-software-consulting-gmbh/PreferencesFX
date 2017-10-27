package com.dlsc.preferencesfx;

import com.dlsc.preferencesfx.widgets.Widget;
import javafx.scene.control.TextField;

public class WidgetFactory {

  public Widget getWidget(String widgetType) {
    widgetType = widgetType.toUpperCase();

    switch (widgetType) {
      case "TEXTFIELD":
        TextField textField = new TextField();
        return new Widget(textField, textField.textProperty());
      default:
        return null;
    }
  }
}
