package com.dlsc.preferencesfx.history;

import com.dlsc.preferencesfx.model.Setting;
import com.dlsc.preferencesfx.util.SettingsListener;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Manages a list of changes, so undo / redo functionality can be used with {@link Setting}.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class History implements SettingsListener {

  private static final Logger LOGGER =
      LogManager.getLogger(History.class.getName());

  private ObservableList<Change> changes = FXCollections.observableArrayList();
  private SimpleObjectProperty<Change> currentChange = new SimpleObjectProperty<>();
  private IntegerProperty position = new SimpleIntegerProperty(-1);
  private IntegerProperty validPosition = new SimpleIntegerProperty(-1);
  private BooleanProperty undoAvailable = new SimpleBooleanProperty(false);
  private BooleanProperty redoAvailable = new SimpleBooleanProperty(false);

  private BooleanProperty listenerActive = new SimpleBooleanProperty(true);
  private final Collection<SettingsListener> listeners = new ArrayList<>(2);

  /**
   * Initializes a new history object.
   */
  public History() {
    setupBindings();
    listeners.add(this);
  }

  private void setupBindings() {
    undoAvailable.bind(position.greaterThanOrEqualTo(0));
    redoAvailable.bind(position.lessThan(validPosition));
    currentChange.bind(Bindings.createObjectBinding(() -> {
      int index = position.get();
      if (index >= 0 && index < changes.size()) {
        LOGGER.trace("Set item");
        return changes.get(index);
      }
      return null;
    }, position));
  }

  /**
   * Adds a listener to the {@code setting}, so every time the value of the {@code setting} changes,
   * a new {@link Change} will be created and added to the list of changes.
   *
   * @param setting the setting to observe for changes
   */
  public void attachChangeListener(Setting setting) {
    setting.valueProperty().addListener((p, oldValue, newValue) -> {
      LOGGER.trace("Change detected, old: " + oldValue + " new: " + newValue);
      listeners.forEach(l -> l.settingChanged(setting, oldValue, newValue));
    });
  }

  public void settingChanged(Setting setting, Object oldValue, Object newValue) {
    if(!isListenerActive() || oldValue == newValue)
      return;
    LOGGER.trace("addChange for: {}, before, size: {}, pos: {}, validPos: {}", setting, changes.size(), position.get(), validPosition.get());

    int pos = position.get();
    Change previous = getChangeAt(pos);

    if (previous != null && previous.setting.equals(setting)) {
      LOGGER.trace("Compounding Change");
      changes.set(pos, new Change<>(setting, previous.oldValue, newValue));
    } else {
      Change change = new Change<>(setting, oldValue, newValue);
      boolean redundant = previous != null && previous.isRedundant();
      if(!redundant)
        pos = incrementPosition();
      if (pos < changes.size() - 1 || redundant) {
        LOGGER.trace("Overwriting previous Change");
        changes.set(pos, change);
      } else {
        LOGGER.trace("Adding new Change");
        changes.add(change);
      }
    }

    int lastIndex = changes.size() - 1;
    // if there are changes after the currently added item
    if (position.get() != lastIndex) {
      LOGGER.trace("Invalidating obsolete Changes");
      changes.remove(position.get() + 1, changes.size());
    }

    // the last valid position is now equal to the current position
    validPosition.setValue(position.get());

    LOGGER.trace("addChange for: {}, after, size: {}, pos: {}, validPos: {}", setting, changes.size(), position.get(), validPosition.get());
  }

  private Change getChangeAt(int pos) {
    return pos > -1 && changes.size() > 0 ? changes.get(pos) : null;
  }

  /**
   * Enables to perform an action, without firing the attached ChangeListener of a Setting.
   * This is used by undo and redo, since those shouldn't cause a new change to be added.
   *
   * @param setting the setting, whose ChangeListener should be ignored
   * @param action  the action to be performed
   */
  public void doWithoutListeners(Setting setting, Runnable action) {
    LOGGER.trace("doWithoutListeners: setting: {}", setting);
    setListenerActive(false);
    LOGGER.trace("removed listener");
    action.run();
    LOGGER.trace("performed action");
    setListenerActive(true);
    LOGGER.trace("add listener back");
  }

  /**
   * Undos a change in the history.
   *
   * @return true if successful, false if there are no changes to undo
   */
  public boolean undo() {
    LOGGER.trace("undo, before, size: " + changes.size() + " pos: " + position.get()
        + " validPos: " + validPosition.get());
    Change lastChange = prev();
    if (lastChange != null) {
      doWithoutListeners(lastChange.setting, lastChange::undo);
      LOGGER.trace("undo, after, size: " + changes.size() + " pos: " + position.get()
          + " validPos: " + validPosition.get());
      return true;
    }
    return false;
  }

  /**
   * Undos all changes in the history.
   */
  public void undoAll() {
    while (undo()) {
    }
  }

  /**
   * Redos a change in the history.
   *
   * @return true if successful, false if there are no changes to redo
   */
  public boolean redo() {
    LOGGER.trace("redo, before, size: " + changes.size() + " pos: " + position.get()
        + " validPos: " + validPosition.get());
    Change nextChange = next();
    if (nextChange != null) {
      doWithoutListeners(nextChange.setting, nextChange::redo);
      LOGGER.trace("redo, after, size: " + changes.size() + " pos: " + position.get()
          + " validPos: " + validPosition.get());
      return true;
    }
    return false;
  }

  /**
   * Redos all changes in the history.
   */
  public void redoAll() {
    while (redo()) {
    }
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
   * Clears the change history.
   *
   * @param undoAll if true, will undo all changes before clearing
   */
  public void clear(boolean undoAll) {
    LOGGER.trace("Clear called, with undoAll: " + undoAll);
    if (undoAll) {
      undoAll();
    }
    LOGGER.trace("Clearing changes");
    changes.clear();
    position.set(-1);
    validPosition.set(-1);
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
    return changes;
  }

  public ReadOnlyObjectProperty<Change> currentChangeProperty() {
    return currentChange;
  }

  private boolean isListenerActive() {
    return listenerActive.get();
  }

  private void setListenerActive(boolean listenerActive) {
    this.listenerActive.set(listenerActive);
  }

}
