package com.dlsc.preferencesfx;

import com.dlsc.formsfx.model.structure.Field;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

public class Setting<F extends Field, P extends Property> {
  private String description;
  private F field;
  private P value;

  Setting(String description, F field, P value) {
    this.description = description;
    this.field = field;
    this.value = value;
  }

  public static Setting of(String description, BooleanProperty property) {
    return new Setting<>(
        description,
        Field.ofBooleanType(property).label(description),
        property);
  }

  public static Setting of(String description, IntegerProperty property) {
    return new Setting<>(
        description,
        Field.ofIntegerType(property).label(description),
        property);
  }

  public static Setting of(String description, StringProperty property) {
    return new Setting<>(
        description,
        Field.ofStringType(property).label(description),
        property);
  }

  public static <P> Setting of(String description, ObservableList<P> items, ObjectProperty<P> selection) {
    return new Setting<>(
        description,
        Field.ofSingleSelectionType(new SimpleListProperty<>(items), selection).label(description),
        selection);
  }

  /*public static <F extends Field, P extends Property> Setting<F, P> of(String description, Node customControl, P value) {
    return new Setting(description, new Widget(widget, value));
  }*/

  public String getDescription() {
    return description;
  }

  public P valueProperty() {
    return value;
  }

  public F getField() {
    return field;
  }
}
