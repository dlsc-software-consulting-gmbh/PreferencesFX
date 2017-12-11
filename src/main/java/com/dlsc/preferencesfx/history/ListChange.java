package com.dlsc.preferencesfx.history;

import com.dlsc.preferencesfx.Setting;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by François Martin on 02.12.17.
 */
public class ListChange<P> extends Change<P<V>> {

  private static final Logger LOGGER =
      LogManager.getLogger(ListChange.class.getName());

  private final SimpleListProperty<P> oldValue = new SimpleListProperty<>();
  private final SimpleListProperty<P> newValue = new SimpleListProperty<>(); // can be changed, if compounded changes occur

  /**
   * Constructs a regular change.
   * @param setting the setting that was changed
   * @param oldValue the "before" value of the change
   * @param newValue the "after" value of the change
   */
  public ListChange(Setting setting, ObservableList<P> listValue) {
    super(setting);
    this.oldValue.set(listValue);
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

  public void setNewValue(ObservableList<P> newValue) {
    this.newValue.set(newValue);
  }

  public Setting getSetting() {
    return setting;
  }

  public SimpleListProperty<P> oldValueProperty() {
    return oldValue;
  }

  public ReadOnlyObjectProperty<P> newValueProperty() {
    return newValue;
  }
}
