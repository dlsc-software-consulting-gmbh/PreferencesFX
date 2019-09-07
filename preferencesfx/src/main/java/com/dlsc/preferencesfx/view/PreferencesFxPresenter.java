package com.dlsc.preferencesfx.view;

import com.dlsc.preferencesfx.model.PreferencesFxModel;
import javafx.stage.WindowEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Contains presenter logic of the {@link PreferencesFxView}.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class PreferencesFxPresenter implements Presenter {
  private static final Logger LOGGER =
      LoggerFactory.getLogger(PreferencesFxPresenter.class.getName());

  private PreferencesFxModel model;
  private PreferencesFxView preferencesFxView;

  /**
   * Constructs a new presenter for the {@link PreferencesFxView}.
   *
   * @param model             the model of PreferencesFX
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
    // As the scene is null here, listen to scene changes and make sure
    // that when the window is closed, the settings are saved beforehand.
    preferencesFxView.sceneProperty().addListener((observable, oldScene, newScene) -> {
      LOGGER.trace("new Scene: " + newScene);
      if (newScene != null && newScene.getWindow() != null) {
        LOGGER.trace("addEventHandler on Window close request to save settings");
        newScene.getWindow()
            .addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, event -> {
              LOGGER.trace("saveSettings because of WINDOW_CLOSE_REQUEST");
              model.saveSettings();
            });
      }
    });
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
