package com.dlsc.preferencesfx;

import com.dlsc.formsfx.model.structure.Field;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.beans.property.StringProperty;

public class Setting<F extends Field, P extends Property> {
  private String description;
  private Type type;
  private F field;
  private P value;

  Setting(String description, Type type, F field, P value) {
    this.description = description;
    this.field = field;
    this.value = value;
    this.type = type;
  }

  public static <F extends Field, P extends Property> Setting<F, P> of(String description, Type type, P property) { //alle W sind unterschiedlich
    switch (type) {
      case BOOLEAN:
        return new Setting(
            description,
            type,
            Field.ofBooleanType((BooleanProperty) property).label(description),
            property);
      case INTEGER:
        return new Setting(
            description,
            type,
            Field.ofIntegerType((IntegerProperty) property).label(description),
            property);
      case STRING:
        return new Setting(
            description,
            type,
            Field.ofStringType((StringProperty) property).label(description),
            property);
      default:
        return null;
    }
  }

  /*public static <F extends Field, P extends Property> Setting<F, P> of(String description, Node customControl, P value) {
    return new Setting(description, new Widget(widget, value));
  }*/

  public String getDescription() {
    return description;
  }

  public Type getType() {
    return type;
  }

  public P valueProperty() {
    return value;
  }

  public F getField() {
    return field;
  }
}
