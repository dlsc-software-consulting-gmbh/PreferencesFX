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
        this.getSelectionModel().select(0);
    }

}
