package com.dlsc.preferencesfx;

import static com.dlsc.preferencesfx.util.StringUtils.containsIgnoreCase;

import com.dlsc.preferencesfx.util.PreferencesFxUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.fx.ui.controls.tree.FilterableTreeItem;
import org.eclipse.fx.ui.controls.tree.TreeItemPredicate;

public class CategoryTree extends TreeView {

  private static final Logger LOGGER =
      LogManager.getLogger(CategoryTree.class.getName());

  private PreferencesFx preferencesFx;

  private List<Category> categoriesLst;
  private List<Setting> settingsLst;
  private List<Group> groupsLst;
  private List<Category> filteredCategoriesLst;
  private List<Setting> filteredSettingsLst;
  private List<Group> filteredGroupsLst;
  private int categoryMatches;
  private int settingsMatches;
  private int groupsMatches;

  private HashMap<Category, FilterableTreeItem<Category>> categoryTreeItemMap = new HashMap<>();
  private HashMap<Setting, Category> settingCategoryMap;
  private HashMap<Group, Category> groupCategoryMap;

  private List<Category> categories;
  private FilterableTreeItem<Category> rootItem;
  private StringProperty searchText = new SimpleStringProperty();

  /**
   * Decides whether a TreeItem should be shown in the TreeView or not.
   * If result is true, it will be shown, if the result is false, it will be hidden.
   */
  private Predicate<Category> filterPredicate = category -> {
    // look in category description for matches
    boolean categoryMatch = containsIgnoreCase(category.getDescription(), searchText.get());
    boolean settingMatch = false;
    boolean groupMatch = false;
    if (category.getGroups() != null) {
      // look in settings too
      settingMatch = category.getGroups().stream()
          .map(Group::getSettings)      // get settings from groups
          .flatMap(Collection::stream)  // flatten all lists of settings to settings
          .anyMatch(setting -> containsIgnoreCase(setting.getDescription(), searchText.get()));
      // look in groups too
      groupMatch = category.getGroups().stream()
          .anyMatch(group -> containsIgnoreCase(group.getDescription(), searchText.get()));
    }
    return categoryMatch || settingMatch || groupMatch;
  };

  public CategoryTree(PreferencesFx preferencesFx, List<Category> categories) {
    this.categories = categories;
    this.preferencesFx = preferencesFx;
    setupParts();
    layoutParts();
    setupBindings();
    setupListeners();
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

    // Filter TreeView upon Search
    searchText.addListener((observable, oldText, newText) -> {
      if (newText.equals("")) { // empty search
        // unmark all categories
        categoryTreeItemMap.keySet().forEach(Category::unmarkAll);
      } else {
        updateSearch(newText);
      }
    });
  }

  public void updateSearch(String searchText) {
    updateFilteredLists(searchText);
    selectCategoryByMatch();
    preferencesFx.removeMarksFromDisplayedCategory();
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
    settingsMatches = filteredSettingsLst.size();
    groupsMatches = filteredGroupsLst.size();
    LOGGER.trace("Matched Categories: " + categoryMatches);
    LOGGER.trace("Matched Settings: " + settingsMatches);
    LOGGER.trace("Matched Groups: " + groupsMatches);
  }

  private void selectCategoryByMatch() {
    // if there is one category left, select it
    if (categoryMatches == 1) {
      setSelectedItem(filteredCategoriesLst.get(0));
    }
    if (groupsMatches == 1) {
      setSelectedItem(groupCategoryMap.get(filteredGroupsLst.get(0)));
    }
    if (settingsMatches == 1) {
      setSelectedItem(settingCategoryMap.get(filteredSettingsLst.get(0)));
    }
  }

  private void markMatches() {
    if (settingsMatches >= 1) {
      filteredSettingsLst.forEach(Setting::mark);
    }
    if (groupsMatches >= 1) {
      filteredGroupsLst.forEach(Group::mark);
    }
  }

  private void setupParts() {
    rootItem = new FilterableTreeItem<>(Category.of("Root"));
    addRecursive(rootItem, categories);
    categoriesLst = new ArrayList<>(categoryTreeItemMap.keySet());
    settingCategoryMap = PreferencesFxUtils.mapSettingsToCategories(categoriesLst);
    groupCategoryMap = PreferencesFxUtils.mapGroupsToCategories(categoriesLst);
    settingsLst = PreferencesFxUtils.categoriesToSettings(categoriesLst);
    groupsLst = PreferencesFxUtils.categoriesToGroups(categoriesLst);
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

  private void layoutParts() {
    setRoot(rootItem);
    // TreeView requires a RootItem, but in this case it's not desired to have it visible.
    setShowRoot(false);
    getRoot().setExpanded(true);
    // Set initial selected category.
    getSelectionModel().select(PreferencesFx.DEFAULT_CATEGORY);
  }

  private void setupBindings() {
    // Make TreeView filterable by implementing the necessary binding
    rootItem.predicateProperty().bind(Bindings.createObjectBinding(() -> {
      if (searchText.get() == null || searchText.get().isEmpty()) {
        return null;
      }
      return TreeItemPredicate.create(filterPredicate);
    }, searchText));
  }

  public StringProperty searchTextProperty() {
    return searchText;
  }

  /**
   * Sets the selected item in the TreeView to the category of the given categoryId.
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
   * @return the category with categoryId or the first category in the TreeView if none is found
   */
  public Category findCategoryById(int categoryId) {
    Category selectedCategory = categoryTreeItemMap.keySet().stream().filter(
        category -> category.getId() == categoryId).findFirst()
        .orElse(rootItem.getChildren().get(0).getValue());
    return selectedCategory;
  }

  /**
   * Selects the given category in the TreeView.
   *
   * @param category the category to be selected
   */
  public void setSelectedItem(Category category) {
    getSelectionModel().select(categoryTreeItemMap.get(category));
  }

  /**
   * Retrieves the currently selected category in the TreeView.
   */
  public Category getSelectedCategory() {
    TreeItem<Category> selectedTreeItem =
        (TreeItem<Category>) getSelectionModel().getSelectedItem();
    if (selectedTreeItem != null) {
      return ((TreeItem<Category>) getSelectionModel().getSelectedItem()).getValue();
    }
    return null;
  }
}
