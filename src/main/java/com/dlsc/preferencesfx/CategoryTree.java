package com.dlsc.preferencesfx;

import static com.dlsc.preferencesfx.util.StringUtils.containsIgnoreCase;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TreeView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.fx.ui.controls.tree.FilterableTreeItem;
import org.eclipse.fx.ui.controls.tree.TreeItemPredicate;

public class CategoryTree extends TreeView {

  private static final Logger LOGGER =
      LogManager.getLogger(CategoryTree.class.getName());

  private List<Category> categories;
  private FilterableTreeItem<Category> rootItem;
  private StringProperty searchText = new SimpleStringProperty();
  private HashMap<Category, FilterableTreeItem<Category>> categoryTreeItemMap = new HashMap<>();
  private Predicate<Category> filterPredicate = category -> {
    // look in category description for matches
    boolean categoryMatch = containsIgnoreCase(category.getDescription(), searchText.get());
    boolean settingMatch = false;
    if (category.getGroups() != null) {
      // look in settings too
      settingMatch = category.getGroups().stream()
          .map(Group::getSettings)      // get settings from groups
          .flatMap(Collection::stream)  // flatten all lists of settings to settings
          .anyMatch(setting -> containsIgnoreCase(setting.getDescription(), searchText.get()));
    }
    if (settingMatch) {
      getSelectionModel().select(categoryTreeItemMap.get(category));
    }
    return categoryMatch || settingMatch;
  };

  public CategoryTree(List<Category> categories) {
    this.categories = categories;
    setupParts();
    layoutParts();
    setupBindings();
  }

  private void setupParts() {
    rootItem = new FilterableTreeItem<>(Category.of("Root"));
    addRecursive(rootItem, categories);
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
    getSelectionModel().select(PreferencesFx.INITIAL_CATEGORY); // Set initial selected category.
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

}
