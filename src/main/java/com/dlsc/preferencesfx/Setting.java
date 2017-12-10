package com.dlsc.preferencesfx;

import com.dlsc.formsfx.model.structure.Field;
import com.dlsc.preferencesfx.util.StorageHandler;
import com.dlsc.preferencesfx.formsfx.view.controls.DoubleSliderControl;
import com.dlsc.preferencesfx.formsfx.view.controls.IntegerSliderControl;
import com.dlsc.preferencesfx.formsfx.view.controls.ToggleControl;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class Setting<F extends Field, P extends Property> {
  public static final String MARKED_STYLE_CLASS = "simple-control-marked";
  private String description;
  private F field;
  private P value;
  private boolean marked = false;
  private final EventHandler<MouseEvent> unmarker = event -> unmark();
  private String breadcrumb = "";

  private Setting(String description, F field, P value) {
    this.description = description;
    this.field = field;
    this.value = value;
  }

  public static Setting of(String description, BooleanProperty property) {
    return new Setting<>(
        description,
        Field.ofBooleanType(property).label(description).render(new ToggleControl()),
        property
    );
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

  public static Setting of(
      String description, DoubleProperty property, double min, double max, int precision) {
    return new Setting<>(
        description,
        Field.ofDoubleType(property).label(description).render(
            new DoubleSliderControl(min, max, precision)),
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

  /**
   * Creates a setting of a custom defined field.
   *
   * @param description title of the setting
   * @param field       custom Field object from FormsFX
   * @param property    property with relevant value to be bound and saved
   * @return constructed setting
   */
  public static <F extends Field<F>, P extends Property> Setting of(
      String description, F field, P property) {
    return new Setting<>(
        description,
        field.label(description),
        property);
  }

  public void mark() {
    // ensure it's not marked yet - so a control doesn't contain the same styleClass multiple times
    if (!marked) {
      getField().getRenderer().addStyleClass(MARKED_STYLE_CLASS);
      getField().getRenderer().setOnMouseExited(unmarker);
      marked = !marked;
    }
  }

  public void unmark() {
    // check if it's marked before removing the style class
    if (marked) {
      getField().getRenderer().removeStyleClass(MARKED_STYLE_CLASS);
      getField().getRenderer().removeEventHandler(MouseEvent.MOUSE_EXITED, unmarker);
      marked = !marked;
    }
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

  public void saveSettingValue(StorageHandler storageHandler) {
    storageHandler.saveObject(breadcrumb, value.getValue());
  }

  public void loadSettingValue(StorageHandler storageHandler) {
    if (value instanceof ListProperty) {
      value.setValue(
          storageHandler.loadObservableList(breadcrumb, (ObservableList) value.getValue())
      );
    } else {
      value.setValue(storageHandler.loadObject(breadcrumb, value.getValue()));
    }
  }

  public void addToBreadcrumb(String breadCrumb) {
    this.breadcrumb = breadCrumb + PreferencesFx.BREADCRUMB_DELIMITER + description;
  }

  public String getBreadcrumb() {
    return breadcrumb;
  }
}
