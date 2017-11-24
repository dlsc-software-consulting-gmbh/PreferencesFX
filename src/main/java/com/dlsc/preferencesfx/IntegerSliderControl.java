package com.dlsc.preferencesfx;

import com.dlsc.formsfx.model.structure.IntegerField;
import com.dlsc.formsfx.view.controls.SimpleControl;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;

/**
 * Created by François Martin on 24.11.17.
 */
public class IntegerSliderControl extends SimpleControl<IntegerField> {
  /**
   * - fieldLabel is the container that displays the label property of the
   * field.
   * - slider is the control to change the value.
   * - container holds the control so that it can be styled properly.
   */
  private Label fieldLabel;
  private Slider slider;
  private Label valueLabel;
  private VBox container;
  private int min, max;

  /**
   * Creates a slider for integer values.
   * @param min minimum slider value
   * @param max maximum slider value
   */
  IntegerSliderControl(int min, int max) {
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

    getStyleClass().add("integer-slider-control");

    fieldLabel = new Label(field.labelProperty().getValue());

    valueLabel = new Label(String.valueOf(field.getValue().intValue()));

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
    setOnMouseEntered(event -> toggleTooltip(slider));
    setOnMouseExited(event -> toggleTooltip(slider));

    slider.valueProperty().addListener((observable, oldValue, newValue) -> {
      field.userInputProperty().setValue(String.valueOf(newValue.intValue()));
    });
  }

}
