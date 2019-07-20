package com.dlsc.preferencesfx.formsfx.view.controls;

/* -
 * ========================LICENSE_START=================================
 * FormsFX
 * %%
 * Copyright (C) 2017 DLSC Software & Consulting
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * =========================LICENSE_END==================================
 */

import com.dlsc.formsfx.model.structure.StringField;
import java.util.Objects;
import javafx.geometry.Pos;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 * This class provides the base implementation for a simple control to edit ColorPicker values.
 *
 * @author Sacha Schmid
 * @author Rinesch Murugathas
 * @author François Martin
 * @author Marco Sanfratello
 * @author Arvid Nyström
 */
public class SimpleColorPickerControl extends SimpleControl<StringField, StackPane> {

  /**
   * - The colorPicker is the container that displays the node to select a color value.
   */
  private ColorPicker colorPicker;
  private Color initialValue;
  private Label fieldLabel;

  /**
   * Create a SimpleColorPickerControl with an initial value.
   *
   * @param initialValue The initial color, cannot be null.
   */
  public SimpleColorPickerControl(Color initialValue) {
    Objects.requireNonNull(initialValue);
    this.initialValue = initialValue;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeParts() {
    super.initializeParts();

    node = new StackPane();
    node.getStyleClass().add("simple-text-control");

    colorPicker = new ColorPicker(initialValue);
    colorPicker.setMaxWidth(Double.MAX_VALUE);
    colorPicker.setOnAction(event -> {
      if (!field.valueProperty().getValue().equals(colorPicker.getValue().toString())) {
        field.valueProperty().setValue(colorPicker.getValue().toString());
      }
    });
    field.valueProperty().setValue(colorPicker.getValue().toString());
    fieldLabel = new Label(field.labelProperty().getValue());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void layoutParts() {
    node.getChildren().addAll(colorPicker);
    node.setAlignment(Pos.CENTER_LEFT);
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

    field.valueProperty().addListener((observable, oldValue, newValue) -> {
      if (!newValue.isEmpty()) {
        Color newColor = Color.valueOf(newValue);
        if (!colorPicker.getValue().equals(newColor)) {
          colorPicker.setValue(newColor);
        }
      }
    });

    field.errorMessagesProperty().addListener(
        (observable, oldValue, newValue) -> toggleTooltip(colorPicker)
    );
    field.tooltipProperty().addListener(
        (observable, oldValue, newValue) -> toggleTooltip(colorPicker)
    );
    colorPicker.focusedProperty().addListener(
        (observable, oldValue, newValue) -> toggleTooltip(colorPicker)
    );
  }
}
