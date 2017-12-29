package com.dlsc.preferencesfx2.view;

import com.dlsc.preferencesfx2.model.Category;
import java.util.HashMap;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.StackPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Acts as a proxy for the CategoryViews.
 * Can be used to switch between different categories, which will be displayed in this view.
 */
public class CategoryController extends StackPane {

  private static final Logger LOGGER =
      LogManager.getLogger(CategoryController.class.getName());

  private ObjectProperty<CategoryView> displayedCategoryView = new SimpleObjectProperty<>();
  private ObjectProperty<CategoryPresenter> displayedCategoryPresenter = new SimpleObjectProperty<>();

  private HashMap<Category, CategoryView> views = new HashMap<>();
  private HashMap<Category, CategoryPresenter> presenters = new HashMap<>();

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
      // If at least one view is already being displayed
      if (!getChildren().isEmpty()) {
        //remove displayed view
        getChildren().clear();
        //add new view
        getChildren().add(categoryView);
      } else {
        // No view is currently being displayed, so just add it
        getChildren().add(categoryView);
      }
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
