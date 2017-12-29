package com.dlsc.preferencesfx2.view;

import com.dlsc.preferencesfx2.model.PreferencesModel;

public class PreferencesPresenter implements Presenter{
  private PreferencesModel preferencesModel;
  private PreferencesView preferenceView;

  public PreferencesPresenter(PreferencesModel preferencesModel, PreferencesView preferenceView) {
    this.preferencesModel = preferencesModel;
    this.preferenceView = preferenceView;
    init();
  }

  private void setupListeners() {
    // Whenever the divider position is changed, it's position is saved.
    preferenceView.preferencesPane.dividerPositionProperty().addListener(
        (observable, oldValue, newValue) ->
            preferencesModel.saveDividerPosition(
                preferenceView.preferencesPane.getDividerPosition()
            )
    );
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

  }
}
