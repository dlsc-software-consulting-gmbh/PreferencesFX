package com.dlsc.preferencesfx;

import com.dlsc.formsfx.model.structure.Field;
import com.dlsc.preferencesfx.widgets.Widget;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.beans.property.StringProperty;

public class Setting<W extends Widget> {
  private String description;
  private W widget;

  Setting(String description, W widget) {
    this.description = description;
    this.widget = widget;
  }

  public static <W extends Widget> Setting of(String description, Type type, Property property) { //alle W sind unterschiedlich
    switch (type) {
      case BOOLEAN:
        return new Setting(description, new Widget(Field.ofBooleanType((BooleanProperty) property), property));
      case INTEGER:
        return new Setting(description, new Widget(Field.ofIntegerType((IntegerProperty) property), property));
      case STRING:
        return new Setting(description, new Widget(Field.ofStringType((StringProperty) property), property));
      default:
        return null;
    }
  }

  /*public static <W extends Widget> Setting of(String description, Node widget, Property property) {
    return new Setting(description, new Widget(widget, property));
  }*/

  public String getDescription() {
    return description;
  }

  public W getWidget() {
    return widget;
  }
}