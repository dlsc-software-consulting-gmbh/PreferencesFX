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

  public static final int DEFAULT_PREFERENCES_WIDTH = 1000;
  public static final int DEFAULT_PREFERENCES_HEIGHT = 700;

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

  public Preferences getPreferences() {
    return preferences;
  }

  public void save() {
//    byte[] data = SerializationUtils.serialize(this);
//    YourObject yourObject = SerializationUtils.deserialize(data);
  }
}
