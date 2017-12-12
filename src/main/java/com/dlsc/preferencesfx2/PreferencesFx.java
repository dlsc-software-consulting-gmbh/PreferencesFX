package com.dlsc.preferencesfx2;

import com.dlsc.preferencesfx.Category;
import com.dlsc.preferencesfx2.model.PreferencesModel;
import com.dlsc.preferencesfx2.view.CategoryPresenter;
import com.dlsc.preferencesfx2.view.CategoryTreePresenter;
import com.dlsc.preferencesfx2.view.CategoryTreeView;
import com.dlsc.preferencesfx2.view.CategoryView;
import com.dlsc.preferencesfx2.view.PreferenceDialog;
import com.dlsc.preferencesfx2.view.PreferencePresenter;
import com.dlsc.preferencesfx2.view.PreferenceView;

public class PreferencesFx {

  private PreferencesModel preferencesModel;

  private CategoryTreeView categoryTreeView;
  private CategoryTreePresenter categoryTreePresenter;

  private CategoryView categoryView;
  private CategoryPresenter categoryPresenter;

  private PreferenceView preferenceView;
  private PreferencePresenter preferencePresenter;

  public PreferencesFx(Class<?> saveClass, Category[] categories) {
    preferencesModel = new PreferencesModel(saveClass, categories);

    categoryTreeView = new CategoryTreeView(preferencesModel);
    categoryTreePresenter = new CategoryTreePresenter(preferencesModel, categoryTreeView);

    categoryView = new CategoryView(preferencesModel);
    categoryPresenter = new CategoryPresenter(preferencesModel, categoryView);

    preferenceView = new PreferenceView(preferencesModel, categoryTreeView, categoryView);
    preferencePresenter = new PreferencePresenter(preferencesModel, preferenceView);
  }

  /**
   * Creates the Preferences window.
   *
   * @param saveClass  the class which the preferences are saved as
   *                   Must be unique to the application using the preferences
   * @param categories the items to be displayed in the CategoryTreeView
   * @return the preferences window
   */
  public static PreferencesFx of(Class<?> saveClass, Category... categories) {
    return new PreferencesFx(saveClass, categories);
  }

  /**
   * Shows the PreferencesFX dialog.
   */
  public void show() {
    new PreferenceDialog(preferencesModel, preferenceView);
  }

  /**
   * Defines whether the state of the dialog window should be persisted or not.
   *
   * @param persist if true, the size, position and last selected item in the CategoryTreeView are
   *                being saved. When the dialog is showed again, it will be restored to
   *                the last saved state. Defaults to false.
   * @return this object for fluent API
   */
  public PreferencesFx persistWindowState(boolean persist) {
    preferencesModel.setPersistWindowState(persist);
    return this;
  }
}
