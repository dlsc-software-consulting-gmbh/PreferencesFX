package com.dlsc.preferencesfx.history;

import com.dlsc.preferencesfx.Setting;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
/**
 * Created by François Martin on 10.12.2017.
 */
public class ListChange<P> extends Change<P> {

  private SimpleListProperty<P> oldValue = new SimpleListProperty<>();
  private SimpleListProperty<P> newValue = new SimpleListProperty<>();

  public ListChange(Setting setting, ListChangeListener.Change change) {
    super(setting);
    boolean added = false;
    List<P> collection = change.getList();
    List<P> elements = new ArrayList<>();

    change.next();
    if (change.wasAdded()) {
      added = true;
      elements = new ArrayList<>(change.getAddedSubList());
    } else if (change.wasRemoved()) {
      added = false;
      elements = new ArrayList<>(change.getRemoved());
    }

    ArrayList<P> oldList = new ArrayList<>(collection);
    if (added) {
      collection.addAll(elements);
    } else {
      collection.removeAll(elements);
    }
    ArrayList<P> newList = new ArrayList<>(collection);

    oldValue.set(FXCollections.observableArrayList(oldList));
    newValue.set(FXCollections.observableArrayList(newList));
  }

  @Override
  public void undo() {
    setting.valueProperty().setValue(oldValue.get());
  }

  @Override
  public void redo() {
    setting.valueProperty().setValue(newValue.get());
  }



}
