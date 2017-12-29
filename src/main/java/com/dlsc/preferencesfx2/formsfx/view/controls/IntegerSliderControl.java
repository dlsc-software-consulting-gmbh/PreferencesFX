package com.dlsc.preferencesfx2.formsfx.view.controls;

import com.dlsc.formsfx.model.structure.IntegerField;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

/**
 * Created by François Martin on 24.11.17.
 */
public class IntegerSliderControl extends SimpleControl<IntegerField, HBox> {
  public static final int VALUE_LABEL_PADDING = 25;
  /**
   * - fieldLabel is the container that displays the label property of the
   * field.
   * - slider is the control to change the value.
   * - container holds the control so that it can be styled properly.
   */
  private Label fieldLabel;
  private Slider slider;
  private Label valueLabel;
  private int min;
  private int max;

  /**
   * Creates a slider for integer values.
   *
   * @param min minimum slider value
   * @param max maximum slider value
   */
  public IntegerSliderControl(int min, int max) {
    super();
    this.min = min;
    this.max = max;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeParts() {
    super.initializeParts();

    fieldLabel = new Label(field.labelProperty().getValue());

    valueLabel = new Label(String.valueOf(field.getValue().intValue()));

    slider = new Slider();
    slider.setMin(min);
    slider.setMax(max);
    slider.setShowTickLabels(false);
    slider.setShowTickMarks(false);
    slider.setValue(field.getValue());

    node = new HBox();
    node.getStyleClass().add("integer-slider-control");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void layoutParts() {
    node.getChildren().addAll(slider, valueLabel);
    HBox.setHgrow(slider, Priority.ALWAYS);
    valueLabel.setAlignment(Pos.CENTER);
    valueLabel.setMinWidth(VALUE_LABEL_PADDING);
    node.setSpacing(VALUE_LABEL_PADDING);
    HBox.setMargin(valueLabel, new Insets(0,VALUE_LABEL_PADDING, 0,0));
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
      int sliderValue = Integer.parseInt(field.getUserInput());
      slider.setValue(sliderValue);
      valueLabel.setText(String.valueOf(sliderValue));
    });

    field.errorMessagesProperty().addListener(
        (observable, oldValue, newValue) -> toggleTooltip(slider));
    field.tooltipProperty().addListener(
        (observable, oldValue, newValue) -> toggleTooltip(slider));

    slider.focusedProperty().addListener(
        (observable, oldValue, newValue) -> toggleTooltip(slider));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setupEventHandlers() {
    slider.valueProperty().addListener((observable, oldValue, newValue) -> {
      field.userInputProperty().setValue(String.valueOf(newValue.intValue()));
    });
  }

}
