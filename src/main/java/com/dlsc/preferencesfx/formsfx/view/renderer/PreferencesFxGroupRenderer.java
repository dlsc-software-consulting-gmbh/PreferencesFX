package com.dlsc.preferencesfx.formsfx.view.renderer;

import com.dlsc.formsfx.model.structure.Field;
import com.dlsc.preferencesfx.formsfx.view.controls.SimpleControl;
import com.dlsc.preferencesfx.util.PreferencesFxUtils;
import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

/**
 * This class renders a group for a PreferencesFx form.
 *
 * @author Sacha Schmid
 * @author Rinesch Murugathas
 * @author François Martin
 * @author Marco Sanfratello
 */
public class PreferencesFxGroupRenderer {

  /**
   * Add the controls in the GridPane in a 12-column layout. If a control
   * takes up too much horizontal space, wrap it to the next row.
   */
  private static final int COLUMN_COUNT = 12;
  public static final int GRID_MARGIN = 10;
  private Label titleLabel;
  private GridPane grid;
  private PreferencesFxGroup preferencesGroup;

  /**
   * This is the constructor to pass over data.
   *
   * @param preferencesGroup The PreferencesGroup which gets rendered.
   */
  PreferencesFxGroupRenderer(PreferencesFxGroup preferencesGroup, GridPane grid) {
    this.preferencesGroup = preferencesGroup;
    this.grid = grid;
    preferencesGroup.setRenderer(this);
    init();
  }

  /**
   * Calls all the other methods for easier initialization.
   */
  public void init() {
    this.initializeParts();
    this.layoutParts();
    this.setupBindings();
  }

  /**
   * Initializes all parts of the rendered group.
   */
  public void initializeParts() {
    titleLabel = new Label();
  }

  /**
   * Defines the layout of the rendered group.
   */
  public void layoutParts() {
    StringBuilder styleClass = new StringBuilder("group");

    // if there are no rows yet, getRowCount returns -1, in this case the next row is 0
    int nextRow = PreferencesFxUtils.getRowCount(grid) + 1;

    // Only when the preferencesGroup has a title
    if (preferencesGroup.getTitle() != null) {
      grid.add(titleLabel, 0, nextRow++, 2, 1);
      styleClass.append("-title");
      titleLabel.getStyleClass().add("group-title");
    }

    List<Field> fields = preferencesGroup.getFields();
    styleClass.append("-setting");

    int rowAmount = nextRow;
    for (int i = 0; i < fields.size(); i++) {
      // add to GridPane
      Field field = fields.get(i);
      SimpleControl c = (SimpleControl) field.getRenderer();
      c.setField(field);
      grid.add(c.getFieldLabel(), 0, i + rowAmount, 1, 1);
      grid.add(c.getNode(), 1, i + rowAmount, 1, 1);

      // Styling
      GridPane.setHgrow(c.getNode(), Priority.SOMETIMES);
      GridPane.setValignment(c.getNode(), VPos.CENTER);
      GridPane.setValignment(c.getFieldLabel(), VPos.CENTER);

      // additional styling for the last setting
      if (i == fields.size() - 1) {
        styleClass.append("-last");
        GridPane.setMargin(
            c.getNode(),
            new Insets(0, 0, PreferencesFxFormRenderer.SPACING * 4, 0)
        );
        GridPane.setMargin(
            c.getFieldLabel(),
            new Insets(0, 0, PreferencesFxFormRenderer.SPACING * 4, 0)
        );
      }

      c.getFieldLabel().getStyleClass().add(styleClass.toString() + "-label");
      c.getNode().getStyleClass().add(styleClass.toString() + "-node");
    }
  }

  /**
   * Sets up bindings of the rendered group.
   */
  public void setupBindings() {
    titleLabel.textProperty().bind(preferencesGroup.titleProperty());
  }

  /**
   * Adds a style class to the group.
   *
   * @param name of the style class to be added to the group
   */
  public void addStyleClass(String name) {
    titleLabel.getStyleClass().add(name);
  }

  /**
   * Removes a style class from the group.
   *
   * @param name of the class to be removed from the group
   */
  public void removeStyleClass(String name) {
    titleLabel.getStyleClass().remove(name);
  }

  public Label getTitleLabel() {
    return titleLabel;
  }
}
