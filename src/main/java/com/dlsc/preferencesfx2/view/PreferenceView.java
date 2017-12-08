package com.dlsc.preferencesfx2.view;

import com.dlsc.preferencesfx.CategoryTree;
import com.dlsc.preferencesfx.CategoryTreeBox;
import com.dlsc.preferencesfx2.model.PreferencesModel;
import javafx.geometry.Side;
import org.controlsfx.control.MasterDetailPane;

public class PreferenceView extends MasterDetailPane {
  private PreferencesModel preferencesModel;
  private CategoryView categoryView;
  private TreeView treeView;

  private CategoryTree categoryTree;
  private CategoryTreeBox categoryTreeBox;

  public PreferenceView(PreferencesModel preferencesModel, TreeView treeView, CategoryView categoryView) {
    this.preferencesModel = preferencesModel;
    this.treeView = treeView;
    this.categoryView = categoryView;
    layoutParts();
  }

  private void layoutParts() {
    setDetailSide(Side.LEFT);
    setDetailNode(treeView);
    setMasterNode(categoryView);
  }
}
