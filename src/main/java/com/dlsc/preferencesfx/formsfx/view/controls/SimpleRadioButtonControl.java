package com.dlsc.preferencesfx.formsfx.view.controls;

/*-
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

import com.dlsc.formsfx.model.structure.SingleSelectionField;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

/**
 * This class provides the base implementation for a simple control to edit
 * radio button values.
 *
 * @author Rinesch Murugathas
 * @author Sacha Schmid
 */
public class SimpleRadioButtonControl<V> extends SimpleControl<SingleSelectionField<V>, VBox> {

  /**
   * - The fieldLabel is the container that displays the label property of
   * the field.
   * - The radioButtons is the list of radio buttons to display.
   * - The toggleGroup defines the group for the radio buttons.
   * - The node is a VBox holding all radio buttons.
   */
  private Label fieldLabel;
  private final List<RadioButton> radioButtons = new ArrayList<>();
  private ToggleGroup toggleGroup;

  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeParts() {
    super.initializeParts();

    node.getStyleClass().add("simple-radio-control");

    fieldLabel = new Label(field.labelProperty().getValue());
    toggleGroup = new ToggleGroup();
    node = new VBox();

    createRadioButtons();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void layoutParts() {
    node.setSpacing(5);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setupBindings() {
    super.setupBindings();

    fieldLabel.textProperty().bind(field.labelProperty());
    setupRadioButtonBindings();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setupValueChangedListeners() {
    super.setupValueChangedListeners();

    field.itemsProperty().addListener((observable, oldValue, newValue) -> {
      createRadioButtons();
      setupRadioButtonBindings();
      setupRadioButtonEventHandlers();
    });

    field.selectionProperty().addListener((observable, oldValue, newValue) -> {
      if (field.getSelection() != null) {
        radioButtons.get(field.getItems().indexOf(field.getSelection())).setSelected(true);
      } else {
        toggleGroup.getSelectedToggle().setSelected(false);
      }
    });

    field.errorMessagesProperty().addListener((observable, oldValue, newValue) -> toggleTooltip(node, radioButtons.get(radioButtons.size() - 1)));
    field.tooltipProperty().addListener((observable, oldValue, newValue) -> toggleTooltip(node, radioButtons.get(radioButtons.size() - 1)));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setupEventHandlers() {
    node.setOnMouseEntered(event -> toggleTooltip(node, radioButtons.get(radioButtons.size() - 1)));
    node.setOnMouseExited(event -> toggleTooltip(node, radioButtons.get(radioButtons.size() - 1)));
    setupRadioButtonEventHandlers();
  }

  /**
   * This method creates radio buttons and adds them to radioButtons
   * and is used when the itemsProperty on the field changes.
   */
  private void createRadioButtons() {
    node.getChildren().clear();
    radioButtons.clear();

    for (int i = 0; i < field.getItems().size(); i++) {
      RadioButton rb = new RadioButton();

      rb.setText(field.getItems().get(i).toString());
      rb.setToggleGroup(toggleGroup);

      radioButtons.add(rb);
    }

    if (field.getSelection() != null) {
      radioButtons.get(field.getItems().indexOf(field.getSelection())).setSelected(true);
    }

    node.getChildren().addAll(radioButtons);
  }

  /**
   * Sets up bindings for all radio buttons.
   */
  private void setupRadioButtonBindings() {
    for (RadioButton radio : radioButtons) {
      radio.disableProperty().bind(field.editableProperty().not());
    }
  }

  /**
   * Sets up bindings for all radio buttons.
   */
  private void setupRadioButtonEventHandlers() {
    for (int i = 0; i < radioButtons.size(); i++) {
      final int j = i;
      radioButtons.get(j).setOnAction(event -> field.select(j));
    }
  }

}
