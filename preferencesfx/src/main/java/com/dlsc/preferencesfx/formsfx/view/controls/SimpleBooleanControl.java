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

import com.dlsc.formsfx.model.structure.BooleanField;
import com.dlsc.preferencesfx.util.VisibilityProperty;
import javafx.scene.control.CheckBox;

/**
 * This class provides the base implementation for a simple control to edit
 * boolean values.
 *
 * @author Rinesch Murugathas
 * @author Sacha Schmid
 * @author François Martin
 * @author Marco Sanfratello
 */
public class SimpleBooleanControl extends SimpleControl<BooleanField, CheckBox> {

  /**
   * Constructs a SimpleBooleanControl of {@link SimpleBooleanControl} type, with visibility condition.
   *
   * @param visibilityProperty - property for control visibility of this element
   *
   * @return the constructed SimpleBooleanControl
   */
  public static SimpleBooleanControl of(VisibilityProperty visibilityProperty) {
    SimpleBooleanControl simpleBooleanControl = new SimpleBooleanControl();

    simpleBooleanControl.visibilityProperty = visibilityProperty;

    return simpleBooleanControl;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeParts() {
    super.initializeParts();

    node = new CheckBox();
    node.getStyleClass().add("simple-boolean-control");
    node.setSelected(field.getValue());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void layoutParts() {
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
    field.userInputProperty().addListener((observable, oldValue, newValue) ->
        node.setSelected(Boolean.parseBoolean(field.getUserInput()))
    );

    field.errorMessagesProperty().addListener(
        (observable, oldValue, newValue) -> toggleTooltip(node)
    );
    field.tooltipProperty().addListener((observable, oldValue, newValue) -> toggleTooltip(node));

    node.focusedProperty().addListener((observable, oldValue, newValue) -> toggleTooltip(node));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setupEventHandlers() {
    node.selectedProperty().addListener((observable, oldValue, newValue) ->
        field.userInputProperty().setValue(String.valueOf(newValue))
    );
  }

}
