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

import com.dlsc.formsfx.model.structure.IntegerField;
import com.dlsc.preferencesfx.util.VisibilityProperty;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;

/**
 * This class provides a specific implementation to edit integer values.
 *
 * @author Rinesch Murugathas
 * @author Sacha Schmid
 * @author François Martin
 * @author Marco Sanfratello
 */
public class SimpleIntegerControl extends SimpleNumberControl<IntegerField, Integer> {

  public SimpleIntegerControl() {
  }

  /**
   * Constructs a SimpleIntegerControl of {@link SimpleIntegerControl} type, with visibility condition.
   *
   * @param visibilityProperty - property for control visibility of this element
   *
   * @return the constructed SimpleIntegerControl
   */
  public static SimpleIntegerControl of(VisibilityProperty visibilityProperty) {
    SimpleIntegerControl simpleIntegerControl = new SimpleIntegerControl();

    simpleIntegerControl.visibilityProperty = visibilityProperty;

    return simpleIntegerControl;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeParts() {
    super.initializeParts();

    getStyleClass().addAll("simple-integer-control");
    final SpinnerValueFactory.IntegerSpinnerValueFactory factory =
        new SpinnerValueFactory.IntegerSpinnerValueFactory(
            Integer.MIN_VALUE, Integer.MAX_VALUE, field.getValue()
        );

    // override old converter (IntegerStringConverter) because it throws
    // NumberFormatException if value can not be parsed to Integer
    factory.setConverter(new NoExceptionStringConverter());
    editableSpinner.setValueFactory(factory);
    editableSpinner.focusedProperty().addListener((observable, wasFocused, isFocused) -> {
      if (wasFocused && !isFocused) {
        overrideNonIntegerSpinnerValues();
      }
    });
    editableSpinner.addEventHandler(KeyEvent.ANY, event -> {
      if (event.getCode() == KeyCode.ENTER) {
        overrideNonIntegerSpinnerValues();
      }
    });
  }

  private void overrideNonIntegerSpinnerValues() {
    try {
      Integer.parseInt(editableSpinner.getEditor().getText());
    } catch (NumberFormatException ex) {
      editableSpinner.getEditor().setText("0");
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setupValueChangedListeners() {
    super.setupValueChangedListeners();

    field.errorMessagesProperty().addListener(
        (observable, oldValue, newValue) -> toggleTooltip(editableSpinner)
    );
    field.tooltipProperty().addListener(
        (observable, oldValue, newValue) -> toggleTooltip(editableSpinner)
    );
  }

  private class NoExceptionStringConverter extends StringConverter<Integer> {

    @Override
    public String toString(Integer object) {
      try {
        return new IntegerStringConverter().toString(object);
      } catch (NumberFormatException ex) {
        return "0";
      }
    }

    @Override
    public Integer fromString(String string) {
      try {
        return new IntegerStringConverter().fromString(string);
      } catch (NumberFormatException ex) {
        return 0;
      }
    }
  }
}
