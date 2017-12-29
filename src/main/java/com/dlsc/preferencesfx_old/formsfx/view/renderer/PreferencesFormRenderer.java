package com.dlsc.preferencesfx_old.formsfx.view.renderer;

import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.view.util.ViewMixin;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;

public class PreferencesFormRenderer extends GridPane implements ViewMixin {

  /**
   * SPACING is used to set the spacing of the group as well as the
   * spacing for vertical/horizontal gaps between controls.
   */
  public static final double SPACING = 5;

  private Form form;
  private List<PreferencesGroupRenderer> groups = new ArrayList<>();

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
    groups = form.getGroups().stream().map(
        g -> new PreferencesGroupRenderer((PreferencesGroup) g, this)).collect(Collectors.toList());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void layoutParts() {
    // Outer Padding of Category Pane
    setPadding(new Insets(SPACING * 3));
    setHgap(SPACING * 3);
    setVgap(SPACING * 2);
  }
}
