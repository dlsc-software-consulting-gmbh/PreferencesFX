package com.dlsc.preferencesfx;

import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * Created by François Martin on 27.11.17.
 */
public class CategoryTreeBox extends VBox{

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
  }

  private void layoutParts() {
    getChildren().addAll(searchFld, tree);
  }

  private void setupBindings() {
    searchFld = new TextField();
  }

}
