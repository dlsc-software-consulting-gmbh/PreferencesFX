package com.dlsc.preferencesfx2.view;

import static com.dlsc.preferencesfx.util.StringUtils.containsIgnoreCase;

import com.dlsc.preferencesfx.Category;
import com.dlsc.preferencesfx.Group;
import com.dlsc.preferencesfx.PreferencesFx;
import com.dlsc.preferencesfx.Setting;
import com.dlsc.preferencesfx.util.PreferencesFxUtils;
import com.dlsc.preferencesfx2.model.PreferencesModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.fx.ui.controls.tree.FilterableTreeItem;
import org.eclipse.fx.ui.controls.tree.TreeItemPredicate;

public class NavigationView extends VBox implements View {

  private static final Logger LOGGER =
      LogManager.getLogger(NavigationView.class.getName());
  private PreferencesModel preferencesModel;
  private TextField searchFld;
  private TreeView treeView;

  private List<Category> categoriesLst;
  private List<Setting> settingsLst;
  private List<Group> groupsLst;
  private List<Category> filteredCategoriesLst;
  private List<Setting> filteredSettingsLst;
  private List<Group> filteredGroupsLst;
  private int categoryMatches;
  private int settingMatches;
  private int groupMatches;

  private HashMap<Category, FilterableTreeItem<Category>> categoryTreeItemMap = new HashMap<>();
  private HashMap<Setting, Category> settingCategoryMap;
  private HashMap<Group, Category> groupCategoryMap;

  private List<Category> categories;
  private FilterableTreeItem<Category> rootItem;
  private StringProperty searchText = new SimpleStringProperty();

  /**
   * Decides whether a TreeItem should be shown in the TreeSearchView or not.
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

  public NavigationView(PreferencesModel preferencesModel) {
    this.preferencesModel = preferencesModel;
    treeView = new TreeView();
    init();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeSelf() {

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeParts() {

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void layoutParts() {

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void bindFieldsToModel() {

  }


  private void setupListeners() {
    // Update category upon selection
    treeView.getSelectionModel().selectedItemProperty().addListener(
        (observable, oldValue, newValue) -> {
          if (newValue != null) {
            preferencesModel.showCategory((Category) ((TreeItem) newValue).getValue());
          }
        }
    );

    // Filter TreeSearchView upon Search
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

  private void setupParts() {
    searchFld = new TextField();
    searchFld.setPromptText("Search..."); // TODO: make this i18n

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
    setVgrow(treeView, Priority.ALWAYS);
    getChildren().addAll(searchFld, treeView);

    treeView.setRoot(rootItem);
    // TreeSearchView requires a RootItem, but in this case it's not desired to have it visible.
    treeView.setShowRoot(false);
    treeView.getRoot().setExpanded(true);
    // Set initial selected category.
    treeView.getSelectionModel().select(PreferencesFx.DEFAULT_CATEGORY);
  }

  private void setupBindings() {
    searchTextProperty().bind(searchFld.textProperty());

    // Make TreeSearchView filterable by implementing the necessary binding
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
   * Sets the selected item in the TreeSearchView to the category of the given categoryId.
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
   * @return the category with categoryId or the first category in the TreeSearchView if none is found
   */
  public Category findCategoryById(int categoryId) {
    Category selectedCategory = categoryTreeItemMap.keySet().stream().filter(
        category -> category.getId() == categoryId).findFirst()
        .orElse(rootItem.getChildren().get(0).getValue());
    return selectedCategory;
  }

  /**
   * Selects the given category in the TreeSearchView.
   *
   * @param category the category to be selected
   */
  public void setSelectedItem(Category category) {
    if (category != null) {
      LOGGER.trace("Selected: " + category.toString());
      treeView.getSelectionModel().select(categoryTreeItemMap.get(category));
    }
  }

  public ArrayList<Category> getAllCategoriesFlatAsList() {
    return new ArrayList<>(categoryTreeItemMap.keySet());
  }

  /**
   * Retrieves the currently selected category in the TreeSearchView.
   */
  public Category getSelectedCategory() {
    TreeItem<Category> selectedTreeItem =
        (TreeItem<Category>) treeView.getSelectionModel().getSelectedItem();
    if (selectedTreeItem != null) {
      return ((TreeItem<Category>) treeView.getSelectionModel().getSelectedItem()).getValue();
    }
    return null;
  }

  public TreeView getCategoryTreeView() {
    return treeView;
  }
}
