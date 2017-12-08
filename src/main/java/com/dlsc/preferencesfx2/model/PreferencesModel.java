package com.dlsc.preferencesfx2.model;

import com.dlsc.preferencesfx.Category;
import com.dlsc.preferencesfx.util.StorageHandler;
import java.util.Arrays;
import java.util.List;

public class PreferencesModel {

  private List<Category> categories;
  private StorageHandler storageHandler;
  private Category displayedCategory;
  private boolean persistWindowState = false;

  public PreferencesModel(Class<?> saveClass, Category[] categories) {
    storageHandler = new StorageHandler(saveClass);
    this.categories = Arrays.asList(categories);
//    setupParts();
//    loadSettingValues();
//    setupListeners();
//    layoutParts();
  }

  public List<Category> getCategories() {
    return categories;
  }

  public void isPersistWindowState(boolean persist) {
    persistWindowState = persist;
  }
}
