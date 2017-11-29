package com.dlsc.preferencesfx;

import com.dlsc.preferencesfx.util.StorageHandler;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javafx.geometry.Side;
import javafx.scene.control.TreeItem;
import org.controlsfx.control.MasterDetailPane;

public class PreferencesFx extends MasterDetailPane {
  public static final String SELECTED_CATEGORY = "SELECTED_CATEGORY";

  public static final String DIVIDER_POSITION = "DIVIDER_POSITION";
  public static final double DEFAULT_DIVIDER_POSITION = 0.2;
  public static final int DEFAULT_CATEGORY = 0;

  public static final int DEFAULT_PREFERENCES_WIDTH = 1000;
  public static final int DEFAULT_PREFERENCES_HEIGHT = 700;
  public static final int DEFAULT_PREFERENCES_POS_X = 700;
  public static final int DEFAULT_PREFERENCES_POS_Y = 500;

  public static final String WINDOW_WIDTH = "WINDOW_WIDTH";
  public static final String WINDOW_HEIGHT = "WINDOW_HEIGHT";
  public static final String WINDOW_POS_X = "WINDOW_POS_X";
  public static final String WINDOW_POS_Y = "WINDOW_POS_Y";

  private List<Category> categories;
  private CategoryTree categoryTree;
  private CategoryTreeBox categoryTreeBox;
  private StorageHandler storageHandler;

  PreferencesFx(Class<?> saveClass, Category[] categories) {
    storageHandler = new StorageHandler(saveClass);
    this.categories = Arrays.asList(categories);
    updateSettingsFromPreferences();
    setupParts();
    setupListeners();
    layoutParts();
  }

  private void updateSettingsFromPreferences() {
    categoryTree
        .getAllCategoriesFlatAsList()
        .stream()
        .map(Category::getGroups)     // get groups from categories
        .filter(Objects::nonNull)     // remove all null
        .flatMap(Collection::stream)
        .map(Group::getSettings)      // get settings from groups
        .filter(Objects::nonNull)     // remove all null
        .flatMap(Collection::stream)
        .collect(Collectors.toList())
        .forEach(setting -> setting.updateFromPreferences(storageHandler));
  }

  /**
   * Creates the Preferences window.
   *
   * @param saveClass  the class which the preferences are saved as
   *                   Must be unique to the application using the preferences
   * @param categories the items to be displayed in the TreeView
   * @return the preferences window
   */
  public static PreferencesFx of(Class<?> saveClass, Category... categories) {
    return new PreferencesFx(saveClass, categories);
  }

  private void setupParts() {
    categoryTree = new CategoryTree(categories);
    categoryTreeBox = new CategoryTreeBox(categoryTree);
  }

  private void setupListeners() {
    // Whenever the divider position is changed, it's position is saved.
    dividerPositionProperty().addListener((observable, oldValue, newValue) ->
        storageHandler.putDividerPosition(getDividerPosition())
    );

    categoryTree.getSelectionModel().selectedItemProperty().addListener(
        (observable, oldValue, newValue) -> {
          if (newValue != null) {
            setSelectedCategory((Category) ((TreeItem) newValue).getValue());
          }
        }
    );
  }

  private void layoutParts() {
    setDetailSide(Side.LEFT);
    setDetailNode(categoryTreeBox);
    // Load last selected category in TreeView.
    categoryTree.setSelectedCategoryById(storageHandler.getSelectedCategory());
    TreeItem treeItem = (TreeItem) categoryTree.getSelectionModel().getSelectedItem();
    setSelectedCategory((Category) treeItem.getValue());
  }

  /**
   * @param category sets the selected Category to the MasterNode
   */
  private void setSelectedCategory(Category category) {
    setMasterNode(category.getCategoryPane());
    // Sets the saved divider position.
    setDividerPosition(storageHandler.getDividerPosition());
  }

  /**
   * Saves the current selected Category.
   */
  public void saveSelectedCategory() {
    TreeItem treeItem = (TreeItem) categoryTree.getSelectionModel().getSelectedItem();
    Category category;
    if (treeItem != null) {
      category = (Category) treeItem.getValue();
    } else {
      category = categoryTree.findCategoryById(DEFAULT_CATEGORY);
    }
    storageHandler.putSelectedCategory(category.getId());
  }

  public StorageHandler getStorageHandler() {
    return storageHandler;
  }
}