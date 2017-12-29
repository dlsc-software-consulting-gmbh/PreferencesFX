package com.dlsc.preferencesfx2.view;

import com.dlsc.preferencesfx2.model.PreferencesModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NavigationPresenter implements Presenter {
  private static final Logger LOGGER =
      LogManager.getLogger(NavigationPresenter.class.getName());

  private PreferencesModel preferencesModel;
  private NavigationView navigationView;

  public NavigationPresenter(PreferencesModel preferencesModel, NavigationView navigationView) {
    this.preferencesModel = preferencesModel;
    this.navigationView = navigationView;
    this.navigationView = navigationView.getCategoryTreeView();
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

}
