package com.dlsc.preferencesfx;

import com.dlsc.formsfx.model.util.TranslationService;
import com.dlsc.preferencesfx.history.History;
import com.dlsc.preferencesfx.model.Category;
import com.dlsc.preferencesfx.model.PreferencesFxModel;
import com.dlsc.preferencesfx.util.StorageHandler;
import com.dlsc.preferencesfx.util.SearchHandler;
import com.dlsc.preferencesfx.util.StorageHandlerImpl;
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
import com.dlsc.preferencesfx.view.UndoRedoBox;
import javafx.event.EventHandler;
import javafx.event.EventType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Represents the main PreferencesFX class.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class PreferencesFx {
  private static final Logger LOGGER =
      LogManager.getLogger(PreferencesFx.class.getName());

  private PreferencesFxModel preferencesFxModel;

  private NavigationView navigationView;
  private NavigationPresenter navigationPresenter;

  private UndoRedoBox undoRedoBox;

  private BreadCrumbView breadCrumbView;
  private BreadCrumbPresenter breadCrumbPresenter;

  private CategoryController categoryController;

  private PreferencesFxView preferencesFxView;
  private PreferencesFxPresenter preferencesFxPresenter;

  private PreferencesFx(Class<?> saveClass, Category... categories) {
    // asciidoctor Documentation - tag::testMock[]
    preferencesFxModel = new PreferencesFxModel(
        new StorageHandlerImpl(saveClass), new SearchHandler(), new History(), categories
    );
    init();
    // asciidoctor Documentation - end::testMock[]
  }

  private PreferencesFx(StorageHandler storageHandler, Category... categories) {
    // asciidoctor Documentation - tag::testMock[]
    preferencesFxModel = new PreferencesFxModel(
            storageHandler, new SearchHandler(), new History(), categories
    );
    // asciidoctor Documentation - end::testMock[]
    init();
  }

  private void init() {
    // setting values are only loaded if they are present already
    preferencesFxModel.loadSettingValues();

    undoRedoBox = new UndoRedoBox(preferencesFxModel.getHistory());

    breadCrumbView = new BreadCrumbView(preferencesFxModel, undoRedoBox);
    breadCrumbPresenter = new BreadCrumbPresenter(preferencesFxModel, breadCrumbView);

    categoryController = new CategoryController();
    initializeCategoryViews();

    // display initial category
    categoryController.setView(preferencesFxModel.getDisplayedCategory());

    navigationView = new NavigationView(preferencesFxModel);
    navigationPresenter = new NavigationPresenter(preferencesFxModel, navigationView);

    preferencesFxView = new PreferencesFxView(
            preferencesFxModel, navigationView, breadCrumbView, categoryController
    );
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
   * Creates the Preferences window.
   *
   * @param customStorageHandler Custom implementation of the {@link StorageHandler}
   * @param categories the items to be displayed in the TreeSearchView
   * @return the preferences window
   */
  public static PreferencesFx of(StorageHandler customStorageHandler, Category... categories) {
    return new PreferencesFx(customStorageHandler, categories);
  }

  /**
   * Prepares the CategoryController by creating CategoryView / CategoryPresenter pairs from
   * all Categories and loading them into the CategoryController.
   */
  private void initializeCategoryViews() {
    preferencesFxModel.getFlatCategoriesLst().forEach(category -> {
      CategoryView categoryView = new CategoryView(preferencesFxModel, category);
      CategoryPresenter categoryPresenter = new CategoryPresenter(
          preferencesFxModel, category, categoryView, breadCrumbPresenter
      );
      categoryController.addView(category, categoryView, categoryPresenter);
    });
  }

  /**
   * Shows the PreferencesFX dialog.
   */
  public void show() {
    // by default, modal is false for retro-compatibility
    show(false);
  }

  /**
   * Show the PreferencesFX dialog.
   * @param modal window or not modal, that's the question.
   */
  public void show(boolean modal) {
    new PreferencesFxDialog(preferencesFxModel, preferencesFxView).show(modal);
  }

  /**
   * Defines if the PreferencesAPI should save the applications states.
   * This includes the persistence of the dialog window, as well as each settings values.
   *
   * @param enable if true, the storing of the window state of the dialog window
   *               and the settings values are enabled.
   * @return this object for fluent API
   */
  public PreferencesFx persistApplicationState(boolean enable) {
    persistWindowState(enable);
    saveSettings(enable);
    return this;
  }

  /**
   * Defines whether the state of the dialog window should be persisted or not.
   *
   * @param persist if true, the size, position and last selected item in the TreeSearchView are
   *                being saved. When the dialog is showed again, it will be restored to
   *                the last saved state. Defaults to false.
   * @return this object for fluent API
   */
  // asciidoctor Documentation - tag::fluentApiMethod[]
  public PreferencesFx persistWindowState(boolean persist) {
    preferencesFxModel.setPersistWindowState(persist);
    return this;
  }
  // asciidoctor Documentation - end::fluentApiMethod[]

  /**
   * Defines whether the adjusted settings of the application should be saved or not.
   *
   * @param save if true, the values of all settings of the application are saved.
   *             When the application is started again, the settings values will be restored to
   *             the last saved state. Defaults to false.
   * @return this object for fluent API
   */
  public PreferencesFx saveSettings(boolean save) {
    preferencesFxModel.setSaveSettings(save);
    // if settings shouldn't be saved, clear them if there are any present
    if (!save) {
      preferencesFxModel.getStorageHandler().clearPreferences();
    }
    return this;
  }

  /**
   * Defines whether the table to debug the undo / redo history should be shown in a dialog
   * when pressing a key combination or not.
   * <\br>
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
   * @param newValue The new value for the translation service property.
   * @return PreferencesFx to allow for chaining.
   */
  public PreferencesFx i18n(TranslationService newValue) {
    preferencesFxModel.setTranslationService(newValue);
    return this;
  }

  /**
   * Registers an event handler with the model. The handler is called when the
   * model receives an {@code Event} of the specified type during the bubbling
   * phase of event delivery.
   *
   * @param eventType    the type of the events to receive by the handler
   * @param eventHandler the handler to register
   *
   * @throws NullPointerException if either event type or handler are {@code null}.
   */
  public PreferencesFx addEventHandler(EventType<PreferencesFxEvent> eventType, EventHandler<? super PreferencesFxEvent> eventHandler) {
    preferencesFxModel.addEventHandler(eventType,eventHandler);
    return this;
  }

  /**
   * Unregisters a previously registered event handler from the model. One
   * handler might have been registered for different event types, so the
   * caller needs to specify the particular event type from which to
   * unregister the handler.
   *
   * @param eventType    the event type from which to unregister
   * @param eventHandler the handler to unregister
   *
   * @throws NullPointerException if either event type or handler are {@code null}.
   */
  public PreferencesFx removeEventHandler(EventType<PreferencesFxEvent> eventType, EventHandler<? super PreferencesFxEvent> eventHandler) {
    preferencesFxModel.removeEventHandler(eventType, eventHandler);
    return this;
  }
}
