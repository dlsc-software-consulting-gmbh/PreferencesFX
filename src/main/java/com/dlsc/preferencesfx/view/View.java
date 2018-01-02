package com.dlsc.preferencesfx.view;

import java.util.List;

/**
 * Defines a view of PreferencesFX.
 */
public interface View {

  /**
   * Holds a list of stylesheets.
   *
   * @return list of stylesheets.
   */
  List<String> getStylesheets();

  /**
   * Calls all the other methods for easier initialization.
   */
  default void init() {
    initializeSelf();
    initializeParts();
    layoutParts();
    bindFieldsToModel();
  }

  /**
   * Initializes the view.
   */
  default void initializeSelf() {
  }

  /**
   * Initializes all parts of the view.
   */
  void initializeParts();

  /**
   * Defines the layout of all parts in the view.
   */
  void layoutParts();

  /**
   * Binds properties from the model to the view.
   */
  default void bindFieldsToModel() {
  }

  /**
   * Adds the stylesheet files to the getStylesheets method.
   *
   * @param stylesheetFile list of stylesheet files
   */
  default void addStylesheetFiles(String... stylesheetFile) {
    for (String file : stylesheetFile) {
      String stylesheet = getClass().getResource(file).toExternalForm();
      getStylesheets().add(stylesheet);
    }
  }

}
