package com.dlsc.preferencesfx.history.view;

import com.dlsc.preferencesfx.history.Change;
import com.dlsc.preferencesfx.history.History;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * Used for debugging purposes only.
 * Represents a TableView of {@link Change} being shown in the dialog to debug the {@link History}.
 * @author François Martin
 * @author Marco Sanfratello
 */
public class HistoryTable extends TableView<Change> {

  ObservableList<Change> changes;

  /**
   * Initializes a new table, showing the {@code changes}.
   * @param changes to be shown in the table
   */
  public HistoryTable(ObservableList<Change> changes) {
    this.changes = changes;

    TableColumn<Change, String> timestamp = new TableColumn<>("Time");
    timestamp.setCellValueFactory(
        change -> new ReadOnlyStringWrapper(change.getValue().getTimestamp())
    );

    TableColumn<Change, String> breadcrumb = new TableColumn<>("Setting");
    breadcrumb.setCellValueFactory(
        change -> new ReadOnlyStringWrapper(change.getValue().getSetting().toString())
    );

    TableColumn<Change, Object> oldValue = new TableColumn<>("Old Value");
    oldValue.setCellValueFactory(change -> change.getValue().oldListProperty());

    TableColumn<Change, Object> newValue = new TableColumn<>("New Value");
    newValue.setCellValueFactory(change -> change.getValue().newListProperty());

    setItems(this.changes);
    getColumns().addAll(timestamp, breadcrumb, oldValue, newValue);
  }

  /**
   * Updates the selection in the table whenever the current {@link Change}
   * in the {@link History} changes.
   * @param currentChange the property which contains the current {@link Change}
   *                      in the {@link History}
   */
  void addSelectionBinding(ReadOnlyObjectProperty<Change> currentChange) {
    currentChange.addListener(
        (observable, oldValue, newValue) -> getSelectionModel().select(newValue)
    );
  }

}
