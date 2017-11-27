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
  public static final int INITIAL_CATEGORY = 0;

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
  private Preferences preferences;

  PreferencesFx(Class<?> aClass, Category[] categories) {
    preferences = Preferences.userNodeForPackage(aClass);
    this.categories = Arrays.asList(categories);
    setupParts();
    layoutParts();
    setupListeners();
  }

  /**
   * @param saveClass  the class which the preferences are saved as.
   *                   Must be unique to the application using the preferences.
   * @param categories the preferences categories
   * @return creates the Preferences window
   */
  public static PreferencesFx of(Class<?> saveClass, Category... categories) {
    return new PreferencesFx(saveClass, categories);
  }

  private void setupParts() {
    categoryTree = new CategoryTree(categories);
  }

  private void layoutParts() {
    setDetailSide(Side.LEFT);
    setDetailNode(categoryTree);

    // Sets initial shown CategoryPane.
    Category category = categories.stream().filter(e -> e.hashCode() == preferences.getInt(SELECTED_CATEGORY, 0)).findFirst().orElse(categories.get(INITIAL_CATEGORY));
    setMasterNode(category.getCategoryPane());
    setDividerPosition(preferences.getDouble(DIVIDER_POSITION, DEFAULT_DIVIDER_POSITION));
  }

  private void setupListeners() {
    /**
     * Whenever the divider position is changed, it's position is saved
     */
    dividerPositionProperty().addListener((observable, oldValue, newValue) ->
        preferences.putDouble(DIVIDER_POSITION, getDividerPosition())
    );

    categoryTree.getSelectionModel().selectedItemProperty().addListener(
        (observable, oldValue, newValue) -> {
          setMasterNode(((Category) ((TreeItem) newValue).getValue()).getCategoryPane());
          // Sets the saved divider position.
          setDividerPosition(preferences.getDouble(DIVIDER_POSITION, DEFAULT_DIVIDER_POSITION));
        }
    );
  }

  public List<Category> getCategories() {
    return categories;
  }

  public Preferences getPreferences() {
    return preferences;
  }

  public void save() {
//    preferences.putByteArray(SELECTED_CATEGORY, SerializationUtils.serialize((Serializable) categoryTree.getSelectionModel().getSelectedItem()));
  }

}
