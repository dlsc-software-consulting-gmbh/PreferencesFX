package com.dlsc.preferencesfx;

import com.dlsc.preferencesfx.util.IncrementId;
import com.dlsc.preferencesfx.util.PreferencesFxUtils;
import java.util.Arrays;
import java.util.List;

public class Category {

  private final int id = IncrementId.get();
  private String description;
  private List<Group> groups;
  private List<Category> children;
  private CategoryPane categoryPane;

  /**
   * Creates a category without groups, for top-level categories without any settings.
   *
   * @param description Category name, for display in {@link CategoryTree}
   */
  private Category(String description) {
    this.description = description;
    this.categoryPane = new CategoryPane(null);
  }

  private Category(String description, Group... groups) {
    this.description = description;
    this.groups = Arrays.asList(groups);
    this.categoryPane = new CategoryPane(this.groups);
  }

  /**
   * Creates an empty category.
   * Can be used for top-level categories without {@link Setting}.
   *
   * @param description Category name, for display in {@link CategoryTree}
   * @return initialized Category object
   */
  public static Category of(String description) {
    return new Category(description);
  }

  /**
   * Creates a new category from groups.
   *
   * @param description Category name, for display in {@link CategoryTree}
   * @param groups      {@link Group} with {@link Setting} to be shown in the {@link CategoryPane}
   * @return initialized Category object
   */
  public static Category of(String description, Group... groups) {
    return new Category(description, groups);
  }

  /**
   * Creates a new category from settings, if the settings shouldn't be individually grouped.
   *
   * @param description Category name, for display in {@link CategoryTree}
   * @param settings    {@link Setting} to be shown in the {@link CategoryPane}
   * @return initialized Category object
   */
  public static Category of(String description, Setting... settings) {
    return new Category(description, Group.of(settings));
  }

  public Category subCategories(Category... children) {
    this.children = Arrays.asList(children);
    return this;
  }

  public void unmarkSettings() {
    if (getGroups() != null) {
      PreferencesFxUtils.groupsToSettings(getGroups())
          .forEach(Setting::unmark);
    }
  }

  public void unmarkGroups() {
    if (getGroups() != null) {
      getGroups().forEach(Group::unmark);
    }
  }

  public void unmarkAll() {
    unmarkGroups();
    unmarkSettings();
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

  public int getId() {
    return id;
  }
}
