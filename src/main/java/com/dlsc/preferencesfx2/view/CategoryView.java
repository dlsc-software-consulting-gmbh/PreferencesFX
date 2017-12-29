package com.dlsc.preferencesfx2.view;

import com.dlsc.preferencesfx2.model.Category;
import com.dlsc.preferencesfx2.model.PreferencesModel;
import javafx.scene.layout.StackPane;

public class CategoryView extends StackPane implements View {
  private PreferencesModel model;
  private Category categoryModel;

  /**
   * Initializes a new view of a Category.
   *
   * @param model
   * @param categoryModel    is the category that will be displayed in this view
   */
  public CategoryView(PreferencesModel model, Category categoryModel) {
    this.model = model;
    this.categoryModel = categoryModel;
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
}
