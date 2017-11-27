package com.dlsc.preferencesfx;

import java.util.Arrays;
import java.util.List;
import java.util.prefs.Preferences;
import javafx.geometry.Side;
import javafx.scene.control.TreeItem;
import org.controlsfx.control.MasterDetailPane;

public class PreferencesFx extends MasterDetailPane {

  public static final float DIVIDER_POSITION = 0.2f;
  public static final int INITIAL_CATEGORY = 0;

  private List<Category> categories;
  private CategoryTree categoryTree;
  private Preferences preferences;

  PreferencesFx(Category[] categories, Class<?> aClass) {
    this.categories = Arrays.asList(categories);
    setupParts();
    layoutParts();
    setupListeners();
    preferences = Preferences.userNodeForPackage(aClass);
  }

  /**
   * @param saveClass  the class which the preferences are saved as
   * @param categories the preferences categories
   * @return creates the Preferences window
   */
  public static PreferencesFx of(Class<?> saveClass, Category... categories) {
    return new PreferencesFx(categories, saveClass);
  }

  private void setupParts() {
    categoryTree = new CategoryTree(categories);
  }

  private void layoutParts() {
    setDetailSide(Side.LEFT);
    setDetailNode(categoryTree);
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
