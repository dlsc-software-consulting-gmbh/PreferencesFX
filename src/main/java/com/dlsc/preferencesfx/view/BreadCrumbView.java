package com.dlsc.preferencesfx.view;

import com.dlsc.preferencesfx.history.view.HistoryButtonBox;
import com.dlsc.preferencesfx.model.Category;
import com.dlsc.preferencesfx.model.PreferencesFxModel;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.BreadCrumbBar;

public class BreadCrumbView extends HBox implements View {
  private static final Logger LOGGER =
      LogManager.getLogger(BreadCrumbView.class.getName());
  private final PreferencesFxModel model;
  private final HistoryButtonBox historyButtonBox;
  TreeItem<Category> breadcrumbsItm;
  BreadCrumbBar<Category> breadCrumbBar = new BreadCrumbBar<>();

  public BreadCrumbView(PreferencesFxModel model, HistoryButtonBox historyButtonBox) {
    this.model = model;
    this.historyButtonBox = historyButtonBox;
    init();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeParts() {
    getChildren().addAll(
        breadCrumbBar, historyButtonBox
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void layoutParts() {
    HBox.setHgrow(breadCrumbBar, Priority.ALWAYS);
  }
}
