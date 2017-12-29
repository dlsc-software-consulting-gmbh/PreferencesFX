package com.dlsc.preferencesfx2.view;

/**
 * Created by François Martin on 29.12.17.
 */
public interface Presenter {

  /**
   * Calls all the other methods for easier initialization.
   */
  default void init() {
    setupEventHandlers();
    setupValueChangedListeners();
  }

  /**
   * This method is used to set up event handlers.
   */
  default void setupEventHandlers() {}

  /**
   * This method is used to set up value change listeners.
   */
  default void setupValueChangedListeners() {}


}
