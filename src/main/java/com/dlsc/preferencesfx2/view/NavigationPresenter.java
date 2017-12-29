package com.dlsc.preferencesfx2.view;

import com.dlsc.preferencesfx2.model.PreferencesModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NavigationPresenter implements Presenter {
  private static final Logger LOGGER =
      LogManager.getLogger(NavigationPresenter.class.getName());

  private PreferencesModel preferencesModel;
  private NavigationView treeSearchView;
  private NavigationView treeSearchView;
  public NavigationPresenter(PreferencesModel preferencesModel, NavigationView treeSearchView) {
    this.preferencesModel = preferencesModel;
    this.treeSearchView = treeSearchView;
    this.treeSearchView = treeSearchView.getCategoryTreeView();
  }

}
