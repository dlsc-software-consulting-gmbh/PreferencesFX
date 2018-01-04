package com.dlsc.preferencesfx.view;

import com.dlsc.preferencesfx.history.view.HistoryButtonBox;
import com.dlsc.preferencesfx.model.PreferencesFxModel;
import javafx.geometry.Side;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.MasterDetailPane;

public class PreferencesFxView extends BorderPane implements View {
  private static final Logger LOGGER =
      LogManager.getLogger(PreferencesFxView.class.getName());

  CategoryController categoryController;
  MasterDetailPane preferencesPane;
  private PreferencesFxModel model;
  private NavigationView navigationView;
  private BreadCrumbView breadCrumbView;

  public PreferencesFxView(
      PreferencesFxModel model,
      NavigationView navigationView,
      BreadCrumbView breadCrumbView,
      CategoryController categoryController
  ) {
    this.model = model;
    this.navigationView = navigationView;
    this.breadCrumbView = breadCrumbView;
    this.categoryController = categoryController;
    init();
  }

  public PreferencesFxView(PreferencesFxModel model, CategoryController categoryController) {
    this.model = model;
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
    if (model.getCategories().size() > 1) {
      preferencesPane.setDetailSide(Side.LEFT);
      preferencesPane.setDetailNode(navigationView);
      preferencesPane.setMasterNode(
          new VBox(
              breadCrumbView,
              categoryController
          )
      );

      setCenter(preferencesPane);
    } else {
      setCenter(new StackPane(categoryController));
    }
    setBottom(new HistoryButtonBox(model.getHistory()));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void bindFieldsToModel() {

  }
}
