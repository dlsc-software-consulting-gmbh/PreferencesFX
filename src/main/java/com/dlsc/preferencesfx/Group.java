package com.dlsc.preferencesfx;

import com.dlsc.preferencesfx.util.formsfx.PreferencesGroup;
import java.util.Arrays;
import java.util.List;

/**
 * Created by François Martin on 07.11.17.
 */
public class Group {

  private static final String MARKED_STYLE_CLASS = "group-marked";
  private String description;
  private List<Setting> settings;
  private PreferencesGroup preferencesGroup;
  private boolean marked = false;

  private Group(String description, Setting... settings) {
    this.description = description;
    this.settings = Arrays.asList(settings);
  }

  public static Group of(String description, Setting... settings) {
    return new Group(description, settings);
  }

  public static Group of(Setting... settings) {
    return new Group(null, settings);
  }

  public Group description(String description) {
    this.description = description;
    return this;
  }

  public String getDescription() {
    return description;
  }

  public List<Setting> getSettings() {
    return settings;
  }

  public PreferencesGroup getPreferencesGroup() {
    return preferencesGroup;
  }

  public void setPreferencesGroup(PreferencesGroup preferencesGroup) {
    this.preferencesGroup = preferencesGroup;
  }

  public void mark() {
    // ensure it's not marked yet - so a control doesn't contain the same styleClass multiple times
    if (!marked) {
      /*preferencesGroup.().getRenderer().addStyleClass(MARKED_STYLE_CLASS);
      getField().getRenderer().setOnMouseExited(unmarker);*/ // TODO: how to add styleclass?
      marked = !marked;
    }
  }

  public void unmark() {
    // check if it's marked before removing the style class
    if (marked) {
      /*getField().getRenderer().removeStyleClass(MARKED_STYLE_CLASS);
      getField().getRenderer().removeEventHandler(MouseEvent.MOUSE_EXITED, unmarker);*/ // TODO: how to add styleclass?
      marked = !marked;
    }
  }
}
