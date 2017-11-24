package com.dlsc.preferencesfx.util;

import com.dlsc.formsfx.model.structure.Group;
import com.dlsc.formsfx.view.renderer.GroupRendererBase;
import javafx.geometry.Insets;

public class PreferencesGroupRenderer extends GroupRendererBase {

//    probably here decision if title or not / border

  /**
   * This is the constructor to pass over data.
   *
   * @param group The section which gets rendered.
   */
  PreferencesGroupRenderer(Group group) {
    this.element = group;
    init();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeParts() {
    super.initializeParts();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void layoutParts() {
    super.layoutParts();

    getStyleClass().add("formsfx-group");

    setFocusTraversable(false);
    setPadding(new Insets(SPACING * 2));
    getChildren().add(grid);
  }

}
