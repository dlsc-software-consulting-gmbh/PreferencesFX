package com.dlsc.preferencesfx.history;

import com.dlsc.preferencesfx.Setting;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Created by François Martin on 02.12.17.
 */
public class History {

  private ObservableList<Change> changes = FXCollections.observableArrayList();
  private IntegerProperty position = new SimpleIntegerProperty(0);
  private IntegerProperty validPosition = new SimpleIntegerProperty(0);
  private BooleanProperty undoAvailable = new SimpleBooleanProperty(false);
  private BooleanProperty redoAvailable = new SimpleBooleanProperty(false);

  public History() {
    undoAvailable.bind(position.greaterThan(0));
    redoAvailable.bind(position.lessThan(validPosition));
  }

  public void attachChangeListener(Setting setting) {
    setting.valueProperty().addListener((observable, oldValue, newValue) -> {
      addChange(new Change(setting, oldValue, newValue));
    });
  }

  private void addChange(Change change) {
    if (changes.get(position.get()) != null) { // if there is already an element at the current position
      changes.set(position.get(), change);
    } else {
      changes.add(position.get(), change);
    }
    validPosition.setValue(incrementPosition());
  }

  public boolean undo() {
    Change lastChange = prev();
    if (lastChange != null) {
      lastChange.undo();
      return true;
    }
    return false;
  }

  public boolean redo() {
    Change nextChange = next();
    if (nextChange != null) {
      nextChange.redo();
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
