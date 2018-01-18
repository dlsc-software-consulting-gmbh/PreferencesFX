package com.dlsc.preferencesfx.view;

import com.dlsc.preferencesfx.model.PreferencesFxModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Contains presenter logic of the {@link PreferencesFxView}.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class PreferencesFxPresenter implements Presenter {
  private static final Logger LOGGER =
      LogManager.getLogger(PreferencesFxPresenter.class.getName());

  private PreferencesFxModel model;
  private PreferencesFxView preferencesFxView;

  /**
   * Constructs a new presenter for the {@link PreferencesFxView}.
   *
   * @param model the model of PreferencesFX
   * @param preferencesFxView corresponding view to this presenter
   */
  public PreferencesFxPresenter(PreferencesFxModel model, PreferencesFxView preferencesFxView) {
    this.model = model;
    this.preferencesFxView = preferencesFxView;
    init();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeViewParts() {

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setupEventHandlers() {

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setupValueChangedListeners() {
    // When the displayedCategory in the model changes, set the view in the CategoryController
    preferencesFxView.categoryController.addListener(model.displayedCategoryProperty());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setupBindings() {
    // Binds the dividerPosition to the divider position in the model.
    preferencesFxView.preferencesPane.dividerPositionProperty()
        .bindBidirectional(model.dividerPositionProperty());
  }
}
