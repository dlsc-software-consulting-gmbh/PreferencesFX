package com.dlsc.preferencesfx2.view;

import com.dlsc.preferencesfx.Category;
import com.dlsc.preferencesfx.Group;
import com.dlsc.preferencesfx.Setting;
import com.dlsc.preferencesfx.util.PreferencesFxUtils;
import com.dlsc.preferencesfx2.model.PreferencesModel;
import java.util.ArrayList;
import javafx.scene.control.TreeItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CategoryTreePresenter {
  private static final Logger LOGGER =
      LogManager.getLogger(CategoryTreePresenter.class.getName());

  private PreferencesModel preferencesModel;
  private CategoryTreeView categoryTreeView;
  public CategoryTreePresenter(PreferencesModel preferencesModel, CategoryTreeView categoryTreeView) {
    this.preferencesModel = preferencesModel;
    this.categoryTreeView = categoryTreeView;
  }

  private void setupListeners() {
    // Update category upon selection
    getSelectionModel().selectedItemProperty().addListener(
        (observable, oldValue, newValue) -> {
          if (newValue != null) {
            preferencesFx.showCategory((Category) ((TreeItem) newValue).getValue());
          }
        }
    );

    // Filter CategoryTreeView upon Search
    searchText.addListener((observable, oldText, newText) -> {
      if (newText.equals("")) { // empty search
        // unmark all categories
        unmarkEverything();
      } else {
        updateSearch(newText);
      }
    });
  }

  private void unmarkEverything() {
    categoryTreeItemMap.keySet().forEach(Category::unmarkAll);
  }

  public void updateSearch(String searchText) {
    updateFilteredLists(searchText);
    setSelectedCategoryByMatch();
    unmarkEverything();
    markMatches();
  }

  private void updateFilteredLists(String searchText) {
    filteredCategoriesLst =
        PreferencesFxUtils.filterCategoriesByDescription(categoriesLst, searchText);
    filteredSettingsLst =
        PreferencesFxUtils.filterSettingsByDescription(settingsLst, searchText);
    filteredGroupsLst =
        PreferencesFxUtils.filterGroupsByDescription(groupsLst, searchText);
    categoryMatches = filteredCategoriesLst.size();
    settingMatches = filteredSettingsLst.size();
    groupMatches = filteredGroupsLst.size();
    LOGGER.trace("Matched Categories: " + categoryMatches);
    LOGGER.trace("Matched Settings: " + settingMatches);
    LOGGER.trace("Matched Groups: " + groupMatches);
  }

  // asciidoctor Documentation - tag::compareMatches[]
  private void setSelectedCategoryByMatch() {
    // Strategy: Go from most specific match to most unspecific match
    Category firstFilteredSetting =
        filteredSettingsLst.size() == 0 ? null : settingCategoryMap.get(filteredSettingsLst.get(0));
    Category firstFilteredGroup =
        filteredGroupsLst.size() == 0 ? null : groupCategoryMap.get(filteredGroupsLst.get(0));
    Category firstFilteredCategory =
        filteredCategoriesLst.size() == 0 ? null : filteredCategoriesLst.get(0);
    setSelectedItem(
        PreferencesFxUtils.compareMatches(
            firstFilteredSetting, firstFilteredGroup, firstFilteredCategory,
            settingMatches, groupMatches, categoryMatches
        )
    );
  }
  // asciidoctor Documentation - end::compareMatches[]

  private void markMatches() {
    if (settingMatches >= 1) {
      filteredSettingsLst.forEach(Setting::mark);
    }
    if (groupMatches >= 1) {
      filteredGroupsLst.forEach(Group::mark);
    }
  }

  /**
   * Sets the selected item in the CategoryTreeView to the category of the given categoryId.
   *
   * @param categoryId the id of the category to be found
   */
  public void setSelectedCategoryById(int categoryId) {
    Category category = findCategoryById(categoryId);
    setSelectedItem(category);
  }

  /**
   * Finds the category with the matching id.
   *
   * @param categoryId the id of the category to be found
   * @return the category with categoryId or the first category in the CategoryTreeView if none is found
   */
  public Category findCategoryById(int categoryId) {
    Category selectedCategory = categoryTreeItemMap.keySet().stream().filter(
        category -> category.getId() == categoryId).findFirst()
        .orElse(rootItem.getChildren().get(0).getValue());
    return selectedCategory;
  }

  /**
   * Selects the given category in the CategoryTreeView.
   *
   * @param category the category to be selected
   */
  public void setSelectedItem(Category category) {
    if (category != null) {
      LOGGER.trace("Selected: " + category.toString());
      getSelectionModel().select(categoryTreeItemMap.get(category));
    }
  }

  public ArrayList<Category> getAllCategoriesFlatAsList() {
    return new ArrayList<>(categoryTreeItemMap.keySet());
  }

  /**
   * Retrieves the currently selected category in the CategoryTreeView.
   */
  public Category getSelectedCategory() {
    TreeItem<Category> selectedTreeItem =
        (TreeItem<Category>) getSelectionModel().getSelectedItem();
    if (selectedTreeItem != null) {
      return ((TreeItem<Category>) getSelectionModel().getSelectedItem()).getValue();
    }
    return null;
  }

  private void loadSettingValues() {
    createBreadcrumbs(categories);
    PreferencesFxUtils.categoriesToSettings(categoryTree.getAllCategoriesFlatAsList())
        .forEach(setting -> {
          LOGGER.trace("Loading: " + setting.getBreadcrumb());
          setting.loadSettingValue(preferencesModel.getStorageHandler());
          preferencesModel.getHistory().attachChangeListener(setting);
        });
  }

}
