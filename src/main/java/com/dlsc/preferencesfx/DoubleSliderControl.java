package com.dlsc.preferencesfx;

import com.dlsc.formsfx.model.structure.DoubleField;
import com.dlsc.formsfx.view.controls.SimpleControl;
import java.math.BigDecimal;
import java.math.RoundingMode;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;

/**
 * Created by François Martin on 24.11.17.
 */
public class DoubleSliderControl extends SimpleControl<DoubleField> {
  /**
   * - fieldLabel is the container that displays the label property of the
   * field.
   * - slider is the control to change the value.
   * - container holds the control so that it can be styled properly.
   */
  private Label fieldLabel;
  private Slider slider;
  private VBox container;
  private Label valueLabel;
  private double min, max;
  private int precision;

  /**
   * Creates a slider for double values with a minimum and maximum value, with a set precision.
   * @param min minimum slider value
   * @param max maximum slider value
   * @param precision number of digits after the decimal point
   */
  DoubleSliderControl(double min, double max, int precision) {
    super();
    this.min = min;
    this.max = max;
    this.precision = precision;
  }

  /**
   * Rounds a value to a given precision, using {@link RoundingMode#HALF_UP}.
   * @param value value to be rounded
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

    getStyleClass().add("double-slider-control");

    fieldLabel = new Label(field.labelProperty().getValue());

    valueLabel = new Label(String.valueOf(field.getValue().doubleValue()));

    slider = new Slider();
    slider.setMin(min);
    slider.setMax(max);
    slider.setShowTickLabels(true);
    slider.setShowTickMarks(true);

    container = new VBox();
    slider.setValue(field.getValue());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void layoutParts() {
    super.layoutParts();

    container.getChildren().add(slider);

    add(fieldLabel, 0, 0, 2, 1);
    add(container, 2, 0, field.getSpan() - 4, 1);
    add(valueLabel, 2+field.getSpan()-3, 0, 2, 1);
    valueLabel.setAlignment(Pos.CENTER);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setupBindings() {
    super.setupBindings();

    slider.disableProperty().bind(field.editableProperty().not());
    fieldLabel.textProperty().bind(field.labelProperty());
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
    setOnMouseEntered(event -> toggleTooltip(slider));
    setOnMouseExited(event -> toggleTooltip(slider));

    slider.valueProperty().addListener((observable, oldValue, newValue) -> {
      field.userInputProperty().setValue(String.valueOf(round(newValue.doubleValue(), precision)));
    });
  }
}
