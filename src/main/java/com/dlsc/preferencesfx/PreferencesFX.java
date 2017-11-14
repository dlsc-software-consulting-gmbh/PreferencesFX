package com.dlsc.preferencesfx;

import java.util.Arrays;
import java.util.List;
import javafx.geometry.Side;
import javafx.scene.control.TreeItem;
import org.controlsfx.control.MasterDetailPane;

public class PreferencesFX extends MasterDetailPane {

  public static final float DIVIDER_POSITION = 0.2f;
  public static final int INITIAL_CATEGORY = 0;

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
    setDetailSide(Side.LEFT);
    setDetailNode(categoryTree);
    setMasterNode(this.categories.get(INITIAL_CATEGORY).getCategoryPane()); // Sets initial shown CategoryPane.
    setDividerPosition(DIVIDER_POSITION);
  }

  private void setupListeners() {
    categoryTree.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
      double dividerPosition = this.getDividerPosition(); // Save the old divider position. When you set the new item, it resets the position.
      setMasterNode(((Category) ((TreeItem) newValue).getValue()).getCategoryPane()); // Replaces the old CategoryPane with the new one.
      setDividerPosition(dividerPosition); // Sets the saved divider position.
    });
  }

  public static PreferencesFX of(Category... categories) {
    return new PreferencesFX(categories);
  }

  public List<Category> getCategories() {
    return categories;
  }
}
