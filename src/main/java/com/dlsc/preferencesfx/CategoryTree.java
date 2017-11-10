package com.dlsc.preferencesfx;

import java.util.List;
import java.util.Objects;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public class CategoryTree extends TreeView {

  private List<Category> categories;
  TreeItem rootItem;

  public CategoryTree(List<Category> categories) {
    this.categories = categories;
    setupParts();
    layoutParts();
  }

  private void setupParts() {
    rootItem = new TreeItem();
    addRecursive(categories);
  }

  private void addRecursive(List<Category> categories) {
    for (Category category : categories) {
      rootItem.getChildren().add(new TreeItem(category.getDescription()));
      if (!Objects.equals(category.getChildren(), null)) {
        addRecursive(category.getChildren());
      }
    }
  }

  private void layoutParts() {
    this.setRoot(rootItem);
    this.getRoot().setExpanded(true);
  }
}
