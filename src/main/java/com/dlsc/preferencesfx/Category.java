package com.dlsc.preferencesfx;

import java.util.Arrays;
import java.util.List;

public class Category {

  private String description;
  private List<Group> groups;
  private List<Category> children;
  private CategoryPane categoryPane;

  private Category(String description, Group... groups) {
    this.description = description;
    this.groups = Arrays.asList(groups);
    this.categoryPane = new CategoryPane(this.groups);
  }

  public static Category of(String description, Group... groups) {
    return new Category(description, groups);
  }

  public Category subCategories(Category... children) {
    this.children = Arrays.asList(children);
    return this;
  }

  public CategoryPane getCategoryPane() {
    return categoryPane;
  }

  public String getDescription() {
    return description;
  }

  public List<Group> getGroups() {
    return groups;
  }

  public List<Category> getChildren() {
    return children;
  }

  @Override
  public String toString() {
    return description;
  }
}
