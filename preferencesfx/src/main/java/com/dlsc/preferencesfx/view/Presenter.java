package com.dlsc.preferencesfx.view;

/**
 * Defines a presenter of PreferencesFX.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public interface Presenter {

  /**
   * Calls all the other methods for easier initialization.
   */
  default void init() {
    initializeViewParts();
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
