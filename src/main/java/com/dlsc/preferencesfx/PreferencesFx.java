package com.dlsc.preferencesfx;

import com.dlsc.formsfx.model.util.TranslationService;
import com.dlsc.preferencesfx.history.History;
import com.dlsc.preferencesfx.model.Category;
import com.dlsc.preferencesfx.model.PreferencesFxModel;
import com.dlsc.preferencesfx.util.SearchHandler;
import com.dlsc.preferencesfx.util.StorageHandler;
import com.dlsc.preferencesfx.view.BreadCrumbPresenter;
import com.dlsc.preferencesfx.view.BreadCrumbView;
import com.dlsc.preferencesfx.view.CategoryController;
import com.dlsc.preferencesfx.view.CategoryPresenter;
import com.dlsc.preferencesfx.view.CategoryView;
import com.dlsc.preferencesfx.view.NavigationPresenter;
import com.dlsc.preferencesfx.view.NavigationView;
import com.dlsc.preferencesfx.view.PreferencesFxDialog;
import com.dlsc.preferencesfx.view.PreferencesFxPresenter;
import com.dlsc.preferencesfx.view.PreferencesFxView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PreferencesFx {
  private static final Logger LOGGER =
      LogManager.getLogger(PreferencesFx.class.getName());

  private PreferencesFxModel preferencesFxModel;

  private NavigationView navigationView;
  private NavigationPresenter navigationPresenter;

  private BreadCrumbView breadCrumbView;
  private BreadCrumbPresenter breadCrumbPresenter;

  private CategoryController categoryController;

  private PreferencesFxView preferencesFxView;
  private PreferencesFxPresenter preferencesFxPresenter;

  private PreferencesFx(Class<?> saveClass, Category... categories) {
    preferencesFxModel = new PreferencesFxModel(new StorageHandler(saveClass), new SearchHandler(), new History(), categories);

    categoryController = new CategoryController();
    initializeCategoryViews();
    categoryController.setView(preferencesFxModel.getDisplayedCategory()); // display initial category

    navigationView = new NavigationView(preferencesFxModel);
    navigationPresenter = new NavigationPresenter(preferencesFxModel, navigationView);

    breadCrumbView = new BreadCrumbView(preferencesFxModel);
    breadCrumbPresenter = new BreadCrumbPresenter(preferencesFxModel, breadCrumbView);

    preferencesFxView = new PreferencesFxView(preferencesFxModel, navigationView, breadCrumbView, categoryController);
    preferencesFxPresenter = new PreferencesFxPresenter(preferencesFxModel, preferencesFxView);
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
    preferencesFxModel.getFlatCategoriesLst().forEach(category -> {
      CategoryView categoryView = new CategoryView(preferencesFxModel, category);
      CategoryPresenter categoryPresenter = new CategoryPresenter(preferencesFxModel, category, categoryView);
      categoryController.addView(category, categoryView, categoryPresenter);
    });
  }

  /**
   * Shows the PreferencesFX dialog.
   */
  public void show() {
    new PreferencesFxDialog(preferencesFxModel, preferencesFxView);
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
    preferencesFxModel.setPersistWindowState(persist);
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
    preferencesFxModel.setHistoryDebugState(debugState);
    return this;
  }

  public PreferencesFx buttonsVisibility(boolean isVisible) {
    preferencesFxModel.setButtonsVisible(isVisible);
    return this;
  }

  /**
   * Sets the translation service property of the preferences dialog.
   *
   * @param newValue
   *              The new value for the translation service property.
   *
   * @return PreferencesFx to allow for chaining.
   */
  public PreferencesFx i18n(TranslationService newValue) {
    preferencesFxModel.setTranslationService(newValue);
    return this;
  }
}
