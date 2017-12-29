package com.dlsc.preferencesfx2.view;

import com.dlsc.preferencesfx2.model.Category;
import com.dlsc.preferencesfx2.model.PreferencesModel;
import javafx.scene.layout.StackPane;

public class CategoryView extends StackPane implements View {
  private PreferencesModel preferencesModel;
  private Category categoryModel;

  /**
   * Initializes a new view of a Category.
   * @param preferencesModel
   * @param categoryModel is the category that will be displayed in this view
   */
  public CategoryView(PreferencesModel preferencesModel, Category categoryModel) {
    this.preferencesModel = preferencesModel;
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
    getChildren().add(preferencesModel.getCategories().get(0).getCategoryPane());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void bindFieldsToModel() {

  }
}
