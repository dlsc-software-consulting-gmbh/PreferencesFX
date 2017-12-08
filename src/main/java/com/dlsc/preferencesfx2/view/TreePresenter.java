package com.dlsc.preferencesfx2.view;

import com.dlsc.preferencesfx2.model.PreferencesModel;

public class TreePresenter {
  private PreferencesModel preferencesModel;
  private TreeView treeView;
  public TreePresenter(PreferencesModel preferencesModel, TreeView treeView) {
    this.preferencesModel = preferencesModel;
    this.treeView = treeView;
  }
}
