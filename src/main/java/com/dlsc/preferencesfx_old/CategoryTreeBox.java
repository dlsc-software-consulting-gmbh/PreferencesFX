package com.dlsc.preferencesfx_old;

import javafx.scene.control.TextField;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by François Martin on 27.11.17.
 */
public class CategoryTreeBox extends VBox {

  private static final Logger LOGGER =
      LogManager.getLogger(CategoryTreeBox.class.getName());

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
