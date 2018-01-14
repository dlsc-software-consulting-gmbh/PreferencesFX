package com.dlsc.preferencesfx.view;

import com.dlsc.preferencesfx.model.Category;
import com.dlsc.preferencesfx.model.PreferencesFxModel;
import javafx.geometry.Insets;
import javafx.scene.control.TreeView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.textfield.CustomTextField;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;
import org.eclipse.fx.ui.controls.tree.FilterableTreeItem;

/**
 * TODO: Add javadoc.
 * @author François Martin
 * @author Marco Sanfratello
 */
public class NavigationView extends VBox implements View {
  private static final Logger LOGGER =
      LogManager.getLogger(NavigationView.class.getName());

  CustomTextField searchFld;
  TreeView<Category> treeView;
  FilterableTreeItem<Category> rootItem;
  private PreferencesFxModel model;

  /**
   * TODO: Add javadoc.
   * @param model TODO: Add javadoc.
   */
  public NavigationView(PreferencesFxModel model) {
    this.model = model;
    treeView = new TreeView<>();
    init();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeSelf() {

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeParts() {
    setupTextField();
    rootItem = new FilterableTreeItem<>(Category.of(""));
  }

  /**
   * Initializes the TextField and sets the search icon.
   */
  private void setupTextField() {
    searchFld = new CustomTextField();
    GlyphFont fontAwesome = GlyphFontRegistry.font("FontAwesome");
    Glyph glyph = fontAwesome.create(FontAwesome.Glyph.SEARCH).color(Color.GRAY);
    glyph.setPadding(new Insets(0, 3, 0, 5));
    searchFld.setLeft(glyph);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void layoutParts() {
    setVgrow(treeView, Priority.ALWAYS);
    getChildren().addAll(searchFld, treeView);

    treeView.setRoot(rootItem);
    // TreeSearchView requires a RootItem, but in this case it's not desired to have it visible.
    treeView.setShowRoot(false);
    treeView.getRoot().setExpanded(true);
    treeView.setStyle("-fx-background-color:transparent;"); // Remove border
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void bindFieldsToModel() {
    model.searchTextProperty().bind(searchFld.textProperty());
  }

  /**
   * Selects the given category TreeItem in the NavigationView.
   *
   * @param categoryTreeItem the category TreeItem to be selected
   */
  protected void setSelectedItem(FilterableTreeItem categoryTreeItem) {
    if (categoryTreeItem != null) {
      treeView.getSelectionModel().select(categoryTreeItem);
    }
  }
}
