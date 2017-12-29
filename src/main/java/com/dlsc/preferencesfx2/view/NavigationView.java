package com.dlsc.preferencesfx2.view;

import com.dlsc.preferencesfx2.model.Category;
import com.dlsc.preferencesfx2.model.PreferencesModel;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.fx.ui.controls.tree.FilterableTreeItem;

public class NavigationView extends VBox implements View {

  private static final Logger LOGGER =
      LogManager.getLogger(NavigationView.class.getName());
  private PreferencesModel model;
  TextField searchFld;
  TreeView<Category> treeView;

  FilterableTreeItem<Category> rootItem;

  public NavigationView(PreferencesModel model) {
    this.model = model;
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
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void bindFieldsToModel() {
    model.searchTextProperty().bind(searchFld.textProperty());
  }

  /**
   * Selects the given category TreeItem in the NavigationView.
   *
   * @param categoryTreeItem the category TreeItem to be selected
   */
  protected void setSelectedItem(FilterableTreeItem categoryTreeItem) {
    if (categoryTreeItem != null) {
      treeView.getSelectionModel().select(categoryTreeItem);
    }
  }
}
