package com.dlsc.preferencesfx.view;

import static javafx.beans.binding.Bindings.createObjectBinding;

import java.lang.reflect.Field;
import java.util.function.Predicate;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.TreeItem;

/**
 * A {@link TreeItem} with filterable leafs.
 *
 * <p>The tree allows to set a {@link Predicate} to filter its leafs.
 * Non leaf nodes are not filtered. If all children of a non leaf node
 * are filtered the node becomes a leaf node and the predicate is applied to it.
 *
 * <p>This class is inspired by the efxclipse project
 * <a href="https://www.eclipse.org/efxclipse">https://www.eclipse.org/efxclipse</a>
 *
 * @param <T> The type of the {@link #getValue() value} property within TreeItem.
 *
 * @author Stephan Classen
 */
public class FilterableTreeItem<T> extends TreeItem<T> {

  private final ObservableList<FilterableTreeItem<T>> internalChildren;
  private final TreeItemPredicate<T> treeItemPredicate = new TreeItemPredicate<>();

  /**
   * Creates a new {@link FilterableTreeItem}.
   *
   * @param value the value of the {@link FilterableTreeItem}
   */
  FilterableTreeItem(T value) {
    super(value);
    internalChildren = FXCollections.observableArrayList();

    final FilteredList<FilterableTreeItem<T>> filteredList = new FilteredList<>(internalChildren);
    filteredList.predicateProperty().bind(
        createObjectBinding(() -> treeItemPredicate, predicateProperty())
    );

    // set the children in the super class
    setChildren(FXCollections.unmodifiableObservableList(filteredList));
    filteredList.addListener(getChildrenListener());
  }

  /**
   * Add a child to this TreeItem.
   *
   * @param newItem the item to add to the children.
   */
  void add(FilterableTreeItem<T> newItem) {
    internalChildren.add(newItem);
  }

  /**
   * Returns the predicate property.
   *
   * @return the predicate property
   */
  ObjectProperty<Predicate<T>> predicateProperty() {
    return treeItemPredicate.predicateProperty;
  }

  private void setChildren(ObservableList<FilterableTreeItem<T>> children) {
    try {
      final Field field = TreeItem.class.getDeclaredField("children");
      if (!field.isAccessible()) {
        field.setAccessible(true);
      }
      field.set(this, children);
    } catch (NoSuchFieldException | IllegalAccessException | RuntimeException e) {
      throw new RuntimeException("Could not set field: TreeItem.children", e);
    }
  }

  @SuppressWarnings("unchecked")
  private ListChangeListener<? super TreeItem<T>> getChildrenListener() {
    try {
      final Field field = TreeItem.class.getDeclaredField("childrenListener");
      if (!field.isAccessible()) {
        field.setAccessible(true);
      }
      final Object value = field.get(this);
      return (ListChangeListener<? super TreeItem<T>>) value;
    } catch (NoSuchFieldException | IllegalAccessException | RuntimeException e) {
      throw new RuntimeException("Could not get field: TreeItem.childrenListener", e);
    }
  }

  /**
   * Wrapper around a predicate to apply it to the value of a {@link FilterableTreeItem}.
   *
   * @param <T> The type of the value property within the TreeItem.
   */
  private static class TreeItemPredicate<T> implements Predicate<FilterableTreeItem<T>> {

    private final ObjectProperty<Predicate<T>> predicateProperty = new SimpleObjectProperty<>();

    /**
     * Tests if a TreeItem is visible.
     *
     * @param child the TreeItem to test
     * @return {@code true} if the TreeItem is visible, {@code false} otherwise
     */
    @Override
    public boolean test(FilterableTreeItem<T> child) {
      final Predicate<T> predicate = predicateProperty.get();

      // Update predicate on child - this will trigger filtering of the child
      final ObjectProperty<Predicate<T>> childPredicate = child.predicateProperty();
      childPredicate.set(newPredicate(predicate, childPredicate.get()));

      // no predicate -> do not filter
      if (predicate == null) {
        return true;
      }

      // has children -> do not filter
      if (!child.getChildren().isEmpty()) {
        return true;
      }

      return predicate.test(child.getValue());
    }

    /**
     * Returns a predicate which is different from oldPredicate.
     * This is required to trigger filtering on the children.
     *
     * @param predicate the new predicate
     * @param oldPredicate the old predicate
     * @return a predicate which is different from oldPredicate
     */
    @SuppressWarnings("FunctionalExpressionCanBeFolded")
    private Predicate<T> newPredicate(Predicate<T> predicate, Predicate<T> oldPredicate) {
      if (predicate == null) {
        return null;
      }
      return oldPredicate == predicate ? predicate::test : predicate;
    }
  }
}
