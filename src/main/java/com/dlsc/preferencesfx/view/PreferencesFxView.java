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

/**
 * Represents the master view, which is used to show all view parts in {@link PreferencesFxDialog}.
 *
 * @author François Martin
 * @author Marco Sanfratello
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
   *
   * @param model the model of PreferencesFX
   * @param navigationView     TODO: Add javadoc.
   * @param breadCrumbView     TODO: Add javadoc.
   * @param categoryController TODO: Add javadoc.
   */
  public PreferencesFxView(
      PreferencesFxModel model,
      NavigationView navigationView,
      BreadCrumbView breadCrumbView,
      CategoryController categoryController
  ) {
    this.breadCrumbView = breadCrumbView;
    this.navigationView = navigationView;
    this.model = model;
    this.categoryController = categoryController;
    init();
  }

  /**
   * TODO: Add javadoc.
   *
   * @param model the model of PreferencesFX
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
    // but always add the categoryController
    contentBox.getChildren().addAll(breadCrumbView, categoryController);
    VBox.setVgrow(categoryController, Priority.ALWAYS);

    if (!model.isOneCategoryLayout()) {
      preferencesPane.setDetailSide(Side.LEFT);
      preferencesPane.setDetailNode(navigationView);
      preferencesPane.setMasterNode(contentBox);
      setCenter(preferencesPane);
    } else {
      setCenter(new StackPane(contentBox));
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void bindFieldsToModel() {

  }
}
