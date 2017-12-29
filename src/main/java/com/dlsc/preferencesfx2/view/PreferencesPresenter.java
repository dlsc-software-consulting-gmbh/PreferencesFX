package com.dlsc.preferencesfx2.view;

import com.dlsc.preferencesfx2.model.PreferencesModel;

public class PreferencesPresenter implements Presenter {
  private PreferencesModel model;
  private PreferencesView preferenceView;

  public PreferencesPresenter(PreferencesModel model, PreferencesView preferenceView) {
    this.model = model;
    this.preferenceView = preferenceView;
    init();
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
    // Whenever the divider position is changed, it's position is saved.
    preferenceView.preferencesPane.dividerPositionProperty().addListener(
        (observable, oldValue, newValue) ->
            model.saveDividerPosition(
                preferenceView.preferencesPane.getDividerPosition()
            )
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setupBindings() {

  }
}
