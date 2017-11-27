package com.dlsc.preferencesfx;

import java.util.Arrays;
import java.util.List;
import javafx.geometry.Side;
import javafx.scene.control.TreeItem;
import org.controlsfx.control.MasterDetailPane;

public class PreferencesFx extends MasterDetailPane {

  public static final float DIVIDER_POSITION = 0.2f;
  public static final int INITIAL_CATEGORY = 0;

  private List<Category> categories;
  private CategoryTree categoryTree;
  private CategoryTreeBox categoryTreeBox;

  PreferencesFx(Category[] categories) {
    this.categories = Arrays.asList(categories);
    setupParts();
    layoutParts();
    setupListeners();
  }

  public static PreferencesFx of(Category... categories) {
    return new PreferencesFx(categories);
  }

  private void setupParts() {
    categoryTree = new CategoryTree(categories);
    categoryTreeBox = new CategoryTreeBox(categoryTree);
  }

  private void layoutParts() {
    setDetailSide(Side.LEFT);
    setDetailNode(categoryTreeBox);
    // Sets initial shown CategoryPane.
    setMasterNode(this.categories.get(INITIAL_CATEGORY).getCategoryPane());
    setDividerPosition(DIVIDER_POSITION);
  }

  private void setupListeners() {
    categoryTree.getSelectionModel().selectedItemProperty().addListener(
        // Save the old divider position. When you set the new item, it resets the position.
        (observable, oldValue, newValue) -> {
          double dividerPosition = this.getDividerPosition();
          // Replaces the old CategoryPane with the new one.
          setMasterNode(((Category) ((TreeItem) newValue).getValue()).getCategoryPane());
          setDividerPosition(dividerPosition); // Sets the saved divider position.
        });
  }

  public List<Category> getCategories() {
    return categories;
  }
}
