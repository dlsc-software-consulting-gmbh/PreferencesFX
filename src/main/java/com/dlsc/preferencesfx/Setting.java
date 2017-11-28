package com.dlsc.preferencesfx;

import com.dlsc.formsfx.model.structure.Field;
import com.dlsc.preferencesfx.util.DoubleSliderControl;
import com.dlsc.preferencesfx.util.IntegerSliderControl;
import com.dlsc.preferencesfx.util.ToggleControl;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

public class Setting<F extends Field, P extends Property> {
  private String description;
  private F field;
  private P value;

  private Setting(String description, F field, P value) {
    this.description = description;
    this.field = field;
    this.value = value;
  }

  public static Setting of(String description, BooleanProperty property) {
    return new Setting<>(
        description,
        Field.ofBooleanType(property).label(description).render(new ToggleControl()),
        property);
  }

  public static Setting of(String description, IntegerProperty property) {
    return new Setting<>(
        description,
        Field.ofIntegerType(property).label(description),
        property);
  }

  public static Setting of(String description, DoubleProperty property) {
    return new Setting<>(
        description,
        Field.ofDoubleType(property).label(description),
        property);
  }

  public static Setting of(String description, DoubleProperty property, double min, double max, int precision) {
    return new Setting<>(
        description,
        Field.ofDoubleType(property).label(description).render(new DoubleSliderControl(min, max, precision)),
        property);
  }

  public static Setting of(String description, IntegerProperty property, int min, int max) {
    return new Setting<>(
        description,
        Field.ofIntegerType(property).label(description).render(new IntegerSliderControl(min, max)),
        property);
  }

  public static Setting of(String description, StringProperty property) {
    return new Setting<>(
        description,
        Field.ofStringType(property).label(description),
        property);
  }

  public static <P> Setting of(
      String description, ListProperty<P> items, ObjectProperty<P> selection) {
    return new Setting<>(
        description,
        Field.ofSingleSelectionType(items, selection).label(description),
        selection);
  }

  public static <P> Setting of(
      String description, ObservableList<P> items, ObjectProperty<P> selection) {
    return new Setting<>(
        description,
        Field.ofSingleSelectionType(new SimpleListProperty<>(items), selection).label(description),
        selection);
  }

  /**
   * Creates a combobox with multiselection.
   * At least one element has to be selected at all times.
   */
  public static <P> Setting of(
      String description, ListProperty<P> items, ListProperty<P> selections) {
    return new Setting<>(
        description,
        Field.ofMultiSelectionType(items, selections).label(description),
        selections);
  }

  /**
   * Creates a combobox with multiselection.
   * At least one element has to be selected at all times.
   */
  public static <P> Setting of(
      String description, ObservableList<P> items, ListProperty<P> selections) {
    return new Setting<>(
        description,
        Field.ofMultiSelectionType(new SimpleListProperty<>(items), selections).label(description),
        selections);
  }

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
