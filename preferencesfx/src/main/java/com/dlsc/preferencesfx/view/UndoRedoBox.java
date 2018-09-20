package com.dlsc.preferencesfx.view;

import com.dlsc.preferencesfx.history.History;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.utils.FontAwesomeIconFactory;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

/**
 * Displays undo and redo buttons and also defines their behavior.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class UndoRedoBox extends HBox {

  Button undoBtn = FontAwesomeIconFactory.get().createIconButton(FontAwesomeIcon.UNDO);
  Button redoBtn = FontAwesomeIconFactory.get().createIconButton(FontAwesomeIcon.REPEAT);
  private History history;

  /**
   * Initializes the undo and redo buttons, sets their respective actions and disables them
   * if undo or redo is not available, respectively.
   *
   * @param history the history to be used for undo and redo
   */
  public UndoRedoBox(History history) {
    this.history = history;

    getChildren().addAll(undoBtn, redoBtn);

    undoBtn.setOnAction(event -> history.undo());
    undoBtn.disableProperty().bind(history.undoAvailableProperty().not());

    redoBtn.setOnAction(event -> history.redo());
    redoBtn.disableProperty().bind(history.redoAvailableProperty().not());
  }

}
