package com.dlsc.preferencesfx2.view;

import com.dlsc.preferencesfx2.history.view.HistoryButtonBox;
import com.dlsc.preferencesfx2.model.PreferencesModel;
import javafx.geometry.Side;
import javafx.scene.layout.BorderPane;
import org.controlsfx.control.MasterDetailPane;

public class PreferencesView extends BorderPane implements View {
  CategoryController categoryController;
  MasterDetailPane preferencesPane;
  private PreferencesModel model;
  private NavigationView navigationView;

  public PreferencesView(PreferencesModel model, NavigationView navigationView, CategoryController categoryController) {
    this.model = model;
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
    setBottom(new HistoryButtonBox(model.getHistory()));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void bindFieldsToModel() {

  }
}
