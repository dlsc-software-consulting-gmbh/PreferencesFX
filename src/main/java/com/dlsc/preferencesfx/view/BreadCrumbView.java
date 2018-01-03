package com.dlsc.preferencesfx.view;

import com.dlsc.preferencesfx.model.Category;
import com.dlsc.preferencesfx.model.PreferencesFxModel;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.HBox;
import org.controlsfx.control.BreadCrumbBar;

public class BreadCrumbView extends HBox {
  private final PreferencesFxModel model;
  TreeItem<Category> breadcrumbsItm;
  BreadCrumbBar<Category> breadCrumbBar = new BreadCrumbBar<>();

  public BreadCrumbView(PreferencesFxModel model) {
    this.model = model;
    layoutParts();
  }

  private void layoutParts() {
    getChildren().addAll(
        breadCrumbBar
    );
  }
}
