package com.dlsc.preferencesfx2.view;

import com.dlsc.preferencesfx2.model.PreferencesModel;
import javafx.scene.layout.Pane;

/**
 * Created by François Martin on 29.12.17.
 */
public class TemplateView extends Pane implements View {

  private PreferencesModel preferencesModel;

  public TemplateView(PreferencesModel preferencesModel) {
    this.preferencesModel = preferencesModel;
    init();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeSelf() {

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeParts() {

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void layoutParts() {

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void bindFieldsToModel() {

  }

}
