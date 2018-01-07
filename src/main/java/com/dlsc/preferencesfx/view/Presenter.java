package com.dlsc.preferencesfx.view;

/**
 * Created by François Martin on 29.12.17.
 */
public interface Presenter {

  /**
   * Calls all the other methods for easier initialization.
   */
  default void init() {
    initializeViewParts();
    setupListeners();
    setupEventHandlers();
    setupValueChangedListeners();
    setupBindings();
  }

  /**
   * Initializes parts of the view which require more logic.
   */
  default void initializeViewParts() {
  }

  /**
   * Sets up listeners of the view.
   */
  default void setupListeners() {
  }

  /**
   * Sets up event handlers of the view.
   */
  default void setupEventHandlers() {
  }

  /**
   * Adds all listeners to view elements and model properties.
   */
  default void setupValueChangedListeners() {
  }

  /**
   * Sets up bindings of the view.
   */
  default void setupBindings() {
  }

}
