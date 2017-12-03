package com.dlsc.preferencesfx.history;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * Created by François Martin on 03.12.2017.
 */
public class HistoryPane extends VBox {

  History history;

  Button undoBtn = new Button("Undo");
  Button redoBtn = new Button("Redo");

  HBox btnBox = new HBox(undoBtn, redoBtn);

  HistoryTable historyTable;

  public HistoryPane(History history) {
    this.history = history;
    historyTable = new HistoryTable(history.getChanges());
    getChildren().addAll(btnBox, historyTable);
    setVgrow(historyTable, Priority.ALWAYS);
    undoBtn.setOnAction(event -> history.undo());
    undoBtn.disableProperty().bind(history.undoAvailableProperty().not());
    redoBtn.setOnAction(event -> history.redo());
    redoBtn.disableProperty().bind(history.redoAvailableProperty().not());

  }

}
