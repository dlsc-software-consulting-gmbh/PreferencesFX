package com.dlsc.preferencesfx.formsfx.view.renderer;

import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.view.util.ViewMixin;
import com.dlsc.preferencesfx.model.Category;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;

/**
 * Renders a {@link Form} for a {@link Category} in PreferencesFX.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class PreferencesFxFormRenderer extends GridPane implements ViewMixin {

  /**
   * SPACING is used to set the spacing of the group as well as the
   * spacing for vertical/horizontal gaps between controls.
   */
  public static final double SPACING = 5;

  private Form form;
  private List<PreferencesFxGroupRenderer> groups = new ArrayList<>();

  /**
   * This is the constructor to pass over data.
   *
   * @param form The form which gets rendered.
   */
  public PreferencesFxFormRenderer(Form form) {
    this.form = form;
    init();
  }

  @Override
  public String getUserAgentStylesheet() {
    return PreferencesFxFormRenderer.class.getResource("style.css").toExternalForm();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeParts() {
    groups = form.getGroups().stream().map(
        group -> new PreferencesFxGroupRenderer((PreferencesFxGroup) group, this)
    ).collect(Collectors.toList());
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
