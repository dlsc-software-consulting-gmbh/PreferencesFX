package com.dlsc.preferencesfx2.model;

import static com.dlsc.preferencesfx2.Constants.DEFAULT_CATEGORY;

import com.dlsc.preferencesfx2.Constants;
import com.dlsc.preferencesfx2.history.History;
import com.dlsc.preferencesfx2.util.PreferencesFxUtils;
import com.dlsc.preferencesfx2.util.StorageHandler;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TreeItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PreferencesModel {
  private static final Logger LOGGER =
      LogManager.getLogger(PreferencesModel.class.getName());

  private ObjectProperty<Category> displayedCategory = new SimpleObjectProperty<>();

  private StringProperty searchText = new SimpleStringProperty();

  private List<Category> categories;
  private StorageHandler storageHandler;
  private History history;

  private boolean persistWindowState = false;
  private boolean historyDebugState = false;

  public PreferencesModel(StorageHandler storageHandler, History history, Category[] categories) {
    this.storageHandler = storageHandler;
    this.history = history;
    this.categories = Arrays.asList(categories);
    initializeDisplayedCategory();
    loadSettingValues();
  }

  private void initializeDisplayedCategory() {
    displayedCategory.setValue(categories.get(Constants.DEFAULT_CATEGORY_INDEX));
  }

  private void loadSettingValues() {
    createBreadcrumbs(categories);
    PreferencesFxUtils.categoriesToSettings(categoryTree.getAllCategoriesFlatAsList())
        .forEach(setting -> {
          LOGGER.trace("Loading: " + setting.getBreadcrumb());
          setting.loadSettingValue(storageHandler);
          history.attachChangeListener(setting);
        });
  }

  private void createBreadcrumbs(List<Category> categories) {
    categories.forEach(category -> {
      if (!Objects.equals(category.getGroups(), null)) {
        category.getGroups().forEach(group -> group.addToBreadcrumb(category.getBreadcrumb()));
      }
      if (!Objects.equals(category.getChildren(), null)) {
        category.createBreadcrumbs(category.getChildren());
      }
    });
  }


  public List<Category> getCategories() {
    return categories;
  }

  public boolean isPersistWindowState() {
    return persistWindowState;
  }

  public void setPersistWindowState(boolean persistWindowState) {
    this.persistWindowState = persistWindowState;
  }

  public History getHistory() {
    return history;
  }

  public StorageHandler getStorageHandler() {
    return storageHandler;
  }

  public void setHistoryDebugState(boolean historyDebugState) {
    this.historyDebugState = historyDebugState;
  }

  public boolean getHistoryDebugState() {
    return historyDebugState;
  }


  // ------ StorageHandler work -------------

  /**
   * Saves the current selected Category.
   */
  public void saveSelectedCategory(CategoryTree categoryTree) {
    TreeItem treeItem = (TreeItem) categoryTree.getSelectionModel().getSelectedItem();
    Category category;
    if (treeItem != null) {
      category = (Category) treeItem.getValue();
    } else {
      category = categories.get(DEFAULT_CATEGORY);
    }
    storageHandler.saveSelectedCategory(category.getId());
  }

  public int loadSelectedCategory() {
    return storageHandler.loadSelectedCategory();
  }

  public void saveDividerPosition(double dividerPosition) {
    storageHandler.saveDividerPosition(dividerPosition);
  }

  public double loadDividerPosition() {
    return storageHandler.loadDividerPosition();
  }

  public Category getDisplayedCategory() {
    return displayedCategory.get();
  }

  public ReadOnlyObjectProperty<Category> displayedCategoryProperty() {
    return displayedCategory;
  }

  public void setDisplayedCategory(Category displayedCategory) {
    this.displayedCategory.set(displayedCategory);
  }

  public String getSearchText() {
    return searchText.get();
  }

  public StringProperty searchTextProperty() {
    return searchText;
  }

  public void setSearchText(String searchText) {
    this.searchText.set(searchText);
  }
}
