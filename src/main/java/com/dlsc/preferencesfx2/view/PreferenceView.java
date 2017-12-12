package com.dlsc.preferencesfx2.view;

import com.dlsc.preferencesfx.CategoryTree;
import com.dlsc.preferencesfx.CategoryTreeBox;
import com.dlsc.preferencesfx2.model.PreferencesModel;
import javafx.geometry.Side;
import javafx.scene.layout.BorderPane;
import org.controlsfx.control.MasterDetailPane;

public class PreferenceView extends BorderPane {
  private PreferencesModel preferencesModel;
  private CategoryView categoryView;
  private CategoryTreeView categoryTreeView;
  private MasterDetailPane masterDetailPane;

  private CategoryTree categoryTree;
  private CategoryTreeBox categoryTreeBox;

  public PreferenceView(PreferencesModel preferencesModel, CategoryTreeView categoryTreeView, CategoryView categoryView) {
    this.preferencesModel = preferencesModel;
    this.categoryTreeView = categoryTreeView;
    this.categoryView = categoryView;
    layoutParts();
  }

  private void layoutParts() {
    masterDetailPane = new MasterDetailPane();
    masterDetailPane.setDetailSide(Side.LEFT);
    masterDetailPane.setDetailNode(categoryTreeView);
    masterDetailPane.setMasterNode(categoryView);
  }

  public CategoryTree getCategoryTree() {
    return null;
  }
}
