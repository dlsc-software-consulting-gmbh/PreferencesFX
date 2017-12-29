package com.dlsc.preferencesfx2.formsfx.view.controls;

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

import com.dlsc.formsfx.model.structure.DataField;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.StackPane;

/**
 * This class provides the base implementation for a simple control to edit
 * numerical fields.
 *
 * @author Rinesch Murugathas
 * @author Sacha Schmid
 */
public abstract class SimpleNumberControl<F extends DataField, D extends Number> extends SimpleControl<F, StackPane> {

  /**
   * This StackPane is needed for achieving the readonly effect by putting
   * the {@code readOnlyLabel} over the {@code editableSpinner} on the change
   * of the {@code visibleProperty}.
   */

  /**
   * - The fieldLabel is the container that displays the label property of
   * the field.
   * - The editableSpinner is a Spinner for setting numerical values.
   * - The readOnlyLabel is the label to put over editableSpinner.
   */
  private Label fieldLabel;
  protected Spinner<D> editableSpinner;
  private Label readOnlyLabel;

  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeParts() {
    super.initializeParts();

    node = new StackPane();

    fieldLabel = new Label();
    readOnlyLabel = new Label();
    editableSpinner = new Spinner<>();
    editableSpinner.setEditable(true);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void layoutParts() {
    readOnlyLabel.getStyleClass().add("read-only-label");
    node.getChildren().addAll(editableSpinner, readOnlyLabel);
    node.setAlignment(Pos.CENTER_LEFT);

    editableSpinner.setMaxWidth(Double.MAX_VALUE);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setupBindings() {
    super.setupBindings();

    editableSpinner.visibleProperty().bind(field.editableProperty());
    readOnlyLabel.visibleProperty().bind(field.editableProperty().not());

    editableSpinner.getEditor().textProperty().bindBidirectional(field.userInputProperty());
    readOnlyLabel.textProperty().bind(field.userInputProperty());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setupEventHandlers() {
    editableSpinner.getEditor().setOnKeyPressed(event -> {
      switch (event.getCode()) {
        case UP:
          editableSpinner.increment(1);
          break;
        case DOWN:
          editableSpinner.decrement(1);
          break;
      }
    });
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setupValueChangedListeners() {
    super.setupValueChangedListeners();
    editableSpinner.focusedProperty().addListener((observable, oldValue, newValue) -> toggleTooltip(editableSpinner));
  }
}
