package com.dlsc.preferencesfx.view;

import com.dlsc.preferencesfx.model.PreferencesFxModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by François Martin on 29.12.17.
 */
public class TemplatePresenter implements Presenter {
  private static final Logger LOGGER =
      LogManager.getLogger(TemplatePresenter.class.getName());

  private PreferencesFxModel model;
  private TemplateView templateView;

  public TemplatePresenter(PreferencesFxModel model, TemplateView templateView) {
    this.model = model;
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
