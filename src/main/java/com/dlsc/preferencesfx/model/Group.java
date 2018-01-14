package com.dlsc.preferencesfx.model;

import com.dlsc.preferencesfx.formsfx.view.renderer.PreferencesFxGroup;
import com.dlsc.preferencesfx.util.Constants;
import java.util.Arrays;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * TODO: Add javadoc.
 */
public class Group {

  private static final Logger LOGGER =
      LogManager.getLogger(Group.class.getName());

  private static final String MARKED_STYLE_CLASS = "group-marked";
  private String description;
  private List<Setting> settings;
  private PreferencesFxGroup preferencesGroup;
  private boolean marked = false;
  private final EventHandler<MouseEvent> unmarker = event -> unmark();
  private final StringProperty breadcrumb = new SimpleStringProperty("");

  private Group(String description, Setting... settings) {
    this.description = description;
    this.settings = Arrays.asList(settings);
  }

  /**
   * TODO: Add javadoc.
   * @param description TODO: Add javadoc.
   * @param settings TODO: Add javadoc.
   * @return TODO: Add javadoc.
   */
  public static Group of(String description, Setting... settings) {
    return new Group(description, settings);
  }

  /**
   * TODO: Add javadoc.
   * @param settings TODO: Add javadoc.
   * @return TODO: Add javadoc.
   */
  public static Group of(Setting... settings) {
    return new Group(null, settings);
  }

  /**
   * TODO: Add javadoc.
   * @param description TODO: Add javadoc.
   * @return TODO: Add javadoc.
   */
  public Group description(String description) {
    this.description = description;
    return this;
  }

  /**
   * TODO: Add javadoc.
   * @return TODO: Add javadoc.
   */
  public String getDescription() {
    if (preferencesGroup != null) {
      return preferencesGroup.getTitle();
    }
    return description;
  }

  public List<Setting> getSettings() {
    return settings;
  }

  public PreferencesFxGroup getPreferencesGroup() {
    return preferencesGroup;
  }

  public void setPreferencesGroup(PreferencesFxGroup preferencesGroup) {
    this.preferencesGroup = preferencesGroup;
  }

  /**
   * TODO: Add javadoc.
   */
  public void mark() {
    // ensure it's not marked yet - so a control doesn't contain the same styleClass multiple times
    if (!marked) {
      preferencesGroup.getRenderer().addStyleClass(MARKED_STYLE_CLASS);
      preferencesGroup.getRenderer().getTitleLabel().setOnMouseExited(unmarker);
      marked = !marked;
    }
  }

  /**
   * TODO: Add javadoc.
   */
  public void unmark() {
    // check if it's marked before removing the style class
    if (marked) {
      preferencesGroup.getRenderer().removeStyleClass(MARKED_STYLE_CLASS);
      preferencesGroup.getRenderer().getTitleLabel().removeEventHandler(
          MouseEvent.MOUSE_EXITED, unmarker
      );
      marked = !marked;
    }
  }

  /**
   * TODO: Add javadoc.
   * @param breadCrumb TODO: Add javadoc.
   */
  public void addToBreadcrumb(String breadCrumb) {
    setBreadcrumb(breadCrumb + Constants.BREADCRUMB_DELIMITER + description);
    settings.forEach(setting -> setting.addToBreadcrumb(getBreadcrumb()));
  }

  public String getBreadcrumb() {
    return breadcrumb.get();
  }

  public StringProperty breadcrumbProperty() {
    return breadcrumb;
  }

  public void setBreadcrumb(String breadcrumb) {
    this.breadcrumb.set(breadcrumb);
  }
}
