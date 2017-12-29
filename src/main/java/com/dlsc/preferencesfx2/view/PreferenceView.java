package com.dlsc.preferencesfx2.view;

import com.dlsc.preferencesfx.CategoryTree;
import com.dlsc.preferencesfx.history.HistoryButtonBox;
import com.dlsc.preferencesfx2.model.PreferencesModel;
import javafx.geometry.Side;
import javafx.scene.layout.BorderPane;
import org.controlsfx.control.MasterDetailPane;

public class PreferenceView extends BorderPane {
  private PreferencesModel preferencesModel;
  private CategoryView categoryView;
  private TreeSearchView treeSearchView;
  MasterDetailPane masterDetailPane;

  public PreferenceView(PreferencesModel preferencesModel, TreeSearchView treeSearchView, CategoryView categoryView) {
    this.preferencesModel = preferencesModel;
    this.treeSearchView = treeSearchView;
    this.categoryView = categoryView;
    layoutParts();
  }

  private void layoutParts() {
    masterDetailPane = new MasterDetailPane();
    masterDetailPane.setDetailSide(Side.LEFT);
    masterDetailPane.setDetailNode(treeSearchView);
    masterDetailPane.setMasterNode(categoryView);

    setCenter(masterDetailPane);
    setBottom(new HistoryButtonBox(preferencesModel.getHistory()));
  }

  public CategoryTree getCategoryTree() {
    return null;
  }
}
