package com.dlsc.preferencesfx2.view;

import com.dlsc.preferencesfx2.model.PreferencesModel;

public class PreferencePresenter {
  private PreferencesModel preferencesModel;
  private PreferenceView preferenceView;

  public PreferencePresenter(PreferencesModel preferencesModel, PreferenceView preferenceView) {
    this.preferencesModel = preferencesModel;
    this.preferenceView = preferenceView;
  }
}
