package com.dlsc.preferencesfx;

import com.dlsc.preferencesfx.util.FilterableTreeItem;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TreeView;

public class CategoryTree extends TreeView {

  private List<Category> categories;
  private FilterableTreeItem<Category> rootItem;
  private StringProperty searchText = new SimpleStringProperty();
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
    return categoryMatch || settingMatch;
  };

  public CategoryTree(List<Category> categories) {
    this.categories = categories;
    setupParts();
    layoutParts();
    setupBindings();
  }

  private boolean containsIgnoreCase(String source, String match) {
    return source.toLowerCase().contains(match.toLowerCase());
  }

  private void setupParts() {
    rootItem = new FilterableTreeItem<>();
    addRecursive(rootItem, categories);
  }

  private void addRecursive(FilterableTreeItem treeItem, List<Category> categories) {
    for (Category category : categories) {
      FilterableTreeItem<Category> item = new FilterableTreeItem<>(category);
      // If there are subcategries, add them recursively.
      if (!Objects.equals(category.getChildren(), null)) {
        addRecursive(item, category.getChildren());
      }
      treeItem.getSourceChildren().add(item);
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
      return filterPredicate;
    }, searchText));
  }

  public StringProperty searchTextProperty() {
    return searchText;
  }

}
