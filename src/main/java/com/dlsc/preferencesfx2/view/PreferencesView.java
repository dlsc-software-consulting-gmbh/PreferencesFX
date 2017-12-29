package com.dlsc.preferencesfx2.view;

import com.dlsc.preferencesfx.CategoryTree;
import com.dlsc.preferencesfx.history.HistoryButtonBox;
import com.dlsc.preferencesfx2.model.PreferencesModel;
import javafx.geometry.Side;
import javafx.scene.layout.BorderPane;
import org.controlsfx.control.MasterDetailPane;

public class PreferencesView extends BorderPane implements View {
  private PreferencesModel preferencesModel;
  private CategoryController categoryController;
  private NavigationView navigationView;
  MasterDetailPane preferencesPane;

  public PreferencesView(PreferencesModel preferencesModel, NavigationView navigationView, CategoryController categoryController) {
    this.preferencesModel = preferencesModel;
    this.navigationView = navigationView;
    this.categoryController = categoryController;
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
    preferencesPane = new MasterDetailPane();
    preferencesPane.setDetailSide(Side.LEFT);
    preferencesPane.setDetailNode(navigationView);
    preferencesPane.setMasterNode(categoryView);

    setCenter(preferencesPane);
    setBottom(new HistoryButtonBox(preferencesModel.getHistory()));
  }

  public CategoryTree getCategoryTree() {
    return null;
  }
}
