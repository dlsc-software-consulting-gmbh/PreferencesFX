package com.dlsc.preferencesfx;

import com.dlsc.preferencesfx.widgets.Widget;
import javafx.beans.property.Property;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

public class Setting<W extends Widget> {
  private String description;
  private W widget;

  Setting(String description, W widget) {
    this.description = description;
    this.widget = widget;
  }

  public static <W extends Widget> Setting of(String description, Type type) { //alle W sind unterschiedlich
    switch (type) {
      case BOOLEAN:
        CheckBox checkBox = new CheckBox();
        return new Setting(description, new Widget(checkBox, checkBox.selectedProperty()));
      case INTEGER:
        TextField textField = new TextField();
        return new Setting(description, new Widget(textField, textField.textProperty()));
      default:
        return null;
    }
  }

  public static <W extends Widget> Setting of(String description, Node widget, Property property) {
    return new Setting(description, new Widget(widget, property));
  }

  enum Type {
    INTEGER, BOOLEAN
  }
}
