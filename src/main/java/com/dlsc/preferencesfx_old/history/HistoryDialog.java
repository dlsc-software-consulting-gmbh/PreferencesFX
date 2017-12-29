package com.dlsc.preferencesfx_old.history;

import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.stage.Modality;

/**
 * Created by François Martin on 03.12.2017.
 */
public class HistoryDialog extends DialogPane {

  private History history;
  private HistoryTable historyTable;

  private Dialog dialog = new Dialog();

  public HistoryDialog(History history) {
    this.history = history;
    historyTable = new HistoryTable(history.getChanges());
    layoutForm();
    setupClose();
    setupBindings();
    dialog.initModality(Modality.NONE);
    dialog.show();
  }

  private void layoutForm() {
    dialog.setTitle("PreferencesFX Undo / Redo History Debug View");
    dialog.setResizable(true);

    dialog.setDialogPane(this);
    setContent(historyTable);
  }

  private void setupBindings() {
    historyTable.addSelectionBinding(history.currentChangeProperty());
  }

  /**
   * Instantiates a close button and makes it invisible.
   * Dialog requires at least one button to close the dialog window.
   *
   * @see <a href="https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/Dialog.html">
   * javafx.scene.control.Dialog</a>
   * Chapter: Dialog Closing Rules
   */
  private void setupClose() {
    // TODO: refactor this into utils
    this.getButtonTypes().add(ButtonType.CLOSE);
    Node closeButton = dialog.getDialogPane().lookupButton(ButtonType.CLOSE);
    closeButton.managedProperty().bind(closeButton.visibleProperty());
    closeButton.setVisible(false);
  }

}
