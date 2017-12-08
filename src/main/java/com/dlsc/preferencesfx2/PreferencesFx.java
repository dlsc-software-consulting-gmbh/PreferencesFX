package com.dlsc.preferencesfx2;

import com.dlsc.preferencesfx.Category;
import com.dlsc.preferencesfx.PreferencesDialog;
import com.dlsc.preferencesfx2.model.PreferencesModel;
import com.dlsc.preferencesfx2.view.CategoryPresenter;
import com.dlsc.preferencesfx2.view.CategoryView;
import com.dlsc.preferencesfx2.view.PreferencePresenter;
import com.dlsc.preferencesfx2.view.PreferenceView;
import com.dlsc.preferencesfx2.view.TreePresenter;
import com.dlsc.preferencesfx2.view.TreeView;

public class PreferencesFx {

  private PreferencesModel preferencesModel;

  private TreeView treeView;
  private TreePresenter treePresenter;

  private CategoryView categoryView;
  private CategoryPresenter categoryPresenter;

  private PreferenceView preferenceView;
  private PreferencePresenter preferencePresenter;

  public PreferencesFx (Class<?> saveClass, Category[] categories) {
    preferencesModel = new PreferencesModel(saveClass, categories);

    treeView = new TreeView(preferencesModel);
    treePresenter = new TreePresenter(preferencesModel, treeView);

    categoryView = new CategoryView(preferencesModel);
    categoryPresenter = new CategoryPresenter(preferencesModel, categoryView);

    preferenceView = new PreferenceView(preferencesModel, treeView, categoryView);
    preferencePresenter = new PreferencePresenter(preferencesModel, preferenceView);
  }

  /**
   * Creates the Preferences window.
   *
   * @param saveClass  the class which the preferences are saved as
   *                   Must be unique to the application using the preferences
   * @param categories the items to be displayed in the TreeView
   * @return the preferences window
   */
  public static PreferencesFx of(Class<?> saveClass, Category... categories) {
    return new PreferencesFx(saveClass, categories);
  }

  /**
   * Shows the PreferencesFX dialog.
   */
  public void show() {
    new PreferencesDialog(preferenceView);
  }

  /**
   * Defines whether the state of the dialog window should be persisted or not.
   *
   * @param persist if true, the size, position and last selected item in the TreeView are
   *                being saved. When the dialog is showed again, it will be restored to
   *                the last saved state. Defaults to false.
   * @return this object for fluent API
   */
  public PreferencesFx persistWindowState(boolean persist) {
    preferencesModel.isPersistWindowState(persist);
    return this;
  }
}
