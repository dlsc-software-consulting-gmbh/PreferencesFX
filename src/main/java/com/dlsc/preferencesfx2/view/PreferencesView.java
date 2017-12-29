package com.dlsc.preferencesfx2.view;

import com.dlsc.preferencesfx.history.HistoryButtonBox;
import com.dlsc.preferencesfx2.model.PreferencesModel;
import javafx.geometry.Side;
import javafx.scene.layout.BorderPane;
import org.controlsfx.control.MasterDetailPane;

public class PreferencesView extends BorderPane implements View {
  private PreferencesModel preferencesModel;
  private NavigationView navigationView;
  CategoryController categoryController;
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
    preferencesPane = new MasterDetailPane();
    preferencesPane.setDetailSide(Side.LEFT);
    preferencesPane.setDetailNode(navigationView);
    preferencesPane.setMasterNode(categoryController);




    setCenter(preferencesPane);
    setBottom(new HistoryButtonBox(preferencesModel.getHistory()));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void bindFieldsToModel() {
    categoryController.addListener(preferencesModel.displayedCategoryProperty());
  }
}
