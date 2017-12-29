package com.dlsc.preferencesfx2.formsfx.view.controls;

import com.dlsc.formsfx.model.structure.DoubleField;
import java.math.BigDecimal;
import java.math.RoundingMode;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

/**
 * Created by François Martin on 24.11.17.
 */
public class DoubleSliderControl extends SimpleControl<DoubleField, HBox> {
  public static final int VALUE_LABEL_PADDING = 25;
  /**
   * - fieldLabel is the container that displays the label property of the
   * field.
   * - slider is the control to change the value.
   * - node holds the control so that it can be styled properly.
   */
  private Slider slider;
  private Label valueLabel;
  private double min;
  private double max;
  private int precision;

  /**
   * Creates a slider for double values with a minimum and maximum value, with a set precision.
   *
   * @param min       minimum slider value
   * @param max       maximum slider value
   * @param precision number of digits after the decimal point
   */
  public DoubleSliderControl(double min, double max, int precision) {
    super();
    this.min = min;
    this.max = max;
    this.precision = precision;
  }

  /**
   * Rounds a value to a given precision, using {@link RoundingMode#HALF_UP}.
   *
   * @param value     value to be rounded
   * @param precision number of digits after the decimal point
   * @return
   */
  private double round(double value, int precision) {
    return BigDecimal.valueOf(value)
        .setScale(precision, RoundingMode.HALF_UP)
        .doubleValue();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeParts() {
    super.initializeParts();

    fieldLabel = new Label(field.labelProperty().getValue());

    valueLabel = new Label(String.valueOf(field.getValue().doubleValue()));

    slider = new Slider();
    slider.setMin(min);
    slider.setMax(max);
    slider.setShowTickLabels(false);
    slider.setShowTickMarks(false);
    slider.setValue(field.getValue());

    node = new HBox();
    node.getStyleClass().add("double-slider-control");
  }

  public void layoutParts() {
    node.getChildren().addAll(slider, valueLabel);
    HBox.setHgrow(slider, Priority.ALWAYS);
    valueLabel.setAlignment(Pos.CENTER);
    valueLabel.setMinWidth(VALUE_LABEL_PADDING);
    node.setSpacing(VALUE_LABEL_PADDING);
    HBox.setMargin(valueLabel, new Insets(0, VALUE_LABEL_PADDING, 0, 0));
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
      double sliderValue = round(Double.parseDouble(field.getUserInput()), precision);
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
      field.userInputProperty().setValue(String.valueOf(round(newValue.doubleValue(), precision)));
    });
  }
}
