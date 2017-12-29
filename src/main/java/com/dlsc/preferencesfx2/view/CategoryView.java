package com.dlsc.preferencesfx2.view;

import com.dlsc.preferencesfx2.model.PreferencesModel;
import javafx.scene.layout.StackPane;

public class CategoryView extends StackPane implements View {
  private PreferencesModel preferencesModel;
  public CategoryView(PreferencesModel preferencesModel) {
    this.preferencesModel = preferencesModel;
    layoutParts();
  }

  private void layoutParts() {
    getChildren().add(preferencesModel.getCategories().get(0).getCategoryPane());
  }
}
