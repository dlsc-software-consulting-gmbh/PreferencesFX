package com.dlsc.preferencesfx;

import java.util.Arrays;
import java.util.List;

public class Category {

  private String description;
  private List<Setting> settings;
  private List<Category> children;

  Category(String description, Setting... settings) {
    this.description = description;
    this.settings = Arrays.asList(settings);
  }

  public static Category of(String description, Setting... settings) {
    return new Category(description, settings);
  }

  public Category withSubCategories(Category... children) {
    this.children = Arrays.asList(children);
    return this;
  }

}
