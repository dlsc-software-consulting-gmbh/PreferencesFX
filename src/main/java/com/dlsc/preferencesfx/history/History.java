package com.dlsc.preferencesfx.history;

import com.dlsc.preferencesfx.Setting;
import java.util.HashMap;
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
  private IntegerProperty position = new SimpleIntegerProperty(-1);
  private IntegerProperty validPosition = new SimpleIntegerProperty(-1);
  private BooleanProperty undoAvailable = new SimpleBooleanProperty(false);
  private BooleanProperty redoAvailable = new SimpleBooleanProperty(false);

  private HashMap<Setting, ChangeListener> settingChangeListenerMap = new HashMap<>();

  public History() {
    setupBindings();
  }

  private void setupBindings() {
    undoAvailable.bind(position.greaterThanOrEqualTo(0));
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
    // check if change is on same setting as the last change => compounded change
    if (changes.size() > 0 && position.get() != -1 &&
        changes.get(position.get()).getSetting().equals(change.getSetting())) {
      LOGGER.trace("Compounded change");
      changes.get(position.get()).setNewValue(change.getNewValue());
    } else {
      LOGGER.trace("New change");
      int lastIndex = changes.size() - 1;
      if (position.get() < lastIndex) { // if there is already an element at the current position
        changes.set(incrementPosition(), change);
      } else {
        changes.add(change);
        incrementPosition();
      }
      validPosition.setValue(position.get());
    }
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
    LOGGER.trace("undo, before, size: " + changes.size() + " pos: " + position.get() + " validPos: " + validPosition.get());
    Change lastChange = prev();
    if (lastChange != null) {
      doWithoutListeners(lastChange.getSetting(), () -> lastChange.undo());
      LOGGER.trace("undo, after, size: " + changes.size() + " pos: " + position.get() + " validPos: " + validPosition.get());
      return true;
    }
    return false;
  }

  public boolean redo() {
    LOGGER.trace("redo, before, size: " + changes.size() + " pos: " + position.get() + " validPos: " + validPosition.get());
    Change nextChange = next();
    if (nextChange != null) {
      doWithoutListeners(nextChange.getSetting(), () -> nextChange.redo());
      LOGGER.trace("redo, after, size: " + changes.size() + " pos: " + position.get() + " validPos: " + validPosition.get());
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
    return redoAvailable.get();
  }

  private Change prev() {
    if (hasPrev()) {
      return changes.get(decrementPosition());
    }
    return null;
  }

  private boolean hasPrev() {
    return undoAvailable.get();
  }

  /**
   * Equals to the same as: "return ++position" if position was an Integer.
   *
   * @return the position value before the incrementation
   */
  private int incrementPosition() {
    int positionValue = position.get() + 1;
    position.setValue(positionValue);
    return positionValue;
  }

  /**
   * Equals to the same as: "return position--" if position was an Integer.
   *
   * @return the position value before the decrementation
   */
  private int decrementPosition() {
    int positionValue = position.get();
    position.setValue(positionValue - 1);
    return positionValue;
  }

  /**
   * Equals to the same as: "return ++validPosition" if validPosition was an Integer.
   *
   * @return the last valid position value before the incrementation
   */
  private int incrementValidPosition() {
    int positionValue = validPosition.get() + 1;
    validPosition.setValue(positionValue);
    return positionValue;
  }

  /**
   * Equals to the same as: "return validPosition--" if validPosition was an Integer.
   *
   * @return the last valid position value before the decrementation
   */
  private int decrementValidPosition() {
    int positionValue = position.get();
    validPosition.setValue(positionValue - 1);
    return positionValue;
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

  public ObservableList<Change> getChanges() {
    return FXCollections.observableArrayList(changes);
  }
}
