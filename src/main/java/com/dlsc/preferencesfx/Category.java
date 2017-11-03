package com.dlsc.preferencesfx;

import com.dlsc.preferencesfx.pages.CategoryPane;
import java.util.Arrays;
import java.util.List;

public class Category {

  private String description;
  private List<Setting> settings;
  private List<Category> children;
  private CategoryPane page;

  Category(String description, Setting... settings) {
    this.description = description;
    this.settings = Arrays.asList(settings);
    this.page = new CategoryPane(settings);
  }

  public static Category of(String description, Setting... settings) {
    return new Category(description, settings);
  }

  public Category withSubCategories(Category... children) {
    this.children = Arrays.asList(children);
    return this;
  }

  public CategoryPane getPage() {
    return page;
  }

  public List<Setting> getSettings() {
    return settings;
  }
}
