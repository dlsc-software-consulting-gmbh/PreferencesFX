package com.dlsc.preferencesfx2;

import com.dlsc.preferencesfx.Category;
import com.dlsc.preferencesfx.history.HistoryDialog;
import com.dlsc.preferencesfx2.model.PreferencesModel;
import com.dlsc.preferencesfx2.view.CategoryPresenter;
import com.dlsc.preferencesfx2.view.CategoryView;
import com.dlsc.preferencesfx2.view.PreferencesDialog;
import com.dlsc.preferencesfx2.view.PreferencesPresenter;
import com.dlsc.preferencesfx2.view.PreferencesView;
import com.dlsc.preferencesfx2.view.NavigationPresenter;
import com.dlsc.preferencesfx2.view.NavigationView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PreferencesFx {
  private static final Logger LOGGER =
      LogManager.getLogger(PreferencesFx.class.getName());

  private PreferencesModel preferencesModel;

  private NavigationView treeSearchView;
  private NavigationPresenter treeSearchPresenter;

  private CategoryView categoryView;
  private CategoryPresenter categoryPresenter;

  private PreferencesView preferenceView;
  private PreferencesPresenter preferencePresenter;

  private PreferencesFx(Class<?> saveClass, Category[] categories) {
    preferencesModel = new PreferencesModel(saveClass, categories);

    treeSearchView = new NavigationView(preferencesModel);
    treeSearchPresenter = new NavigationPresenter(preferencesModel, treeSearchView);

    categoryView = new CategoryView(preferencesModel);
    categoryPresenter = new CategoryPresenter(preferencesModel, categoryView);

    preferenceView = new PreferencesView(preferencesModel, treeSearchView, categoryView);
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
   * Shows the PreferencesFX dialog.
   */
  public void show() {
    new PreferencesDialog(preferencesModel, preferenceView);
    if (preferencesModel.getHistoryDebugState()) {
      setupDebugHistoryTable();
    }
  }

  private void setupDebugHistoryTable() {
    final KeyCombination keyCombination = new KeyCodeCombination(KeyCode.H,
        KeyCombination.SHORTCUT_DOWN);
    preferenceView.getScene().addEventHandler(KeyEvent.KEY_RELEASED, event -> {
          if (keyCombination.match(event)) {
            LOGGER.trace("Opened History Debug View");
            new HistoryDialog(preferencesModel.getHistory());
          }
        }
    );
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
   * Pressing Ctrl + H (Windows) or CMD + H (Mac) opens a dialog with the undo / redo history,
   * shown in a table.
   *
   * @param debugState if true, pressing the key combination will open the dialog
   * @return this object for fluent API
   */
  public PreferencesFx debugHistoryMode(boolean debugState) {
    preferencesModel.setHistoryDebugState(debugState);
    return this;
  }
}
