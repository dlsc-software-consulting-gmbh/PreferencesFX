package com.dlsc.preferencesfx;

import com.dlsc.preferencesfx.widgets.TextField;
import com.dlsc.preferencesfx.widgets.Widget;

public class WidgetFactory {

  public Widget getWidget(String widgetType) {
    widgetType = widgetType.toUpperCase();

    switch (widgetType) {
      case "TEXTFIELD":
        return new TextField();
      default:
        return null;
    }
  }
}
