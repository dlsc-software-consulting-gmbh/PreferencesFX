package com.dlsc.preferencesfx2.view;

import static com.dlsc.preferencesfx.util.StringUtils.containsIgnoreCase;

import com.dlsc.preferencesfx.util.PreferencesFxUtils;
import com.dlsc.preferencesfx2.model.Category;
import com.dlsc.preferencesfx2.model.Group;
import com.dlsc.preferencesfx2.model.PreferencesModel;
import com.dlsc.preferencesfx2.model.Setting;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import javafx.beans.binding.Bindings;
import javafx.beans.property.StringProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.fx.ui.controls.tree.FilterableTreeItem;
import org.eclipse.fx.ui.controls.tree.TreeItemPredicate;

public class NavigationPresenter implements Presenter {
  private static final Logger LOGGER =
      LogManager.getLogger(NavigationPresenter.class.getName());

  private PreferencesModel preferencesModel;
  private NavigationView navigationView;


  private HashMap<Setting, Category> settingCategoryMap;
  private HashMap<Group, Category> groupCategoryMap;

  private List<Category> categoriesLst;
  private List<Setting> settingsLst;
  private List<Group> groupsLst;
  private List<Category> filteredCategoriesLst;
  private List<Setting> filteredSettingsLst;
  private List<Group> filteredGroupsLst;
  private int categoryMatches;
  private int settingMatches;
  private int groupMatches;

  /**
   * Decides whether a TreeItem should be shown in the TreeSearchView or not.
   * If result is true, it will be shown, if the result is false, it will be hidden.
   */
  private Predicate<Category> filterPredicate = category -> {
    // look in category description for matches
    boolean categoryMatch = containsIgnoreCase(category.getDescription(), navigationView.searchText.get());
    boolean settingMatch = false;
    boolean groupMatch = false;
    if (category.getGroups() != null) {
      // look in settings too
      settingMatch = category.getGroups().stream()
          .map(Group::getSettings)      // get settings from groups
          .flatMap(Collection::stream)  // flatten all lists of settings to settings
          .anyMatch(setting -> containsIgnoreCase(setting.getDescription(), navigationView.searchText.get()));
      // look in groups too
      groupMatch = category.getGroups().stream()
          .anyMatch(group -> containsIgnoreCase(group.getDescription(), navigationView.searchText.get()));
    }
    return categoryMatch || settingMatch || groupMatch;
  };

  public NavigationPresenter(PreferencesModel preferencesModel, NavigationView navigationView) {
    this.preferencesModel = preferencesModel;
    this.navigationView = navigationView;
    init();
    initializeTreeItems();
    initializeSearch();
  }

  private void initializeSearch(){
    categoriesLst = new ArrayList<>(navigationView.categoryTreeItemMap.keySet());
    settingCategoryMap = PreferencesFxUtils.mapSettingsToCategories(categoriesLst);
    groupCategoryMap = PreferencesFxUtils.mapGroupsToCategories(categoriesLst);
    settingsLst = PreferencesFxUtils.categoriesToSettings(categoriesLst);
    groupsLst = PreferencesFxUtils.categoriesToGroups(categoriesLst);
  }

  private void initializeTreeItems() {
    addRecursive(navigationView.rootItem, categories);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setupEventHandlers() {

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setupValueChangedListeners() {
    // Update displayed category upon selection
    navigationView.treeView.getSelectionModel().selectedItemProperty().addListener(
        (observable, oldTreeItem, newTreeItem) -> {
          if (newTreeItem != null) {
            preferencesModel.setDisplayedCategory(newTreeItem.getValue());
          }
        }
    );

    // Filter TreeSearchView upon Search
    navigationView.searchText.addListener((observable, oldText, newText) -> {
      if (newText.equals("")) { // empty search
        // unmark all categories
        unmarkEverything();
      } else {
        updateSearch(newText);
      }
    });
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setupBindings(){
    StringProperty searchText = preferencesModel.searchTextProperty();
    // Make TreeSearchView filterable by implementing the necessary binding
    navigationView.rootItem.predicateProperty().bind(Bindings.createObjectBinding(() -> {
      if (searchText.get() == null || searchText.get().isEmpty()) {
        return null;
      }
      return TreeItemPredicate.create(filterPredicate);
    }, searchText));
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

  private void unmarkEverything() {
    categoryTreeItemMap.keySet().forEach(Category::unmarkAll);
  }

  private void markMatches() {
    if (settingMatches >= 1) {
      filteredSettingsLst.forEach(Setting::mark);
    }
    if (groupMatches >= 1) {
      filteredGroupsLst.forEach(Group::mark);
    }
  }

  private void addRecursive(FilterableTreeItem treeItem, List<Category> categories) {
    for (Category category : categories) {
      FilterableTreeItem<Category> item = new FilterableTreeItem<>(category);
      // If there are subcategries, add them recursively.
      if (!Objects.equals(category.getChildren(), null)) {
        addRecursive(item, category.getChildren());
      }
      treeItem.getInternalChildren().add(item);
      categoryTreeItemMap.put(category, item);
    }
  }

}
