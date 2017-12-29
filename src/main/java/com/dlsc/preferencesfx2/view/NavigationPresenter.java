package com.dlsc.preferencesfx2.view;

import static com.dlsc.preferencesfx2.util.StringUtils.containsIgnoreCase;

import com.dlsc.preferencesfx2.model.Category;
import com.dlsc.preferencesfx2.model.Group;
import com.dlsc.preferencesfx2.model.PreferencesModel;
import com.dlsc.preferencesfx2.model.Setting;
import com.dlsc.preferencesfx2.util.PreferencesFxUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import javafx.beans.binding.Bindings;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TreeItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.fx.ui.controls.tree.FilterableTreeItem;
import org.eclipse.fx.ui.controls.tree.TreeItemPredicate;

public class NavigationPresenter implements Presenter {
  private static final Logger LOGGER =
      LogManager.getLogger(NavigationPresenter.class.getName());

  private PreferencesModel model;
  private NavigationView navigationView;

  // Search related
  private List<Category> flatCategoriesLst;
  private List<Setting> flatSettingsLst;
  private List<Group> flatGroupsLst;
  private List<Category> filteredCategoriesLst;
  private List<Setting> filteredSettingsLst;
  private List<Group> filteredGroupsLst;
  private int categoryMatches;
  private int settingMatches;
  private int groupMatches;
  private HashMap<Category, FilterableTreeItem<Category>> categoryTreeItemMap = new HashMap<>();
  private HashMap<Group, Category> groupCategoryMap;
  private HashMap<Setting, Category> settingCategoryMap;

  /**
   * Decides whether a TreeItem should be shown in the TreeSearchView or not.
   * If result is true, it will be shown, if the result is false, it will be hidden.
   */
  private Predicate<Category> filterPredicate = category -> {
    // look in category description for matches
    String searchText = model.getSearchText();
    boolean categoryMatch = containsIgnoreCase(category.getDescription(), searchText);
    boolean settingMatch = false;
    boolean groupMatch = false;
    if (category.getGroups() != null) {
      // look in settings too
      settingMatch = category.getGroups().stream()
          .map(Group::getSettings)      // get settings from groups
          .flatMap(Collection::stream)  // flatten all lists of settings to settings
          .anyMatch(setting -> containsIgnoreCase(setting.getDescription(), searchText));
      // look in groups too
      groupMatch = category.getGroups().stream()
          .anyMatch(group -> containsIgnoreCase(group.getDescription(), searchText));
    }
    return categoryMatch || settingMatch || groupMatch;
  };

  public NavigationPresenter(PreferencesModel model, NavigationView navigationView) {
    this.model = model;
    this.navigationView = navigationView;
    init();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeViewParts() {
    initializeTreeItems();
    setSelectedCategory(model.getDisplayedCategory());
    initializeSearch();
  }

  private void initializeSearch() {
    flatCategoriesLst = new ArrayList<>(categoryTreeItemMap.keySet());
    settingCategoryMap = PreferencesFxUtils.mapSettingsToCategories(flatCategoriesLst);
    groupCategoryMap = PreferencesFxUtils.mapGroupsToCategories(flatCategoriesLst);
    flatSettingsLst = PreferencesFxUtils.categoriesToSettings(flatCategoriesLst);
    flatGroupsLst = PreferencesFxUtils.categoriesToGroups(flatCategoriesLst);
  }

  private void initializeTreeItems() {
    addRecursive(navigationView.rootItem, model.getCategories());
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
            model.setDisplayedCategory(newTreeItem.getValue());
          }
        }
    );

    // Filter TreeSearchView upon Search
    model.searchTextProperty().addListener((observable, oldText, newText) -> {
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
  public void setupBindings() {
    StringProperty searchText = model.searchTextProperty();
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
        PreferencesFxUtils.filterCategoriesByDescription(flatCategoriesLst, searchText);
    filteredSettingsLst =
        PreferencesFxUtils.filterSettingsByDescription(flatSettingsLst, searchText);
    filteredGroupsLst =
        PreferencesFxUtils.filterGroupsByDescription(flatGroupsLst, searchText);
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
    setSelectedCategory(
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

  /**
   * Selects the given category in the NavigationView.
   *
   * @param category to be selected
   */
  public void setSelectedCategory(Category category) {
    navigationView.setSelectedItem(categoryTreeItemMap.get(category));
  }

  /**
   * Retrieves the currently selected category in the TreeSearchView.
   */
  public Category getSelectedCategory() {
    TreeItem<Category> selectedTreeItem =
        navigationView.treeView.getSelectionModel().getSelectedItem();
    if (selectedTreeItem != null) {
      return navigationView.treeView.getSelectionModel().getSelectedItem().getValue();
    }
    return null;
  }

}
