package com.dlsc.preferencesfx;

import com.dlsc.preferencesfx.util.FilterableTreeItem;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TreeView;

public class CategoryTree extends TreeView {

  private List<Category> categories;
  private FilterableTreeItem<Category> rootItem;
  private StringProperty searchText = new SimpleStringProperty();
  Predicate<Category> filterPredicate =
      category -> category.getDescription().toLowerCase().contains(searchText.get().toLowerCase());

  public CategoryTree(List<Category> categories) {
    this.categories = categories;
    setupParts();
    layoutParts();

    rootItem.predicateProperty().bind(Bindings.createObjectBinding(() -> {
      if (searchText.get() == null || searchText.get().isEmpty()) {
        return null;
      }
      return filterPredicate;
    }, searchText));
  }

  private void setupParts() {
    rootItem = new FilterableTreeItem<>();
    addRecursive(rootItem, categories);
  }

  private void addRecursive(FilterableTreeItem treeItem, List<Category> categories) {
    for (Category category : categories) {
      FilterableTreeItem<Category> item = new FilterableTreeItem<>(category);
      // If there are subcategries, add them recursively.
      if (!Objects.equals(category.getChildren(), null)) {
        addRecursive(item, category.getChildren());
      }
      treeItem.getSourceChildren().add(item);
    }
  }

  private void layoutParts() {
    setRoot(rootItem);
    // TreeView requires a RootItem, but in this case it's not desired to have it visible.
    setShowRoot(false);
    getRoot().setExpanded(true);
    getSelectionModel().select(PreferencesFx.INITIAL_CATEGORY); // Set initial selected category.
  }

  public StringProperty searchTextProperty() {
    return searchText;
  }

}
