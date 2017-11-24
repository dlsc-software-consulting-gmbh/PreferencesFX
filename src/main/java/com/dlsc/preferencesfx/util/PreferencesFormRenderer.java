package com.dlsc.preferencesfx.util;

import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.view.util.ViewMixin;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;

public class PreferencesFormRenderer extends VBox implements ViewMixin {

  private Form form;
  private List<PreferencesGroupRenderer> sections = new ArrayList<>();

  /**
   * This is the constructor to pass over data.
   *
   * @param form The form which gets rendered.
   */
  public PreferencesFormRenderer(Form form) {
    this.form = form;
    init();
  }

  @Override
  public String getUserAgentStylesheet() {
    return PreferencesFormRenderer.class.getResource("style.css").toExternalForm();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeParts() {
    sections = form.getGroups().stream().map(
        g -> new PreferencesGroupRenderer((PreferencesGroup) g)).collect(Collectors.toList());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void layoutParts() {
    // Outer Padding of Category Pane
    setPadding(new Insets(10));
    getChildren().addAll(sections);
  }
}
