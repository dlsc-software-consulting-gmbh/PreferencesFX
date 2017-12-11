package com.dlsc.preferencesfx.history;

import com.dlsc.preferencesfx.Setting;
import com.google.common.collect.Lists;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.concurrent.Callable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.commons.collections4.CollectionUtils;
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

  private final LocalDateTime timestamp;

  /**
   * Constructs a generalized change.
   *
   * @param setting    the setting that was changed
   * @param listChange true if this is a list change
   */
  protected Change(Setting setting, boolean listChange) {
    this.setting = setting;
    this.listChange.set(listChange);
    timestamp = LocalDateTime.now();
    setupBindings();
  }

  /**
   * Constructs a list change.
   *
   * @param setting the setting that was changed
   * @param oldList the "before" value(s) of the change
   * @param newList the "after" value(s) of the change
   */
  public Change(Setting setting, ObservableList<P> oldList, ObservableList<P> newList) {
    this(setting, true);
    this.oldList.set(FXCollections.observableArrayList(oldList));
    this.newList.set(FXCollections.observableArrayList(newList));
  }

  /**
   * Constructs a regular object change.
   *
   * @param setting  the setting that was changed
   * @param oldValue the "before" value of the change
   * @param newValue the "after" value of the change
   */
  public Change(Setting setting, P oldValue, P newValue) {
    this(setting, false);
    this.oldList.set(FXCollections.observableArrayList(Lists.newArrayList(oldValue)));
    this.newList.set(FXCollections.observableArrayList(Lists.newArrayList(newValue)));
  }

  private void setupBindings() {
    // oldValue will represent the object contained in oldList, if this is not a listChange
    oldValue.bind(
        Bindings.createObjectBinding(createListToObjectBinding(oldList), oldList, listChange)
    );
    // newValue will represent the object contained in newList, if this is not a listChange
    newValue.bind(
        Bindings.createObjectBinding(createListToObjectBinding(newList), newList, listChange)
    );
  }

  /**
   * Creates a function, which handles binding between a ListProperty and an ObjectProperty.
   * <p>
   * If this change isn't a list change, oldValue and newValue properties will have the single
   * element inside of the list, for easier usage.
   *
   * @param listProperty to be bound to the object property
   * @return Callable function, which binds a list to an object property.
   */
  private Callable createListToObjectBinding(ListProperty<P> listProperty) {
    return () -> {
      if (!isListChange() && listProperty.get() != null && listProperty.get().size() != 0) {
        return listProperty.get().get(0);
      }
      return null;
    };
  }

  /**
   * Compares newValue and oldValue to see if they are the same.
   * If this is the case, this change is redundant, since it doesn't represent a true change.
   * This can happen on compounded changes.
   *
   * @return true if redundant, else if otherwise.
   */
  public boolean isRedundant() {
    if (isListChange()) {
      return CollectionUtils.isEqualCollection(oldList.get(), newList.get());
    }
    return oldValue.get().equals(newValue.get());
  }

  public void undo() {
    if (isListChange()) {
      LOGGER.trace("Undoing list change: " + oldList.get().toString());
      setting.valueProperty().setValue(oldList.get());
    } else {
      setting.valueProperty().setValue(oldValue.get());
    }
  }

  public void redo() {
    if (isListChange()) {
      LOGGER.trace("Redoing list change: " + newList.get().toString());
      setting.valueProperty().setValue(newList.get());
    } else {
      setting.valueProperty().setValue(newValue.get());
    }
  }

  public ObservableList<P> getOldList() {
    return oldList.get();
  }

  public ObservableList<P> getNewList() {
    return newList.get();
  }

  public void setNewList(ObservableList<P> newList) {
    LOGGER.trace("Setting new List, old: " + oldList.toString() + " new: " + newList.toString());
    this.newList.set(FXCollections.observableArrayList(newList));
  }

  public boolean isListChange() {
    return listChange.get();
  }

  public ReadOnlyBooleanProperty listChangeProperty() {
    return listChange;
  }

  public P getOldValue() {
    return oldValue.get();
  }

  public P getNewValue() {
    return newValue.get();
  }

  public void setNewValue(P newValue) {
    this.newList.set(FXCollections.observableArrayList(Lists.newArrayList(newValue)));
  }

  public Setting getSetting() {
    return setting;
  }

  public String getTimestamp() {
    DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
    return timestamp.format(formatter);
  }

  public ReadOnlyObjectProperty<P> oldValueProperty() {
    return oldValue;
  }

  public ReadOnlyObjectProperty<P> newValueProperty() {
    return newValue;
  }

  public ReadOnlyListProperty<P> oldListProperty() {
    return oldList;
  }

  public ReadOnlyListProperty<P> newListProperty() {
    return newList;
  }
}
