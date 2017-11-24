package com.dlsc.preferencesfx.util;

import com.dlsc.formsfx.model.structure.Field;
import com.dlsc.formsfx.view.controls.SimpleControl;
import com.dlsc.formsfx.view.util.ViewMixin;
import java.util.Iterator;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class PreferencesGroupRenderer extends VBox implements ViewMixin {

  protected final int SPACING = 10;
  protected GridPane grid;
  protected PreferencesGroup group;

  private Label titleLabel;

  /**
   * This is the constructor to pass over data.
   *
   * @param group The section which gets rendered.
   */
  PreferencesGroupRenderer(PreferencesGroup group) {
    this.group = group;
    init();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeParts() {
    this.grid = new GridPane();
    titleLabel = new Label();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void layoutParts() {
    int COLUMN_COUNT = 12;

    int currentRow;
    for (currentRow = 0; currentRow < COLUMN_COUNT; ++currentRow) {
      ColumnConstraints colConst = new ColumnConstraints();
      colConst.setPercentWidth(100.0D / (double) COLUMN_COUNT);
      this.grid.getColumnConstraints().add(colConst);
    }

    this.grid.setHgap(10.0D);
    this.grid.setVgap(10.0D);
    this.setPadding(new Insets(10.0D));
    currentRow = 0;
    int currentColumnCount = 0;

    int span;
    for (Iterator var4 = group.getFields().iterator(); var4.hasNext(); currentColumnCount += span) {
      Field f = (Field) var4.next();
      span = f.getSpan();
      if (currentColumnCount + span > COLUMN_COUNT) {
        ++currentRow;
        currentColumnCount = 0;
      }

      SimpleControl c = f.getRenderer();
      c.setField(f);
      this.grid.add(c, currentColumnCount, currentRow, span, 1);
    }

    getStyleClass().add("preferencesfx-group");

    // Spaces from each Group
    setPadding(new Insets(SPACING * 1.5));
    setMargin(titleLabel, new Insets(0, 0, 10, 0));
    titleLabel.getStyleClass().add("category-title");

    if (group.getTitle() != null) {
      getChildren().add(titleLabel);
    }
    getChildren().add(grid);
  }

  public void setupBindings() {
    titleLabel.textProperty().bind(group.titleProperty());
  }

}
