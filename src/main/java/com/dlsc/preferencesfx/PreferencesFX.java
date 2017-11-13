package com.dlsc.preferencesfx;

import javafx.scene.control.SplitPane;
import javafx.scene.control.TreeItem;

import java.util.Arrays;
import java.util.List;

public class PreferencesFX extends SplitPane {

    private List<Category> categories;
    private CategoryTree categoryTree;

    PreferencesFX(Category[] categories) {
        this.categories = Arrays.asList(categories);
        setupParts();
        layoutParts();
        setupListeners();
    }

    private void setupParts() {
        categoryTree = new CategoryTree(categories);
    }

    private void layoutParts() {
        getItems().addAll(
                categoryTree,
                this.categories.get(0).getPage()
        );

        this.setDividerPositions(0.2f);
    }

    private void setupListeners() {
        categoryTree.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            double dividerPosition = this.getDividerPositions()[0]; // Save the old divider position. When you set the new item, it resets the position.
            getItems().remove(((Category) ((TreeItem) oldValue).getValue()).getPage()); // Removes the old page
            getItems().add(((Category) ((TreeItem) newValue).getValue()).getPage()); // Places the new page
            this.setDividerPositions(dividerPosition); // Sets the saved divider position
        });
    }

    public static PreferencesFX of(Category... categories) {
        return new PreferencesFX(categories);
    }

    public List<Category> getCategories() {
        return categories;
    }
}
