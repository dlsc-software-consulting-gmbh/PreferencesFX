package com.dlsc.preferencesfx.history;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

/**
 * Created by François Martin on 11.12.2017.
 */
public class HistoryButtonBox extends HBox {
  private History history;

  Button undoBtn = new Button("Undo");
  Button redoBtn = new Button("Redo");

  public HistoryButtonBox(History history) {
    this.history = history;
    getChildren().addAll(undoBtn, redoBtn);
    undoBtn.setOnAction(event -> history.undo());
    undoBtn.disableProperty().bind(history.undoAvailableProperty().not());
    redoBtn.setOnAction(event -> history.redo());
    redoBtn.disableProperty().bind(history.redoAvailableProperty().not());

  }

}
