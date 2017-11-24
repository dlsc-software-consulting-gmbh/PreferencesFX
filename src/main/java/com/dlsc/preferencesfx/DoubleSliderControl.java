package com.dlsc.preferencesfx;

import com.dlsc.formsfx.model.structure.DoubleField;
import com.dlsc.formsfx.view.controls.SimpleNumberControl;
import javafx.scene.control.SpinnerValueFactory;

/**
 * Created by François Martin on 24.11.17.
 */
public class DoubleSliderControl extends SimpleNumberControl<DoubleField, Double> {

  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeParts() {
    super.initializeParts();

    getStyleClass().add("simple-double-control");
    editableSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(-Double.MAX_VALUE, Double.MAX_VALUE, field.getValue()));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setupValueChangedListeners() {
    super.setupValueChangedListeners();

    field.tooltipProperty().addListener((observable, oldValue, newValue) -> toggleTooltip(editableSpinner));
    field.errorMessagesProperty().addListener((observable, oldValue, newValue) -> toggleTooltip(editableSpinner));
  }
}
