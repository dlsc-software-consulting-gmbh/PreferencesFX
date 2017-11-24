package com.dlsc.preferencesfx;

import com.dlsc.formsfx.model.structure.DoubleField;
import com.dlsc.formsfx.view.controls.SimpleControl;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
  private double min, max;
  private int precision;

  /**
   * {@inheritDoc}
   */
  DoubleSliderControl(double min, double max, int precision) {
    super();
    this.min = min;
    this.max = max;
    this.precision = precision;
  }

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

    slider = new Slider();
    slider.setMin(min);
    slider.setMax(max);

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
    add(container, 2, 0, field.getSpan() - 2, 1);
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
      slider.setValue(round(Double.parseDouble(field.getUserInput()), precision));
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
