package com.dlsc.preferencesfx;

import javafx.scene.control.TextField;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * Created by François Martin on 27.11.17.
 */
public class CategoryTreeBox extends VBox {

  TextField searchFld;
  CategoryTree tree;

  CategoryTreeBox(CategoryTree tree) {
    this.tree = tree;
    setupParts();
    layoutParts();
    setupBindings();
  }

  private void setupParts() {
    searchFld = new TextField();
    searchFld.setPromptText("Search..."); // TODO: make this i18n
  }

  private void layoutParts() {
    setVgrow(tree, Priority.ALWAYS);
    getChildren().addAll(searchFld, tree);
  }

  private void setupBindings() {
    tree.searchTextProperty().bind(searchFld.textProperty());
  }

}
