package com.dlsc.preferencesfx;

import java.util.Arrays;
import java.util.List;
import java.util.prefs.Preferences;
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
  private Preferences preferences;

  private boolean persistWindowState = false;

  PreferencesFx(Class<?> saveClass, Category[] categories) {
    preferences = Preferences.userNodeForPackage(saveClass);
    this.categories = Arrays.asList(categories);
    setupParts();
    setupListeners();
    layoutParts();
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

  private void layoutParts() {
    setDetailSide(Side.LEFT);
    setDetailNode(categoryTreeBox);
    // Load last selected category in TreeView.
    categoryTree.setSelectedCategoryById(preferences.getInt(SELECTED_CATEGORY, DEFAULT_CATEGORY));
    TreeItem treeItem = (TreeItem) categoryTree.getSelectionModel().getSelectedItem();
    setSelectedCategory((Category) treeItem.getValue());
  }

  private void setupListeners() {
    // Whenever the divider position is changed, it's position is saved.
    dividerPositionProperty().addListener((observable, oldValue, newValue) ->
        preferences.putDouble(DIVIDER_POSITION, getDividerPosition())
    );

    categoryTree.getSelectionModel().selectedItemProperty().addListener(
        (observable, oldValue, newValue) -> {
          if (newValue != null) {
            setSelectedCategory((Category) ((TreeItem) newValue).getValue());
          }
        }
    );
  }

  /**
   * @param category sets the selected Category to the MasterNode
   */
  private void setSelectedCategory(Category category) {
    setMasterNode(category.getCategoryPane());
    // Sets the saved divider position.
    setDividerPosition(preferences.getDouble(DIVIDER_POSITION, DEFAULT_DIVIDER_POSITION));
  }

  public Preferences getPreferences() {
    return preferences;
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
    preferences.putInt(SELECTED_CATEGORY, category.getId());
  }

  /**
   * Opens the PreferencesFX dialog.
   */
  public void open() {
    new PreferencesDialog(this, persistWindowState);
  }

  /**
   * Defines whether the state of the dialog window should be persisted or not.
   * @param persist if true, the size, position and last selected item in the TreeView are
   *                being saved. When the dialog is opened again, it will be restored to
   *                the last saved state. Defaults to false.
   * @return this object for fluent API
   */
  public PreferencesFx persistWindowState(boolean persist){
    this.persistWindowState = persist;
    return this;
  }
}
