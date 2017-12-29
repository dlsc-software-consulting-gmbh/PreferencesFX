package com.dlsc.preferencesfx2.view;

import com.dlsc.preferencesfx2.model.Category;
import com.dlsc.preferencesfx2.model.PreferencesModel;

public class CategoryPresenter implements Presenter {
  private PreferencesModel preferencesModel;
  private Category categoryModel;
  private CategoryView categoryView;

  public CategoryPresenter(PreferencesModel preferencesModel, Category categoryModel, CategoryView categoryView) {
    this.preferencesModel = preferencesModel;
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
