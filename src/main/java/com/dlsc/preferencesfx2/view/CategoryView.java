package com.dlsc.preferencesfx2.view;

import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.preferencesfx2.formsfx.view.renderer.PreferencesFormRenderer;
import com.dlsc.preferencesfx2.model.Category;
import com.dlsc.preferencesfx2.model.PreferencesModel;
import javafx.scene.layout.StackPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CategoryView extends StackPane implements View {
  private static final Logger LOGGER =
      LogManager.getLogger(CategoryView.class.getName());

  Form form;
  private PreferencesModel model;
  private Category categoryModel;
  private PreferencesFormRenderer preferencesFormRenderer;

  /**
   * Initializes a new view of a Category.
   *
   * @param model
   * @param categoryModel is the category that will be displayed in this view
   */
  public CategoryView(PreferencesModel model, Category categoryModel) {
    this.model = model;
    this.categoryModel = categoryModel;
    init();
  }

  void initializeFormRenderer() {
    getChildren().clear();
    preferencesFormRenderer = new PreferencesFormRenderer(form);
    getChildren().add(preferencesFormRenderer);
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
    form = Form.of();
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
}
