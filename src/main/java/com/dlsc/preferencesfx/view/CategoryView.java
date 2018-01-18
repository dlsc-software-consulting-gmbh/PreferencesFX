package com.dlsc.preferencesfx.view;

import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.preferencesfx.formsfx.view.renderer.PreferencesFxFormRenderer;
import com.dlsc.preferencesfx.model.Category;
import com.dlsc.preferencesfx.model.Group;
import com.dlsc.preferencesfx.model.PreferencesFxModel;
import com.dlsc.preferencesfx.model.Setting;
import javafx.scene.layout.StackPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Displays a {@link Category} as a form with all of its {@link Group} and {@link Setting}.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class CategoryView extends StackPane implements View {
  private static final Logger LOGGER =
      LogManager.getLogger(CategoryView.class.getName());

  private PreferencesFxModel model;
  private Category categoryModel;
  private PreferencesFxFormRenderer preferencesFormRenderer;

  /**
   * Initializes a new view of a {@link Category}.
   *
   * @param model         the model of PreferencesFX
   * @param categoryModel is the category that will be displayed in this view
   */
  public CategoryView(PreferencesFxModel model, Category categoryModel) {
    this.model = model;
    this.categoryModel = categoryModel;
    init();
  }

  /**
   * Initializes the {@link PreferencesFxFormRenderer}.
   *
   * @param form to be rendered using the {@link PreferencesFxFormRenderer}
   * @implNote This can't be done in the constructor, since the form has to be initialized by the
   * corresponding presenter first.
   */
  void initializeFormRenderer(Form form) {
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
