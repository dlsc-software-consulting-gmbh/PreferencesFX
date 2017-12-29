package com.dlsc.preferencesfx_raw;

import com.dlsc.preferencesfx2.model.Category;
import com.dlsc.preferencesfx_raw.history.HistoryButtonBox;
import com.dlsc.preferencesfx_raw.history.HistoryDialog;
import com.dlsc.preferencesfx2.util.StorageHandler;
import javafx.geometry.Side;
import javafx.scene.control.TreeItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import org.controlsfx.control.MasterDetailPane;

public class PreferencesFx extends BorderPane {
  private CategoryTree categoryTree;
  private CategoryTreeBox categoryTreeBox;

  private void setupParts() {
    //categoryTree = new CategoryTree(this, categories);
    categoryTreeBox = new CategoryTreeBox(categoryTree);
  }

  private void layoutParts() {
    // Load last selected category in NavigationView.
    //categoryTree.setSelectedCategoryById(storageHandler.loadSelectedCategory()); // loading done in model!
    TreeItem treeItem = (TreeItem) categoryTree.getSelectionModel().getSelectedItem();
    showCategory((Category) treeItem.getValue());
  }

  /**
   * Shows the category as its CategoryPane in the master node.
   *
   * @param category the category to be shown
   */
  public void showCategory(Category category) {
    //displayedCategory = category;
    //preferencesPane.setMasterNode(new CategoryPane(category.getGroups())); // Replaced this, due to refactoring of CategoryPane in Categories
    // Sets the saved divider position.
    //preferencesPane.setDividerPosition(storageHandler.loadDividerPosition());// loading done in model!
  }

  /**
   * Saves the current selected Category.
   */
  public void saveSelectedCategory() {
    TreeItem treeItem = (TreeItem) categoryTree.getSelectionModel().getSelectedItem();
    Category category;
    if (treeItem != null) {
      category = (Category) treeItem.getValue();
    } else {
      category = categories.get(DEFAULT_CATEGORY);
    }
    storageHandler.saveSelectedCategory(category.getId());
  }

  /**
   * Retrieves the category which has last been displayed in the CategoryPane.
   */
  public Category getDisplayedCategory() {
    return displayedCategory;
  }


  /**
   * Shows the PreferencesFX dialog.
   */
  public void show() {
    new PreferencesDialog(this, persistWindowState);
    if (historyDebugState) {
      setupDebugHistoryTable();
    }
  }

  /**
   * Defines whether the state of the dialog window should be persisted or not.
   *
   * @param persist if true, the size, position and last selected item in the NavigationView are
   *                being saved. When the dialog is showed again, it will be restored to
   *                the last saved state. Defaults to false.
   * @return this object for fluent API
   */
  public PreferencesFx persistWindowState(boolean persist) {
    this.persistWindowState = persist;
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
    this.historyDebugState = debugState;
    return this;
  }

  public void setupDebugHistoryTable() {
    final KeyCombination keyCombination = new KeyCodeCombination(KeyCode.H,
        KeyCombination.SHORTCUT_DOWN);
    getScene().addEventHandler(KeyEvent.KEY_RELEASED, event -> {
          if (keyCombination.match(event)) {
            LOGGER.trace("Opened History Debug View");
            new HistoryDialog(history);
          }
        }
    );
  }

  public StorageHandler getStorageHandler() {
    return storageHandler;
  }

  public CategoryTree getCategoryTree() {
    return categoryTree;
  }
}
