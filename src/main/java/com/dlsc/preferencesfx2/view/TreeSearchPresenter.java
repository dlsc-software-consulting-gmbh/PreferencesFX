package com.dlsc.preferencesfx2.view;

import com.dlsc.preferencesfx2.model.PreferencesModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TreeSearchPresenter {
  private static final Logger LOGGER =
      LogManager.getLogger(TreeSearchPresenter.class.getName());

  private PreferencesModel preferencesModel;
  private TreeSearchView treeSearchView;
  private TreeSearchView treeSearchView;
  public TreeSearchPresenter(PreferencesModel preferencesModel, TreeSearchView treeSearchView) {
    this.preferencesModel = preferencesModel;
    this.treeSearchView = treeSearchView;
    this.treeSearchView = treeSearchView.getCategoryTreeView();
  }

}
