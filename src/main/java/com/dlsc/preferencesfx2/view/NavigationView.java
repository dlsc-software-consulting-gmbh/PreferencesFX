package com.dlsc.preferencesfx2.view;

import com.dlsc.preferencesfx2.Constants;
import com.dlsc.preferencesfx2.model.Category;
import com.dlsc.preferencesfx2.model.PreferencesModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
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
  TextField searchFld;
  TreeView<Category> treeView;

  HashMap<Category, FilterableTreeItem<Category>> categoryTreeItemMap = new HashMap<>();

  private List<Category> categories;
  FilterableTreeItem<Category> rootItem;

  public NavigationView(PreferencesModel preferencesModel) {
    this.preferencesModel = preferencesModel;
    treeView = new TreeView<>();
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
    searchFld = new TextField();
    searchFld.setPromptText("Search..."); // TODO: make this i18n
    rootItem = new FilterableTreeItem<>(Category.of(""));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void layoutParts() {
    setVgrow(treeView, Priority.ALWAYS);
    getChildren().addAll(searchFld, treeView);

    treeView.setRoot(rootItem);
    // TreeSearchView requires a RootItem, but in this case it's not desired to have it visible.
    treeView.setShowRoot(false);
    treeView.getRoot().setExpanded(true);
    // Set initial selected category.
    treeView.getSelectionModel().select(Constants.DEFAULT_CATEGORY);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void bindFieldsToModel() {
    searchTextProperty().bind(searchFld.textProperty());

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
