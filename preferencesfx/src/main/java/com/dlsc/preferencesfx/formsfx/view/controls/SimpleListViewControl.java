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

import com.dlsc.formsfx.model.structure.MultiSelectionField;
import com.dlsc.preferencesfx.util.VisibilityProperty;
import javafx.collections.ListChangeListener;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;

/**
 * This class provides the base implementation for a simple control to edit
 * listview values.
 *
 * @author Sacha Schmid
 * @author Rinesch Murugathas
 * @author François Martin
 * @author Marco Sanfratello
 */
public class SimpleListViewControl<V>
    extends SimpleControl<MultiSelectionField<V>, ListView<String>> {

  /**
   * - The fieldLabel is the container that displays the label property of
   * the field.
   * - The node is the container that displays list values.
   */
  private Label fieldLabel;

  /**
   * The flag used for setting the selection properly.
   */
  private boolean preventUpdate;

  /**
   * Constructs a SimpleListViewControl of {@link SimpleListViewControl} type, with visibility condition.
   *
   * @param visibilityProperty - property for control visibility of this element
   *
   * @return the constructed SimpleListViewControl
   */
  public static SimpleListViewControl of(VisibilityProperty visibilityProperty) {
    SimpleListViewControl simpleListViewControl = new SimpleListViewControl();

    simpleListViewControl.visibilityProperty = visibilityProperty;

    return simpleListViewControl;
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeParts() {
    super.initializeParts();

    node = new ListView<>();
    node.getStyleClass().add("simple-listview-control");

    fieldLabel = new Label(field.labelProperty().getValue());

    node.setItems(field.getItems());
    node.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    for (int i = 0; i < field.getItems().size(); i++) {
      if (field.getSelection().contains(field.getItems().get(i))) {
        node.getSelectionModel().select(i);
      } else {
        node.getSelectionModel().clearSelection(i);
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void layoutParts() {
    node.setPrefHeight(200);
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

    field.itemsProperty().addListener(
        (observable, oldValue, newValue) -> node.setItems(field.getItems())
    );

    field.selectionProperty().addListener((observable, oldValue, newValue) -> {
      if (preventUpdate) {
        return;
      }

      preventUpdate = true;

      for (int i = 0; i < field.getItems().size(); i++) {
        if (field.getSelection().contains(field.getItems().get(i))) {
          node.getSelectionModel().select(i);
        } else {
          node.getSelectionModel().clearSelection(i);
        }
      }

      preventUpdate = false;
    });

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
    node.getSelectionModel().getSelectedIndices().addListener((ListChangeListener<Integer>) c -> {
      if (preventUpdate) {
        return;
      }

      preventUpdate = true;

      for (int i = 0; i < node.getItems().size(); i++) {
        if (node.getSelectionModel().getSelectedIndices().contains(i)) {
          field.select(i);
        } else {
          field.deselect(i);
        }
      }

      preventUpdate = false;
    });
  }

}
