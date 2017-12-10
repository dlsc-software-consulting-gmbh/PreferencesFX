package com.dlsc.preferencesfx.history;

import com.dlsc.preferencesfx.Setting;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by François Martin on 02.12.17.
 */
public class Change<P> {

  private static final Logger LOGGER =
      LogManager.getLogger(Change.class.getName());

  protected final Setting setting;

  private final SimpleObjectProperty<P> oldValue = new SimpleObjectProperty<>();
  private final SimpleObjectProperty<P> newValue = new SimpleObjectProperty<>(); // can be changed, if compounded changes occur

  private final LocalDate timestamp;

  /**
   * Constructs a generalized change.
   * @param setting the setting that was changed
   */
  protected Change(Setting setting) {
    this.setting = setting;
    timestamp = LocalDate.now();
  }

  /**
   * Constructs a regular change.
   * @param setting the setting that was changed
   * @param oldValue the "before" value of the change
   * @param newValue the "after" value of the change
   */
  public Change(Setting setting, P oldValue, P newValue) {
    this(setting);
    this.oldValue.set(oldValue);
    this.newValue.set(newValue);
  }

  public Change(Setting setting, ListChangeListener.Change change, SimpleListProperty property) {
    this(setting);
    boolean added = false;
    List elements = new ArrayList();
    List collection = change.getList();

    change.next();
    if (change.wasAdded()) {
      added = true;
      elements = new ArrayList<>(change.getAddedSubList());
    } else if (change.wasRemoved()) {
      added = false;
      elements = new ArrayList<>(change.getRemoved());
    }

    ArrayList oldList = new ArrayList(collection);
    if (added) {
      collection.addAll(elements);
    } else {
      collection.removeAll(elements);
    }
    ArrayList newList = new ArrayList(collection);

    oldValue.set((P)FXCollections.observableArrayList(oldList));

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
