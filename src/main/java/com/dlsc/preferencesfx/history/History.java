package com.dlsc.preferencesfx.history;

import com.dlsc.preferencesfx.CategoryTree;
import com.dlsc.preferencesfx.Setting;
import java.util.HashMap;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by François Martin on 02.12.17.
 */
public class History {

  private static final Logger LOGGER =
      LogManager.getLogger(History.class.getName());

  private ObservableList<Change> changes = FXCollections.observableArrayList();
  private IntegerProperty position = new SimpleIntegerProperty(0);
  private IntegerProperty validPosition = new SimpleIntegerProperty(0);
  private BooleanProperty undoAvailable = new SimpleBooleanProperty(false);
  private BooleanProperty redoAvailable = new SimpleBooleanProperty(false);

  private HashMap<Setting, ChangeListener> settingChangeListenerMap = new HashMap<>();

  public History() {
    undoAvailable.bind(position.greaterThan(0));
    redoAvailable.bind(position.lessThan(validPosition));
  }

  public void attachChangeListener(Setting setting) {
    ChangeListener changeEvent = (observable, oldValue, newValue) -> {
      if (oldValue != newValue) {
        LOGGER.trace("Change detected, old: " + oldValue + " new: " + newValue);
        addChange(new Change(setting, oldValue, newValue));
      }
    };
    settingChangeListenerMap.put(setting, changeEvent);
    setting.valueProperty().addListener(changeEvent);
  }

  private void addChange(Change change) {
    LOGGER.trace("addChange, before, size: " + changes.size() + " pos: " + position.get() + " validPos: " + validPosition.get());
    if (position.get() < changes.size()) { // if there is already an element at the current position
      changes.set(position.get(), change);
    } else {
      changes.add(change);
    }
    validPosition.setValue(incrementPosition());
    LOGGER.trace("addChange, after, size: " + changes.size() + " pos: " + position.get() + " validPos: " + validPosition.get());
  }

  public void doWithoutListeners(Setting setting, Runnable action) {
    setting.valueProperty().removeListener(
        settingChangeListenerMap.get(setting)
    );
    action.run();
    setting.valueProperty().addListener(
        settingChangeListenerMap.get(setting)
    );
  }

  public boolean undo() {
    Change lastChange = prev();
    if (lastChange != null) {
      doWithoutListeners(lastChange.setting, () -> lastChange.undo());
      decrementPosition();
      return true;
    }
    return false;
  }

  public boolean redo() {
    Change nextChange = next();
    if (nextChange != null) {
      doWithoutListeners(nextChange.setting, () -> nextChange.redo());
      incrementPosition();
      return true;
    }
    return false;
  }

  private Change next() {
    if (hasNext()) {
      return changes.get(incrementPosition());
    }
    return null;
  }

  private boolean hasNext() {
    return position.get() < validPosition.get();
  }

  private Change prev() {
    if (hasPrev()) {
      return changes.get(decrementPosition());
    }
    return null;
  }

  private boolean hasPrev() {
    return position.get() > 0;
  }

  private int incrementPosition() {
    int nextPosition = position.get() + 1;
    position.setValue(nextPosition);
    return nextPosition;
  }

  private int decrementPosition() {
    int prevPosition = position.get() - 1;
    position.setValue(prevPosition);
    return prevPosition;
  }

  private int incrementValidPosition() {
    int nextPosition = validPosition.get() + 1;
    validPosition.setValue(nextPosition);
    return nextPosition;
  }

  private int decrementValidPosition() {
    int prevPosition = validPosition.get() - 1;
    validPosition.setValue(prevPosition);
    return prevPosition;
  }

  public boolean isUndoAvailable() {
    return undoAvailable.get();
  }

  public BooleanProperty undoAvailableProperty() {
    return undoAvailable;
  }

  public boolean isRedoAvailable() {
    return redoAvailable.get();
  }

  public BooleanProperty redoAvailableProperty() {
    return redoAvailable;
  }
}
