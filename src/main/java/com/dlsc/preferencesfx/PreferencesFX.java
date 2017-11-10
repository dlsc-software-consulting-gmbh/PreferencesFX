package com.dlsc.preferencesfx;

import java.util.Arrays;
import java.util.List;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.BorderPane;

public class PreferencesFX extends BorderPane {

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
    setLeft(categoryTree);
    setCenter(this.categories.get(0).getPage());
  }

  private void setupListeners() {
    categoryTree.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
      setCenter(((Category) ((TreeItem) newValue).getValue()).getPage());
    });
  }

  public static PreferencesFX of(Category... categories) {
    return new PreferencesFX(categories);
  }

  public List<Category> getCategories() {
    return categories;
  }
}
