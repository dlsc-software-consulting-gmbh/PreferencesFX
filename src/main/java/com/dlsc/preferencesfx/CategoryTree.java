package com.dlsc.preferencesfx;

import java.util.List;
import java.util.Objects;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public class CategoryTree extends TreeView {

  private List<Category> categories;
  private FilterableTreeItem<Category> rootItem;
  private StringProperty searchText = new SimpleStringProperty();

  public CategoryTree(List<Category> categories) {
    this.categories = categories;
    setupParts();
    layoutParts();
    rootItem.predicateProperty().setValue(category -> searchText.get().equals(category.getDescription()));
  }

  private void setupParts() {
    rootItem = new FilterableTreeItem<>();
    TreeItem<Category> treeItem = new TreeItem<>();
    rootItem.getSourceChildren().add(treeItem);
    addRecursive(treeItem, categories);
  }

  private void addRecursive(TreeItem treeItem, List<Category> categories) {
    for (Category category : categories) {
      TreeItem<Category> item = new TreeItem<>(category);
      // If there are subcategries, add them recursively.
      if (!Objects.equals(category.getChildren(), null)) {
        addRecursive(item, category.getChildren());
      }
      treeItem.getChildren().add(item);
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
