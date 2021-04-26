package com.dlsc.preferencesfx.view;

import com.dlsc.preferencesfx.history.History;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material.Material;

/**
 * Displays undo and redo buttons and also defines their behavior.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class UndoRedoBox extends HBox {

  Button undoBtn = new Button();
  Button redoBtn = new Button();

  /**
   * Initializes the undo and redo buttons, sets their respective actions and disables them
   * if undo or redo is not available, respectively.
   *
   * @param history the history to be used for undo and redo
   */
  public UndoRedoBox(History history) {
    getChildren().addAll(undoBtn, redoBtn);

    undoBtn.setGraphic(new FontIcon(Material.UNDO));
    undoBtn.setOnAction(event -> history.undo());
    undoBtn.disableProperty().bind(history.undoAvailableProperty().not());

    redoBtn.setGraphic(new FontIcon(Material.REDO));
    redoBtn.setOnAction(event -> history.redo());
    redoBtn.disableProperty().bind(history.redoAvailableProperty().not());
  }

}
