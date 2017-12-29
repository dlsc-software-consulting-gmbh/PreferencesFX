package com.dlsc.preferencesfx2.view;

import com.dlsc.preferencesfx2.model.Category;
import com.dlsc.preferencesfx2.model.PreferencesModel;

public class CategoryPresenter implements Presenter {
  private PreferencesModel model;
  private Category categoryModel;
  private CategoryView categoryView;

  public CategoryPresenter(PreferencesModel model, Category categoryModel, CategoryView categoryView) {
    this.model = model;
    this.categoryModel = categoryModel;
    this.categoryView = categoryView;
    init();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setupEventHandlers() {

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setupValueChangedListeners() {

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setupBindings() {

  }

}
