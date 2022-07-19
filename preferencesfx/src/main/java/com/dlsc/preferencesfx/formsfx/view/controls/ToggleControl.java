package com.dlsc.preferencesfx.formsfx.view.controls;

import com.dlsc.formsfx.model.structure.BooleanField;
import com.dlsc.preferencesfx.util.ElementVisibility;
import javafx.scene.control.Label;
import org.controlsfx.control.ToggleSwitch;

/**
 * Displays a control for boolean values with a toggle from ControlsFX.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class ToggleControl extends SimpleControl<BooleanField, ToggleSwitch> {
  public static final double NEGATIVE_LABEL_INSETS = -17.3;
  /**
   * - fieldLabel is the container that displays the label property of the
   * field.
   * - toggleSwitch is the toggle switch to set user input.
   * - container holds the toggle so that it can be styled properly.
   */
  private Label fieldLabel;

  private ElementVisibility elementVisibility;

  public ToggleControl() {
  }

  public ToggleControl(ElementVisibility elementVisibility) {
    this.elementVisibility = elementVisibility;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeParts() {
    super.initializeParts();

    fieldLabel = new Label(field.labelProperty().getValue());

    node = new ToggleSwitch();
    node.getStyleClass().add("toggle-control");
    // is necessary to offset the control to the left, because we don't use the provided label
    node.setTranslateX(NEGATIVE_LABEL_INSETS);
    node.setSelected(field.getValue());

    if (this.elementVisibility != null) {
      node.visibleProperty().bind(this.elementVisibility.visible());
      fieldLabel.visibleProperty().bind(this.elementVisibility.visible());
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void layoutParts() {

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setupBindings() {
    super.setupBindings();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setupValueChangedListeners() {
    super.setupValueChangedListeners();
    field.userInputProperty().addListener((observable, oldValue, newValue) -> {
      node.setSelected(Boolean.parseBoolean(field.getUserInput()));
    });
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setupEventHandlers() {
    node.selectedProperty().addListener((observable, oldValue, newValue) -> {
      field.userInputProperty().setValue(String.valueOf(newValue));
    });
  }

}


