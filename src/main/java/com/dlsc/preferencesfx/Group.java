package com.dlsc.preferencesfx;

import java.util.Arrays;
import java.util.List;

/**
 * Created by François Martin on 07.11.17.
 */
public class Group {

  private String description;
  private List<Setting> settings;

  Group(Setting... settings) {
    this.settings = Arrays.asList(settings);
  }

  public static Group of(Setting... settings) {
    return new Group(settings);
  }

  public Group description(String description){
    this.description = description;
    return this;
  }

  public String getDescription() {
    return description;
  }

  public List<Setting> getSettings() {
    return settings;
  }
}
