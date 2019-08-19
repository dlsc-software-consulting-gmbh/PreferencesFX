package com.dlsc.preferencesfx.view;

import com.dlsc.preferencesfx.model.Category;
import com.dlsc.preferencesfx.model.PreferencesFxModel;
import javafx.geometry.Insets;
import javafx.scene.control.TreeView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.controlsfx.control.textfield.CustomTextField;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Displays a {@link TreeView} of all {@link Category} including a search bar.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class NavigationView extends VBox implements View {
  private static final Logger LOGGER =
      LoggerFactory.getLogger(NavigationView.class.getName());

  CustomTextField searchFld;
  TreeView<Category> treeView;
  FilterableTreeItem<Category> rootItem;
  private PreferencesFxModel model;

  /**
   * Constructs a new view, which displays the {@link TreeView} and the search bar.
   *
   * @param model the model of PreferencesFX
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
  protected void setSelectedItem(FilterableTreeItem<Category> categoryTreeItem) {
    if (categoryTreeItem != null) {
      treeView.getSelectionModel().select(categoryTreeItem);
    }
  }
}
