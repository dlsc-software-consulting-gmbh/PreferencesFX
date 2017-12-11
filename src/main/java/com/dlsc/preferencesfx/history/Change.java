package com.dlsc.preferencesfx.history;

import com.dlsc.preferencesfx.Setting;
import java.time.LocalDate;
import java.util.Objects;
import java.util.concurrent.Callable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by François Martin on 02.12.17.
 */
public class Change<P> {

  private static final Logger LOGGER =
      LogManager.getLogger(Change.class.getName());

  protected final Setting setting;

  private final ListProperty<P> oldList = new SimpleListProperty<>();
  private final ListProperty<P> newList = new SimpleListProperty<>();
  private final ObjectProperty<P> oldValue = new SimpleObjectProperty<>();
  private final ObjectProperty<P> newValue = new SimpleObjectProperty<>();
  private final BooleanProperty listChange = new SimpleBooleanProperty();

  private final LocalDate timestamp;

  /**
   * Constructs a generalized change.
   *
   * @param setting the setting that was changed
   */
  protected Change(Setting setting) {
    this.setting = setting;
    timestamp = LocalDate.now();
    setupBindings();

  }

  private void setupBindings() {
    // oldValue will represent the object contained in oldList, if this is not a listChange
    oldValue.bind(Bindings.createObjectBinding(createListToObjectBinding(oldList), oldList));
    // newValue will represent the object contained in newList, if this is not a listChange
    newValue.bind(Bindings.createObjectBinding(createListToObjectBinding(newList), newList));

    // if both one of both lists has multiple values, it's a list change
    listChange.bind(Bindings.createBooleanBinding(
        () -> (
            (!Objects.equals(null, getOldValue())) ||
                (!Objects.equals(null, getOldValue()))
        )
    ));
  }

  /**
   * Creates a function, which handles binding between a ListProperty and an ObjectProperty.
   * <p>
   * In case there is only one element in a List of a ListProperty, this is not a change which has
   * been performed on a list, but a single object.
   * The object property will in case of an object change be the object which was changed,
   * in case of a list change it will be null.
   *
   * @param listProperty to be bound to the object property
   * @return Callable function, which binds a list to an object property.
   */
  private Callable createListToObjectBinding(ListProperty<P> listProperty) {
    return () -> {
      if (listProperty.get().size() == 1) {
        return listProperty.get().get(0);
      }
      return null;
    };
  }

  /**
   * Constructs a change.
   * If both lists only contain one element, it is a regular change.
   * If both lists contain one or multiple elements, it is a list change.
   *
   * @param setting  the setting that was changed
   * @param oldList the "before" value(s) of the change
   * @param newList the "after" value(s) of the change
   */
  public Change(Setting setting, ObservableList<P> oldList, ObservableList<P> newList) {
    this(setting);
    this.oldList.set(oldList);
    this.newList.set(newList);
  }

  /**
   * Compares newValue and oldValue to see if they are the same.
   * If this is the case, this change is redundant, since it doesn't represent a true change.
   * This can happen on compounded changes.
   *
   * @return true if redundant, else if otherwise.
   */
  public boolean isRedundant() {
    return oldValue.get().equals(newValue.get());
  }

  public void undo() {
    setting.valueProperty().setValue(oldValue.get());
  }

  public void redo() {
    setting.valueProperty().setValue(newValue.get());
  }

  public boolean isListChange() {
    return listChange.get();
  }

  public BooleanProperty listChangeProperty() {
    return listChange;
  }

  public P getOldValue() {
    return oldValue.get();
  }

  public P getNewValue() {
    return newValue.get();
  }

  public void setNewValue(P newValue) {
    this.newValue.set(newValue);
  }

  public Setting getSetting() {
    return setting;
  }

  public String getTimestamp() {
    return timestamp.toString();
  }

  public ReadOnlyObjectProperty<P> oldValueProperty() {
    return oldValue;
  }

  public ReadOnlyObjectProperty<P> newValueProperty() {
    return newValue;
  }
}
