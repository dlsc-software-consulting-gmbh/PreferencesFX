package com.dlsc.preferencesfx;

import com.dlsc.preferencesfx.widgets.TextField;
import com.dlsc.preferencesfx.widgets.Toggle;
import com.dlsc.preferencesfx.widgets.Widget;

public class Setting<W extends Widget> {
  private String description;
  private W widget;

  Setting(String description, W widget) {
    this.description = description;
    this.widget = widget;
  }

  public static <W extends Widget> Setting of(String description, Type type) { //beide W sind unterschiedlich
    W widget = null;
    switch (type) {
      case BOOLEAN:
        widget = (W) new Toggle();
      case INTEGER:
        widget = (W) new TextField();
    }
    return new Setting(description, widget);
  }

  enum Type {
    INTEGER, BOOLEAN
  }
}
