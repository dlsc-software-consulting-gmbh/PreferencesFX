package com.dlsc.preferencesfx.view;

import com.dlsc.preferencesfx.model.PreferencesFxModel;
import javafx.geometry.Side;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
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
  VBox contentBox;
  private PreferencesFxModel model;
  private NavigationView navigationView;
  private BreadCrumbView breadCrumbView;

  public PreferencesFxView(
      PreferencesFxModel model,
      NavigationView navigationView,
      BreadCrumbView breadCrumbView,
      CategoryController categoryController
  ) {
    this(model, categoryController);
    this.navigationView = navigationView;
    this.breadCrumbView = breadCrumbView;
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
    preferencesPane = new MasterDetailPane();
    contentBox = new VBox();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void layoutParts() {
    // if there is more than 1 category, also add the breadCrumbBar
    if (breadCrumbView != null) {
      contentBox.getChildren().add(breadCrumbView);
    }
    // but always add the categoryController
    contentBox.getChildren().addAll(categoryController);
    VBox.setVgrow(categoryController, Priority.ALWAYS);

    if (model.getCategories().size() > 1) {
      preferencesPane.setDetailSide(Side.LEFT);
      preferencesPane.setDetailNode(navigationView);
      preferencesPane.setMasterNode(contentBox);
      setCenter(preferencesPane);
    } else {
      setCenter(new StackPane(categoryController));
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void bindFieldsToModel() {

  }
}
