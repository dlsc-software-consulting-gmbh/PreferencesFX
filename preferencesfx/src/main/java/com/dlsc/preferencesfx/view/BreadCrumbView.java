package com.dlsc.preferencesfx.view;

import com.dlsc.preferencesfx.model.Category;
import com.dlsc.preferencesfx.model.PreferencesFxModel;
import javafx.geometry.Pos;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.BreadCrumbBar;

/**
 * Displays the {@link BreadCrumbBar} of all {@link Category} up to a certain hierarchy level.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class BreadCrumbView extends HBox implements View {
  private static final Logger LOGGER =
      LogManager.getLogger(BreadCrumbView.class.getName());
  private final PreferencesFxModel model;
  private final UndoRedoBox undoRedoBox;
  TreeItem<Category> breadcrumbsItm;
  BreadCrumbBar<Category> breadCrumbBar = new BreadCrumbBar<>();

  /**
   * Constructs a new view, which displays the {@link BreadCrumbBar}.
   *
   * @param model the model of PreferencesFX
   */
  public BreadCrumbView(PreferencesFxModel model, UndoRedoBox undoRedoBox) {
    this.model = model;
    this.undoRedoBox = undoRedoBox;
    init();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeParts() {
    // only add breadCrumbBar if there's more than one category
    if (!model.isOneCategoryLayout()) {
      getChildren().add(breadCrumbBar);
    }
    getChildren().addAll(
        undoRedoBox
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void layoutParts() {
    HBox.setHgrow(breadCrumbBar, Priority.ALWAYS);
    // align undo / redo buttons on the right if there are no breadcrumbs
    if (model.isOneCategoryLayout()) {
      setAlignment(Pos.CENTER_RIGHT);
    }
  }
}
