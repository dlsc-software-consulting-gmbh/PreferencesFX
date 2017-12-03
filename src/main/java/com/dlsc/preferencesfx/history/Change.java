package com.dlsc.preferencesfx.history;

import com.dlsc.preferencesfx.Setting;
import java.time.LocalDate;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by François Martin on 02.12.17.
 */
public class Change<P> {

  private static final Logger LOGGER =
      LogManager.getLogger(Change.class.getName());

  private final Setting setting;

  private final SimpleObjectProperty<P> oldValue;
  private final SimpleObjectProperty<P> newValue; // can be changed, if compounded changes occur

  private final LocalDate timestamp;

  public Change(Setting setting, P oldValue, P newValue) {
    this.setting = setting;
    this.oldValue = new SimpleObjectProperty<>(oldValue);
    this.newValue = new SimpleObjectProperty<>(newValue);
    timestamp = LocalDate.now();
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
