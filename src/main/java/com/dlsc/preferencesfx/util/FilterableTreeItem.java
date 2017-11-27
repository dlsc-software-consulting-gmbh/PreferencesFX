package com.dlsc.preferencesfx.util;

import java.util.function.Predicate;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.TreeItem;

public class FilterableTreeItem<T> extends TreeItem<T> {
  private final ObservableList<TreeItem<T>> sourceChildren = FXCollections.observableArrayList();
  private final FilteredList<TreeItem<T>> filteredChildren = new FilteredList<>(sourceChildren);
  private final ObjectProperty<Predicate<T>> predicate = new SimpleObjectProperty<>();

  public FilterableTreeItem() {
    super();
    setupFilter();
  }

  public FilterableTreeItem(T item) {
    super(item);
    setupFilter();
  }

  private void setupFilter() {
    filteredChildren.predicateProperty().bind(Bindings.createObjectBinding(() -> {
      Predicate<TreeItem<T>> p = child -> {
        if (child instanceof FilterableTreeItem) {
          ((FilterableTreeItem<T>) child).predicateProperty().set(predicate.get());
        }
        if (predicate.get() == null || !child.getChildren().isEmpty()) {
          return true;
        }
        return predicate.get().test(child.getValue());
      };
      return p;
    } , predicate));

    filteredChildren.addListener((ListChangeListener<TreeItem<T>>) c -> {
      while (c.next()) {
        getChildren().removeAll(c.getRemoved());
        getChildren().addAll(c.getAddedSubList());
      }
    });
  }

  /**
   * Adds children to the tree.
   * Use this method instead of "getChildren()" and make sure
   * all items in the tree are "FilteredTreeItem" class objects.
   *
   * "getChildren()" from TreeItem cannot be overwritten properly, because of bug JDK-8089158
   * https://bugs.openjdk.java.net/browse/JDK-8089158
   * @return list of tree items
   */
  public ObservableList<TreeItem<T>> getSourceChildren() {
    return sourceChildren;
  }

  public ObjectProperty<Predicate<T>> predicateProperty() {
    return predicate;
  }

}
