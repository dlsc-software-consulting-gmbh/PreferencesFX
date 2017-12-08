package com.dlsc.preferencesfx.util.formsfx;

import com.dlsc.formsfx.model.structure.BooleanField;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.controlsfx.control.ToggleSwitch;

/**
 * Displays a control for boolean values with a toggle from ControlsFX.
 */
public class ToggleControl extends SimpleControl<BooleanField> {
  public static final double NEGATIVE_LABEL_INSETS = -17.3;
  /**
   * - fieldLabel is the container that displays the label property of the
   * field.
   * - toggleSwitch is the toggle switch to set user input.
   * - container holds the toggle so that it can be styled properly.
   */
  private Label fieldLabel;
  private ToggleSwitch toggleSwitch;
  private VBox container;

  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeParts() {
    super.initializeParts();

    getStyleClass().add("toggle-control");

    fieldLabel = new Label(field.labelProperty().getValue());
    toggleSwitch = new ToggleSwitch();
    // is necessary to offset the control to the left, because we don't use the provided label
    toggleSwitch.setTranslateX(NEGATIVE_LABEL_INSETS);
    container = new VBox();
    toggleSwitch.setSelected(field.getValue());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void layoutParts() {
    super.layoutParts();

    container.getChildren().add(toggleSwitch);

    add(fieldLabel, 0, 0, 2, 1);
    add(container, 2, 0, field.getSpan() - 2, 1);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setupBindings() {
    super.setupBindings();

    toggleSwitch.disableProperty().bind(field.editableProperty().not());
    fieldLabel.textProperty().bind(field.labelProperty());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setupValueChangedListeners() {
    super.setupValueChangedListeners();
    field.userInputProperty().addListener((observable, oldValue, newValue) -> {
      toggleSwitch.setSelected(Boolean.parseBoolean(field.getUserInput()));
    });

    field.errorMessagesProperty().addListener(
        (observable, oldValue, newValue) -> toggleTooltip(toggleSwitch));
    field.tooltipProperty().addListener(
        (observable, oldValue, newValue) -> toggleTooltip(toggleSwitch));

    toggleSwitch.focusedProperty().addListener(
        (observable, oldValue, newValue) -> toggleTooltip(toggleSwitch));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setupEventHandlers() {
    setOnMouseEntered(event -> toggleTooltip(toggleSwitch));
    setOnMouseExited(event -> toggleTooltip(toggleSwitch));

    toggleSwitch.selectedProperty().addListener((observable, oldValue, newValue) -> {
      field.userInputProperty().setValue(String.valueOf(newValue));
    });
  }

}


