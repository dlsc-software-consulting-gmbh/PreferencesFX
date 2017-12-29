package com.dlsc.preferencesfx2;

import com.dlsc.preferencesfx2.history.History;
import com.dlsc.preferencesfx2.model.Category;
import com.dlsc.preferencesfx2.model.PreferencesModel;
import com.dlsc.preferencesfx2.util.StorageHandler;
import com.dlsc.preferencesfx2.view.CategoryController;
import com.dlsc.preferencesfx2.view.CategoryPresenter;
import com.dlsc.preferencesfx2.view.CategoryView;
import com.dlsc.preferencesfx2.view.NavigationPresenter;
import com.dlsc.preferencesfx2.view.NavigationView;
import com.dlsc.preferencesfx2.view.PreferencesDialog;
import com.dlsc.preferencesfx2.view.PreferencesPresenter;
import com.dlsc.preferencesfx2.view.PreferencesView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PreferencesFx {
  private static final Logger LOGGER =
      LogManager.getLogger(PreferencesFx.class.getName());

  private PreferencesModel preferencesModel;

  private NavigationView navigationView;
  private NavigationPresenter navigationPresenter;

  private CategoryController categoryController;

  private PreferencesView preferenceView;
  private PreferencesPresenter preferencePresenter;

  private PreferencesFx(Class<?> saveClass, Category... categories) {
    preferencesModel = new PreferencesModel(new StorageHandler(saveClass), new History(), categories);

    categoryController = new CategoryController();
    initializeCategoryViews();
    categoryController.setView(preferencesModel.getDisplayedCategory()); // display initial category

    navigationView = new NavigationView(preferencesModel);
    navigationPresenter = new NavigationPresenter(preferencesModel, navigationView);

    preferenceView = new PreferencesView(preferencesModel, navigationView, categoryController);
    preferencePresenter = new PreferencesPresenter(preferencesModel, preferenceView);
  }

  /**
   * Creates the Preferences window.
   *
   * @param saveClass  the class which the preferences are saved as
   *                   Must be unique to the application using the preferences
   * @param categories the items to be displayed in the TreeSearchView
   * @return the preferences window
   */
  public static PreferencesFx of(Class<?> saveClass, Category... categories) {
    return new PreferencesFx(saveClass, categories);
  }

  /**
   * Prepares the CategoryController by creating CategoryView / CategoryPresenter pairs from
   * all Categories and loading them into the CategoryController.
   */
  private void initializeCategoryViews() {
    preferencesModel.getFlatCategoriesLst().forEach(category -> {
      CategoryView categoryView = new CategoryView(preferencesModel, category);
      CategoryPresenter categoryPresenter = new CategoryPresenter(preferencesModel, category, categoryView);
      categoryController.addView(category, categoryView, categoryPresenter);
    });
  }

  /**
   * Shows the PreferencesFX dialog.
   */
  public void show() {
    new PreferencesDialog(preferencesModel, preferenceView);
  }

  /**
   * Defines whether the state of the dialog window should be persisted or not.
   *
   * @param persist if true, the size, position and last selected item in the TreeSearchView are
   *                being saved. When the dialog is showed again, it will be restored to
   *                the last saved state. Defaults to false.
   * @return this object for fluent API
   */
  public PreferencesFx persistWindowState(boolean persist) {
    preferencesModel.setPersistWindowState(persist);
    return this;
  }

  /**
   * Defines whether the table to debug the undo / redo history should be shown in a dialog
   * when pressing a key combination or not.
   * <p>
   * Pressing Ctrl + Shift + H (Windows) or CMD + Shift + H (Mac) opens a dialog with the
   * undo / redo history, shown in a table.
   *
   * @param debugState if true, pressing the key combination will open the dialog
   * @return this object for fluent API
   */
  public PreferencesFx debugHistoryMode(boolean debugState) {
    preferencesModel.setHistoryDebugState(debugState);
    return this;
  }
}
