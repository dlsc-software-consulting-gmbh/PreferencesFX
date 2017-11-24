package com.dlsc.preferencesfx;

import com.dlsc.formsfx.model.structure.IntegerField;
import com.dlsc.formsfx.view.controls.SimpleNumberControl;
import javafx.scene.control.SpinnerValueFactory;

/**
 * Created by François Martin on 24.11.17.
 */
public class IntegerSliderControl extends SimpleNumberControl<IntegerField, Integer> {

  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeParts() {
    super.initializeParts();

    getStyleClass().addAll("integer-slider-control");
    editableSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(Integer.MIN_VALUE, Integer.MAX_VALUE, field.getValue()));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setupValueChangedListeners() {
    super.setupValueChangedListeners();

    field.errorMessagesProperty().addListener((observable, oldValue, newValue) -> toggleTooltip(editableSpinner));
    field.tooltipProperty().addListener((observable, oldValue, newValue) -> toggleTooltip(editableSpinner));
  }

}
