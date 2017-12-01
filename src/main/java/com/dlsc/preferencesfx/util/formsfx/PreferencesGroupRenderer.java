package com.dlsc.preferencesfx.util.formsfx;

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

  /**
   * SPACING is used to set the spacing of the group as well as the
   * spacing for vertical/horizontal gaps between controls.
   */
  private static final double SPACING = 10;
  /**
   * Add the controls in the GridPane in a 12-column layout. If a control
   * takes up too much horizontal space, wrap it to the next row.
   */
  private static final int COLUMN_COUNT = 12;
  private Label titleLabel;
  private GridPane grid;
  private PreferencesGroup preferencesGroup;

  /**
   * This is the constructor to pass over data.
   *
   * @param preferencesGroup The PreferencesGroup which gets rendered.
   */
  PreferencesGroupRenderer(PreferencesGroup preferencesGroup) {
    this.preferencesGroup = preferencesGroup;
    preferencesGroup.setRenderer(this);
    init();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeParts() {
    grid = new GridPane();
    titleLabel = new Label();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void layoutParts() {
    int currentRow;
    for (currentRow = 0; currentRow < COLUMN_COUNT; ++currentRow) {
      ColumnConstraints colConst = new ColumnConstraints();
      colConst.setPercentWidth(SPACING * 10 / (double) COLUMN_COUNT);
      grid.getColumnConstraints().add(colConst);
    }

    currentRow = 0;
    int currentColumnCount = 0;

    int span;
    for (Iterator var4 = preferencesGroup.getFields().iterator(); var4.hasNext();
         currentColumnCount += span) {
      Field f = (Field) var4.next();
      span = f.getSpan();

      if (currentColumnCount + span > COLUMN_COUNT) {
        ++currentRow;
        currentColumnCount = 0;
      }

      SimpleControl c = f.getRenderer();
      c.setField(f);
      grid.add(c, currentColumnCount, currentRow, span, 1);
    }

    // Styling
    grid.setHgap(SPACING);
    grid.setVgap(SPACING);
    setPadding(new Insets(SPACING * 1.5));
    titleLabel.getStyleClass().add("category-title");

    // Only when the preferencesGroup has a title
    if (preferencesGroup.getTitle() != null) {
      getChildren().add(titleLabel);
      grid.getStyleClass().add("category-content");
    }
    getChildren().add(grid);
  }

  public void setupBindings() {
    titleLabel.textProperty().bind(preferencesGroup.titleProperty());
  }

  /**
   * Adds a style class to the control.
   * @param name of the style class to be added to the control
   */
  public void addStyleClass(String name) {
    getStyleClass().add(name);
  }

  /**
   * Removes a style class from the control.
   * @param name of the class to be removed from the control
   */
  public void removeStyleClass(String name) {
    getStyleClass().remove(name);
  }
}
