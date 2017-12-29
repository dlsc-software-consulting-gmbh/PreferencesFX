package com.dlsc.preferencesfx2.view;

import com.dlsc.preferencesfx2.model.PreferencesModel;

public class CategoryPresenter implements Presenter {
  private PreferencesModel preferencesModel;
  private CategoryView categoryView;

  public CategoryPresenter(PreferencesModel preferencesModel, CategoryView categoryView) {
      this.preferencesModel = preferencesModel;
      this.categoryView = categoryView;
    }
}
