package com.dlsc.preferencesfx;

import java.util.Arrays;
import java.util.List;

public class PreferencesFX {

  private List<Category> categories;

  PreferencesFX(Category[] categories) {
    this.categories = Arrays.asList(categories);
  }

  public static PreferencesFX of(Category... categories) {
    return new PreferencesFX(categories);
  }

}
