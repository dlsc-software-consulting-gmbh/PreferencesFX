package com.dlsc.preferencesfx2.view;

import com.dlsc.preferencesfx.CategoryTree;
import com.dlsc.preferencesfx.history.HistoryButtonBox;
import com.dlsc.preferencesfx2.model.PreferencesModel;
import javafx.geometry.Side;
import javafx.scene.layout.BorderPane;
import org.controlsfx.control.MasterDetailPane;

public class PreferencesView extends BorderPane implements View {
  private PreferencesModel preferencesModel;
  private CategoryView categoryView;
  private NavigationView treeSearchView;
  MasterDetailPane masterDetailPane;

  public PreferencesView(PreferencesModel preferencesModel, NavigationView treeSearchView, CategoryView categoryView) {
    this.preferencesModel = preferencesModel;
    this.treeSearchView = treeSearchView;
    this.categoryView = categoryView;
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

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void layoutParts() {

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void bindFieldsToModel() {

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
