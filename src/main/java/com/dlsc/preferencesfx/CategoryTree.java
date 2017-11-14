package com.dlsc.preferencesfx;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.util.List;
import java.util.Objects;

public class CategoryTree extends TreeView {

  private List<Category> categories;
  private TreeItem<Category> rootItem;

  public CategoryTree(List<Category> categories) {
    this.categories = categories;
    setupParts();
    layoutParts();
  }

  private void setupParts() {
    rootItem = new TreeItem();
    addRecursive(rootItem, categories);
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
    setShowRoot(false); // TreeView requires a RootItem, but in this case it's not desired to have it visible.
    getRoot().setExpanded(true);
    getSelectionModel().select(PreferencesFX.INITIAL_CATEGORY); // Set initial selected category.
  }

}
