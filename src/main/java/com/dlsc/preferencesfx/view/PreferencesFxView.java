package com.dlsc.preferencesfx.view;

import com.dlsc.preferencesfx.history.view.HistoryButtonBox;
import com.dlsc.preferencesfx.model.PreferencesFxModel;
import javafx.geometry.Side;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.MasterDetailPane;

/**
 * TODO: Add javadoc.
 */
public class PreferencesFxView extends BorderPane implements View {
  private static final Logger LOGGER =
      LogManager.getLogger(PreferencesFxView.class.getName());

  CategoryController categoryController;
  MasterDetailPane preferencesPane;
  VBox contentBox;
  private PreferencesFxModel model;
  private NavigationView navigationView;
  private BreadCrumbView breadCrumbView;

  /**
   * TODO: Add javadoc.
   * @param model TODO: Add javadoc.
   * @param navigationView TODO: Add javadoc.
   * @param breadCrumbView TODO: Add javadoc.
   * @param categoryController TODO: Add javadoc.
   */
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

  /**
   * TODO: Add javadoc.
   * @param model TODO: Add javadoc.
   * @param categoryController TODO: Add javadoc.
   */
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
    contentBox.getChildren().addAll(breadCrumbView, categoryController);
    VBox.setVgrow(categoryController, Priority.ALWAYS);

    if (model.getCategories().size() > 1) {
      preferencesPane.setDetailSide(Side.LEFT);
      preferencesPane.setDetailNode(navigationView);
      preferencesPane.setMasterNode(contentBox);
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
