package com.dlsc.preferencesfx;

import java.util.HashMap;
import java.util.Map;
import javafx.application.Application;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Example TreeView Filtering Application from: https://stackoverflow.com/questions/26072510/how-to-implement-filtering-for-treetableview
 */
@SuppressWarnings("all")
public class TreeTableViewFilterDemo extends Application {

  private TreeItem<Map<String, Object>> root;
  private TreeTableView<Map<String, Object>> tree;


  @Override
  public void start(Stage primaryStage) throws Exception {
    VBox outer = new VBox();

    TextField filter = new TextField();
    filter.textProperty().addListener((observable, oldValue, newValue) -> filterChanged(newValue));

    root = new TreeItem<>();
    tree = new TreeTableView<>(root);
    addColumn("Region", "region");
    addColumn("Type", "type");
    addColumn("Pop.", "population");
    setup();
    tree.setShowRoot(false);

    outer.getChildren().addAll(filter, tree);
    Scene scene = new Scene(outer, 640, 480);
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  private void filterChanged(String filter) {
    if (filter.isEmpty()) {
      tree.setRoot(root);
    }
    else {
      TreeItem<Map<String, Object>> filteredRoot = new TreeItem<>();
      filter(root, filter, filteredRoot);
      tree.setRoot(filteredRoot);
    }
  }


  private void filter(TreeItem<Map<String, Object>> root, String filter, TreeItem<Map<String, Object>> filteredRoot) {
    for (TreeItem<Map<String, Object>> child : root.getChildren()) {
      TreeItem<Map<String, Object>> filteredChild = new TreeItem<>();
      filteredChild.setValue(child.getValue());
      filteredChild.setExpanded(true);
      filter(child, filter, filteredChild );
      if (!filteredChild.getChildren().isEmpty() || isMatch(filteredChild.getValue(), filter)) {
        System.out.println(filteredChild.getValue() + " matches.");
        filteredRoot.getChildren().add(filteredChild);
      }
    }
  }

  private boolean isMatch(Map<String, Object> value, String filter) {
    return value.values().stream().anyMatch((Object o) -> o.toString().contains(filter));
  }

  private void setup() {
    TreeItem<Map<String, Object>> europe = createItem(root, "Europe", "continent", 742500000L);
    createItem(europe, "Germany", "country", 80620000L);
    TreeItem<Map<String, Object>> austria = createItem(europe, "Austria", "country", 847400L);
    createItem(austria, "Tyrol", "state", 728537L);
    TreeItem<Map<String, Object>> america = createItem(root, "America", "continent", 953700000L);
    createItem(america, "USA", "country", 318900000L);
    createItem(america, "Mexico", "country", 122300000L);
  }

  private TreeItem<Map<String, Object>> createItem(TreeItem<Map<String, Object>> parent, String region, String type, long population) {
    TreeItem<Map<String, Object>> item = new TreeItem<>();
    Map<String, Object> value = new HashMap<>();
    value.put("region",  region);
    value.put("type", type);
    value.put("population", population);
    item.setValue(value);
    parent.getChildren().add(item);
    item.setExpanded(true);
    return item;
  }

  protected void addColumn(String label, String dataIndex) {
    TreeTableColumn<Map<String, Object>, String> column = new TreeTableColumn<>(label);
    column.setPrefWidth(150);
    column.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<Map<String, Object>, String> param) -> {
          ObservableValue<String> result = new ReadOnlyStringWrapper("");
          if (param.getValue().getValue() != null) {
            result = new ReadOnlyStringWrapper("" + param.getValue().getValue().get(dataIndex));
          }
          return result;
        }
    );
    tree.getColumns().add(column);
  }


  public static void main(String[] args) {
    launch(args);
  }

}
