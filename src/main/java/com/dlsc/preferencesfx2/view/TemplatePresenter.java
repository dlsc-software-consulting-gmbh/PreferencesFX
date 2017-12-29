package com.dlsc.preferencesfx2.view;

import com.dlsc.preferencesfx2.model.PreferencesModel;

/**
 * Created by François Martin on 29.12.17.
 */
public class TemplatePresenter implements Presenter {

  private PreferencesModel preferencesModel;
  private TemplateView templateView;

  public TemplatePresenter(PreferencesModel preferencesModel, TemplateView templateView) {
    this.preferencesModel = preferencesModel;
    this.templateView = templateView;
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

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setupBindings() {

  }

}
