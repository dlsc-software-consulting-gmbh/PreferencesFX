package com.dlsc.preferencesfx.view;

import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.preferencesfx.formsfx.view.renderer.PreferencesFxFormRenderer;
import com.dlsc.preferencesfx.model.Category;
import com.dlsc.preferencesfx.model.PreferencesFxModel;
import javafx.scene.layout.StackPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * TODO: Add javadoc.
 */
public class CategoryView extends StackPane implements View {
  private static final Logger LOGGER =
      LogManager.getLogger(CategoryView.class.getName());

  Form form;
  private PreferencesFxModel model;
  private Category categoryModel;
  private PreferencesFxFormRenderer preferencesFormRenderer;

  /**
   * Initializes a new view of a Category.
   *
   * @param model the preferencesFx model
   * @param categoryModel is the category that will be displayed in this view
   */
  public CategoryView(PreferencesFxModel model, Category categoryModel) {
    this.model = model;
    this.categoryModel = categoryModel;
    init();
  }

  /**
   * TODO: Add javadoc.
   */
  void initializeFormRenderer() {
    getChildren().clear();
    preferencesFormRenderer = new PreferencesFxFormRenderer(form);
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
