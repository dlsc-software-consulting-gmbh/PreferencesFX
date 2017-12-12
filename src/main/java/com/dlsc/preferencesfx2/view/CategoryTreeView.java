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
import javafx.scene.control.TreeView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.eclipse.fx.ui.controls.tree.FilterableTreeItem;
import org.eclipse.fx.ui.controls.tree.TreeItemPredicate;

public class CategoryTreeView extends VBox {
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
   * Decides whether a TreeItem should be shown in the CategoryTreeView or not.
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

  public CategoryTreeView(PreferencesModel preferencesModel) {
    this.preferencesModel = preferencesModel;
    categories = preferencesModel.getCategories();
    setupParts();
    layoutParts();
    setupBindings();
  }

  private void setupParts() {
    searchFld = new TextField();
    searchFld.setPromptText("Search..."); // TODO: make this i18n

    treeView = new TreeView();
  }

  private void layoutParts() {
    setVgrow(treeView, Priority.ALWAYS);
    getChildren().addAll(
        searchFld,
        treeView
    );
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
    // CategoryTreeView requires a RootItem, but in this case it's not desired to have it visible.
    setShowRoot(false);
    getRoot().setExpanded(true);
    // Set initial selected category.
    getSelectionModel().select(PreferencesFx.DEFAULT_CATEGORY);
  }

  private void setupBindings() {
    // Make CategoryTreeView filterable by implementing the necessary binding
    rootItem.predicateProperty().bind(Bindings.createObjectBinding(() -> {
      if (searchText.get() == null || searchText.get().isEmpty()) {
        return null;
      }
      return TreeItemPredicate.create(filterPredicate);
    }, searchText));

    searchTextProperty().bind(searchFld.textProperty());

  }

  public StringProperty searchTextProperty() {
    return searchText;
  }

}
