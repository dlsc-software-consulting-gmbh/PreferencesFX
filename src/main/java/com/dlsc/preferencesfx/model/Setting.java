package com.dlsc.preferencesfx.model;

import com.dlsc.formsfx.model.structure.DataField;
import com.dlsc.formsfx.model.structure.Field;
import com.dlsc.formsfx.model.validators.Validator;
import com.dlsc.preferencesfx.formsfx.view.controls.DoubleSliderControl;
import com.dlsc.preferencesfx.formsfx.view.controls.IntegerSliderControl;
import com.dlsc.preferencesfx.formsfx.view.controls.SimpleComboBoxControl;
import com.dlsc.preferencesfx.formsfx.view.controls.SimpleControl;
import com.dlsc.preferencesfx.formsfx.view.controls.SimpleDoubleControl;
import com.dlsc.preferencesfx.formsfx.view.controls.SimpleIntegerControl;
import com.dlsc.preferencesfx.formsfx.view.controls.SimpleListViewControl;
import com.dlsc.preferencesfx.formsfx.view.controls.SimpleTextControl;
import com.dlsc.preferencesfx.formsfx.view.controls.ToggleControl;
import com.dlsc.preferencesfx.util.Constants;
import com.dlsc.preferencesfx.util.StorageHandler;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * TODO: Add javadoc.
 * @author François Martin
 * @author Marco Sanfratello
 */
public class Setting<F extends Field, P extends Property> {
  private static final Logger LOGGER =
      LogManager.getLogger(Setting.class.getName());

  public static final String MARKED_STYLE_CLASS = "simple-control-marked";
  private String description;
  private F field;
  private P value;
  private boolean marked = false;
  private final EventHandler<MouseEvent> unmarker = event -> unmark();
  private final StringProperty breadcrumb = new SimpleStringProperty("");

  private Setting(String description, F field, P value) {
    this.description = description;
    this.field = field;
    this.value = value;
  }

  /**
   * TODO: Add javadoc.
   * @param description TODO: Add javadoc.
   * @param property TODO: Add javadoc.
   * @return TODO: Add javadoc.
   */
  public static Setting of(String description, BooleanProperty property) {
    return new Setting<>(
        description,
        Field.ofBooleanType(property)
            .label(description)
            .render(new ToggleControl()),
        property
    );
  }

  /**
   * TODO: Add javadoc.
   * @param description TODO: Add javadoc.
   * @param property TODO: Add javadoc.
   * @return TODO: Add javadoc.
   */
  public static Setting of(String description, IntegerProperty property) {
    return new Setting<>(
        description,
        Field.ofIntegerType(property)
            .label(description)
            .render(new SimpleIntegerControl()),
        property);
  }

  /**
   * TODO: Add javadoc.
   * @param description TODO: Add javadoc.
   * @param property TODO: Add javadoc.
   * @return TODO: Add javadoc.
   */
  public static Setting of(String description, DoubleProperty property) {
    return new Setting<>(
        description,
        Field.ofDoubleType(property)
            .label(description)
            .render(new SimpleDoubleControl()),
        property);
  }

  /**
   * TODO: Add javadoc.
   * @param description TODO: Add javadoc.
   * @param property TODO: Add javadoc.
   * @param min TODO: Add javadoc.
   * @param max TODO: Add javadoc.
   * @param precision TODO: Add javadoc.
   * @return TODO: Add javadoc.
   */
  public static Setting of(
      String description, DoubleProperty property, double min, double max, int precision) {
    return new Setting<>(
        description,
        Field.ofDoubleType(property)
            .label(description)
            .render(new DoubleSliderControl(min, max, precision)),
        property);
  }

  /**
   * TODO: Add javadoc.
   * @param description TODO: Add javadoc.
   * @param property TODO: Add javadoc.
   * @param min TODO: Add javadoc.
   * @param max TODO: Add javadoc.
   * @return TODO: Add javadoc.
   */
  public static Setting of(String description, IntegerProperty property, int min, int max) {
    return new Setting<>(
        description,
        Field.ofIntegerType(property)
            .label(description)
            .render(new IntegerSliderControl(min, max)),
        property);
  }

  /**
   * TODO: Add javadoc.
   * @param description TODO: Add javadoc.
   * @param property TODO: Add javadoc.
   * @return TODO: Add javadoc.
   */
  public static Setting of(String description, StringProperty property) {
    return new Setting<>(
        description,
        Field.ofStringType(property)
            .label(description)
            .render(new SimpleTextControl()),
        property);
  }

