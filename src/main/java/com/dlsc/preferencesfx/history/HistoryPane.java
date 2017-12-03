package com.dlsc.preferencesfx.history;

import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

/**
 * Created by François Martin on 03.12.2017.
 */
public class HistoryPane extends VBox {

  History history;

  Button undoBtn = new Button("Undo");
  Button redoBtn = new Button("Redo");

  public HistoryPane(History history) {
    this.history = history;
    getChildren().addAll(undoBtn, redoBtn);
    undoBtn.setOnAction(event -> history.undo());
    undoBtn.disableProperty().bind(history.undoAvailableProperty().not());
    redoBtn.setOnAction(event -> history.redo());
    redoBtn.disableProperty().bind(history.redoAvailableProperty().not());
  }

}
