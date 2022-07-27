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
import com.dlsc.preferencesfx.util.VisibilityProperty;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

/**
 * This class provides the base implementation for a simple control to edit
 * string values.
 *
 * @author Rinesch Murugathas
 * @author Sacha Schmid
 * @author François Martin
 * @author Marco Sanfratello
 */
public class SimpleTextControl extends SimpleControl<StringField, StackPane> {

  /**
   * - The fieldLabel is the container that displays the label property of
   * the field.
   * - The editableField allows users to modify the field's value.
   * - The readOnlyLabel displays the field's value if it is not editable.
   */
  private TextField editableField;
  private TextArea editableArea;
  private Label readOnlyLabel;
  private Label fieldLabel;

  public SimpleTextControl() {
  }

  /**
   * Constructs a SimpleTextControl of {@link SimpleTextControl} type, with visibility condition.
   *
   * @param visibilityProperty - property for control visibility of this element
   *
   * @return the constructed SimpleTextControl
   */
  public static SimpleTextControl of(VisibilityProperty visibilityProperty) {
    SimpleTextControl simpleTextControl = new SimpleTextControl();

    simpleTextControl.visibilityProperty = visibilityProperty;

    return simpleTextControl;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeParts() {
    super.initializeParts();

    node = new StackPane();
    node.getStyleClass().add("simple-text-control");

    editableField = new TextField(field.getValue());
    editableArea = new TextArea(field.getValue());

    readOnlyLabel = new Label(field.getValue());
    fieldLabel = new Label(field.labelProperty().getValue());
    editableField.setPromptText(field.placeholderProperty().getValue());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void layoutParts() {
    readOnlyLabel.getStyleClass().add("read-only-label");

    readOnlyLabel.setPrefHeight(26);

    editableArea.getStyleClass().add("simple-textarea");
    editableArea.setPrefRowCount(5);
    editableArea.setPrefHeight(80);
    editableArea.setWrapText(true);

    if (field.isMultiline()) {
      node.setPrefHeight(80);
      readOnlyLabel.setPrefHeight(80);
    }

    node.getChildren().addAll(editableField, editableArea, readOnlyLabel);

    node.setAlignment(Pos.CENTER_LEFT);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setupBindings() {
    super.setupBindings();

    editableArea.visibleProperty().bind(Bindings.and(field.editableProperty(),
        field.multilineProperty()));
    editableField.visibleProperty().bind(Bindings.and(field.editableProperty(),
        field.multilineProperty().not()));
    readOnlyLabel.visibleProperty().bind(field.editableProperty().not());

    editableField.textProperty().bindBidirectional(field.userInputProperty());
    editableArea.textProperty().bindBidirectional(field.userInputProperty());
    readOnlyLabel.textProperty().bind(field.userInputProperty());
    editableField.promptTextProperty().bind(field.placeholderProperty());
    editableArea.promptTextProperty().bind(field.placeholderProperty());

    editableArea.managedProperty().bind(editableArea.visibleProperty());
    editableField.managedProperty().bind(editableField.visibleProperty());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setupValueChangedListeners() {
    super.setupValueChangedListeners();

    field.multilineProperty().addListener((observable, oldValue, newValue) -> {
      node.setPrefHeight(newValue ? 80 : 0);
      readOnlyLabel.setPrefHeight(newValue ? 80 : 26);
    });

    field.errorMessagesProperty().addListener((observable, oldValue, newValue) ->
        toggleTooltip(field.isMultiline() ? editableArea : editableField)
    );

    editableField.focusedProperty().addListener(
        (observable, oldValue, newValue) -> toggleTooltip(editableField)
    );
    editableArea.focusedProperty().addListener(
        (observable, oldValue, newValue) -> toggleTooltip(editableArea)
    );
  }

}