  /**
   * TODO: Add javadoc.
   * @param description TODO: Add javadoc.
   * @param items TODO: Add javadoc.
   * @param selection TODO: Add javadoc.
   * @param <P> TODO: Add javadoc.
   * @return TODO: Add javadoc.
   */
  public static <P> Setting of(
      String description, ListProperty<P> items, ObjectProperty<P> selection) {
    return new Setting<>(
        description,
        Field.ofSingleSelectionType(items, selection)
            .label(description)
            .render(new SimpleComboBoxControl<>()),
        selection);
  }

  /**
   * TODO: Add javadoc.
   * @param description TODO: Add javadoc.
   * @param items TODO: Add javadoc.
   * @param selection TODO: Add javadoc.
   * @param <P> TODO: Add javadoc.
   * @return TODO: Add javadoc.
   */
  public static <P> Setting of(
      String description, ObservableList<P> items, ObjectProperty<P> selection) {
    return new Setting<>(
        description,
        Field.ofSingleSelectionType(new SimpleListProperty<>(items), selection)
            .label(description)
            .render(new SimpleComboBoxControl<>()),
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
        Field.ofMultiSelectionType(items, selections)
            .label(description)
            .render(new SimpleListViewControl<>()),
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
        Field.ofMultiSelectionType(new SimpleListProperty<>(items), selections)
            .label(description)
            .render(new SimpleListViewControl<>()),
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

  /**
   * Sets the list of validators for the current field. This overrides all
   * validators that have previously been added.
   *
   * @param newValue The validators that are to be used for validating this
   *                 field. Limited to validators that are able to handle the
   *                 field's underlying type.
   * @return the current setting to allow for chaining
   * @throws UnsupportedOperationException if this {@link Field} is not instanceof {@link DataField}
   */
  @SafeVarargs
  public final Setting validate(Validator... newValue) {
    if (field instanceof DataField) {
      ((DataField) field).validate(newValue);
    } else {
      throw new UnsupportedOperationException("Field type must be instance of DataField");
    }
    return this;
  }

  /**
   * TODO: Add javadoc.
   */
  public void mark() {
    // ensure it's not marked yet - so a control doesn't contain the same styleClass multiple times
    if (!marked) {
      SimpleControl renderer = (SimpleControl) getField().getRenderer();
      Node markNode = renderer.getFieldLabel();
      markNode.getStyleClass().add(MARKED_STYLE_CLASS);
      markNode.setOnMouseExited(unmarker);
      marked = !marked;
    }
  }

  /**
   * TODO: Add javadoc.
   */
  public void unmark() {
    // check if it's marked before removing the style class
    if (marked) {
      SimpleControl renderer = (SimpleControl) getField().getRenderer();
      Node markNode = renderer.getFieldLabel();
      markNode.getStyleClass().remove(MARKED_STYLE_CLASS);
      markNode.removeEventHandler(MouseEvent.MOUSE_EXITED, unmarker);
      marked = !marked;
    }
  }

  /**
   * TODO: Add javadoc.
   * @return TODO: Add javadoc.
   */
  public String getDescription() {
    if (field != null) {
      return field.getLabel();
    }
    return description;
  }

  public P valueProperty() {
    return value;
  }

  public F getField() {
    return field;
  }

  /**
   * TODO: Add javadoc.
   * @param storageHandler TODO: Add javadoc.
   */
  public void saveSettingValue(StorageHandler storageHandler) {
    storageHandler.saveObject(getBreadcrumb(), value.getValue());
  }

  /**
   * TODO: Add javadoc.
   * @param storageHandler TODO: Add javadoc.
   */
  public void loadSettingValue(StorageHandler storageHandler) {
    if (value instanceof ListProperty) {
      value.setValue(
          storageHandler.loadObservableList(getBreadcrumb(), (ObservableList) value.getValue())
      );
    } else {
      value.setValue(storageHandler.loadObject(getBreadcrumb(), value.getValue()));
    }
  }

  /**
   * TODO: Add javadoc.
   * @param breadCrumb TODO: Add javadoc.
   */
  public void addToBreadcrumb(String breadCrumb) {
    setBreadcrumb(breadCrumb + Constants.BREADCRUMB_DELIMITER + description);
  }

  public String getBreadcrumb() {
    return breadcrumb.get();
  }

  public StringProperty breadcrumbProperty() {
    return breadcrumb;
  }

  public void setBreadcrumb(String breadcrumb) {
    this.breadcrumb.set(breadcrumb);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Setting<?, ?> setting = (Setting<?, ?>) o;
    return Objects.equals(breadcrumb, setting.breadcrumb);
  }

  @Override
  public int hashCode() {
    return Objects.hash(breadcrumb);
  }

  @Override
  public String toString() {
    return getBreadcrumb();
  }
}
