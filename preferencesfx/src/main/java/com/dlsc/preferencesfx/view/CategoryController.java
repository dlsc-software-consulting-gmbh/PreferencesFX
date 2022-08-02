package com.dlsc.preferencesfx.view;

import static com.dlsc.preferencesfx.util.Constants.SCROLLBAR_SUBTRACT;

import com.dlsc.preferencesfx.model.Category;
import java.util.HashMap;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.ScrollPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Acts as a proxy for the CategoryViews.
 * Can be used to switch between different categories, which will be displayed in this view.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class CategoryController extends ScrollPane {
  private static final Logger LOGGER =
      LoggerFactory.getLogger(CategoryController.class.getName());

  private ObjectProperty<CategoryView> displayedCategoryView = new SimpleObjectProperty<>();
  private ObjectProperty<CategoryPresenter> displayedCategoryPresenter =
      new SimpleObjectProperty<>();

  private HashMap<Category, CategoryView> views = new HashMap<>();
  private HashMap<Category, CategoryPresenter> presenters = new HashMap<>();

  /**
   * Initializes the category controller.
   */
  public CategoryController() {
    // removes the border around the scrollpane
    setStyle("-fx-background-color:transparent;");
  }

  /**
   * Returns an already loaded presenter.
   *
   * @param category of the view / presenter
   * @return presenter object
   */
  public CategoryPresenter getPresenter(Category category) {
    return presenters.get(category);
  }

  /**
   * Adds a view / presenter pair to the respective HashMaps.
   *
   * @param category of the categoryView
   * @param view the view to add to the map
   * @param presenter the presenter to add to the map
   */
  public void addView(Category category, CategoryView view, CategoryPresenter presenter) {
    views.put(category, view);
    presenters.put(category, presenter);
  }

  /**
   * Sets the current view to the one of the respective category.
   * This method can be called in the presenter to switch to a different CategoryView.
   * Controls the way views are being transitioned from one to another.
   *
   * @param category of the categoryView that will be set
   * @return true if successfully loaded, false if view is nonexistent
   */
  public boolean setView(final Category category) {
    LOGGER.trace("CategoryController, setView: " + category);
    CategoryView categoryView = views.get(category);
    if (categoryView != null) { // view is loaded
      setContent(categoryView);
      // Binding for ScrollPane
      categoryView.minWidthProperty().bind(widthProperty().subtract(SCROLLBAR_SUBTRACT));
      displayedCategoryView.setValue(categoryView);
      displayedCategoryPresenter.setValue(getPresenter(category));
      return true;
    } else {
      LOGGER.info("Category " + category.getDescription() + " hasn't been loaded!");
      return false;
    }
  }

  /**
   * Unloads a view / presenter pair from the HashMaps.
   * This method can be used in case a view / presenter pair needs to be reloaded.
   *
   * @param category of the view / presenter pair to be unloaded.
   * @return true if the view and presenter were unloaded and false if view doesn't exist.
   */
  public boolean unloadView(Category category) {
    if (views.remove(category) == null | presenters.remove(category) == null) {
      LOGGER.info("Category " + category.getDescription() + " doesn't exist!");
      return false;
    } else {
      return true;
    }
  }

  /**
   * Sets the view according to the current category in categoryProperty.
   * Must ensure that the category is already loaded, else it will fail.
   *
   * @param categoryProperty with category to be listened for
   */
  public void addListener(ReadOnlyObjectProperty<Category> categoryProperty) {
    categoryProperty.addListener((observable, oldCategory, newCategory) -> {
      setView(newCategory);
    });
  }

  public CategoryView getDisplayedCategoryView() {
    return displayedCategoryView.get();
  }

  public ReadOnlyObjectProperty<CategoryView> displayedCategoryViewProperty() {
    return displayedCategoryView;
  }

  public CategoryPresenter getDisplayedCategoryPresenter() {
    return displayedCategoryPresenter.get();
  }

  public ReadOnlyObjectProperty<CategoryPresenter> displayedCategoryPresenterProperty() {
    return displayedCategoryPresenter;
  }
}
