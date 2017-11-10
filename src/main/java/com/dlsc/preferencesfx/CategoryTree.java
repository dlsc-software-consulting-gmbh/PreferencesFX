package com.dlsc.preferencesfx;

import java.util.List;
import java.util.Objects;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public class CategoryTree extends TreeView {

  private List<Category> categories;
  TreeItem<Category> rootItem;

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
      item.setExpanded(true);
      if (!Objects.equals(category.getChildren(), null)) {
        addRecursive(item, category.getChildren());
      }
      treeItem.getChildren().add(item);
    }
  }

  private void layoutParts() {
    this.setRoot(rootItem);
    this.setShowRoot(false);
    this.getRoot().setExpanded(true);
  }

}
