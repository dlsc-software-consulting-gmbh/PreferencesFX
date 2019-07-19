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
import java.io.File;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

/**
 * This class provides the base implementation for a simple control to select or enter a directory
 * path.
 *
 * @author Rinesch Murugathas
 * @author Sacha Schmid
 * @author François Martin
 * @author Marco Sanfratello
 * @author Arvid Nyström
 */
public class SimpleChooserControl extends SimpleControl<StringField, StackPane> {

    /**
     * - The fieldLabel is the container that displays the label property of the field. - The
     * editableField allows users to modify the field's value. - The readOnlyLabel displays the
     * field's value if it is not editable.
     */
    private TextField editableField;
    private TextArea editableArea;
    private Label readOnlyLabel;
    private Label fieldLabel;
    private Button chooserButton = new Button();
    private HBox contentBox = new HBox();
    private String buttonText;
    private File initialDirectory;
    private boolean directory;

    /**
     * Create a new SimpleChooserControl.
     *
     * @param buttonText Text for the button to show, e.g. "Browse"
     * @param initialDirectory An optional initial path, can be null.
     *                         If null, will use the path from the previously
     *                         chosen file if present.
     * @param directory  true, if only directories are allowed
     */
    public SimpleChooserControl(String buttonText,
                                File initialDirectory,
                                boolean directory) {
        this.buttonText = buttonText;
        this.initialDirectory = initialDirectory;
        this.directory = directory;
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

        if (field.valueProperty().get().equals("null")) {
            field.valueProperty().set("");
        }

        chooserButton.setOnAction(event -> {
            File currentInitialDirectory = initialDirectory;
            boolean fileChosen = !field.valueProperty().get().trim().isEmpty();
            if (initialDirectory == null && fileChosen) {
                // define previously chosen path as initial directory
                currentInitialDirectory = new File(field.valueProperty().get());
            }

            File chosen;

            if (directory) {
                DirectoryChooser directoryChooser = new DirectoryChooser();
                directoryChooser.setInitialDirectory(currentInitialDirectory);
                chosen = directoryChooser.showDialog(getNode().getScene().getWindow());
            } else {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setInitialDirectory(currentInitialDirectory);
                chosen = fileChooser.showOpenDialog(getNode().getScene().getWindow());
            }

            if (chosen != null) {
                editableField.setText(chosen.getAbsolutePath());
            }
        });

        chooserButton.setText(buttonText);

        StackPane fieldStackPane = new StackPane();
        fieldStackPane.getChildren().addAll(editableField, editableArea, readOnlyLabel);
        fieldStackPane.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(fieldStackPane, Priority.ALWAYS);

        contentBox.getChildren().addAll(fieldStackPane, chooserButton);
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

        node.getChildren().add(contentBox);

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