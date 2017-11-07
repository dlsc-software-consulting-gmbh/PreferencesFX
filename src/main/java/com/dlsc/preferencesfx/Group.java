package com.dlsc.preferencesfx;

/**
 * Created by François Martin on 07.11.17.
 */
public class Group {

  private String description;
  private Setting[] settings;

  Group(Setting... settings) {
    this.settings = settings;
  }

  public static Group of(Setting... settings) {
    return new Group(settings);
  }

  public Group description(String description){
    this.description = description;
  }

  public String getDescription() {
    return description;
  }

  public Setting[] getSettings() {
    return settings;
  }
}
