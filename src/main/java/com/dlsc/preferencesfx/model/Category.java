package com.dlsc.preferencesfx.model;

import com.dlsc.formsfx.model.util.TranslationService;
import com.dlsc.preferencesfx.util.Constants;
import com.dlsc.preferencesfx.util.PreferencesFxUtils;
import com.google.common.base.Strings;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Category {

  private static final Logger LOGGER =
      LogManager.getLogger(Category.class.getName());

  private StringProperty description = new SimpleStringProperty();
  private StringProperty descriptionKey = new SimpleStringProperty();
  private List<Group> groups;
  private List<Category> children;
  private String breadcrumb;

  /**
   * Creates a category without groups, for top-level categories without any settings.
   *
   * @param description Category name, for display in {@link }
   */
  private Category(String description) {
    descriptionKey.setValue(description);
    translate(null);
    breadcrumb = description;
  }

  private Category(String description, Group... groups) {
    this(description);
    this.groups = Arrays.asList(groups);
  }

  /**
   * Creates an empty category.
   * Can be used for top-level categories without {@link Setting}.
   *
   * @param description Category name, for display in {@link }
   * @return initialized Category object
   */
  public static Category of(String description) {
    return new Category(description);
  }

  /**
   * Creates a new category from groups.
   *
   * @param description Category name, for display in {@link }
   * @param groups      {@link Group} with {@link Setting} to be shown in the {@link }
   * @return initialized Category object
   */
  public static Category of(String description, Group... groups) {
    return new Category(description, groups);
  }

  /**
   * Creates a new category from settings, if the settings shouldn't be individually grouped.
   *
   * @param description Category name, for display in {@link }
   * @param settings    {@link Setting} to be shown in the {@link }
   * @return initialized Category object
   */
  public static Category of(String description, Setting... settings) {
    return new Category(description, Group.of(settings));
  }

  public Category subCategories(Category... children) {
    this.children = Arrays.asList(children);
    return this;
  }

  public void createBreadcrumbs(List<Category> categories) {
    categories.forEach(category -> {
      breadcrumb = breadcrumb + Constants.BREADCRUMB_DELIMITER + category.getDescription();
      if (!Objects.equals(category.getGroups(), null)) {
        category.getGroups().forEach(group -> group.addToBreadcrumb(breadcrumb));
      }
      if (!Objects.equals(category.getChildren(), null)) {
        createBreadcrumbs(category.getChildren());
      }
    });
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

  /**
   * This internal method is used as a callback for when the translation
   * service or its locale changes. Also applies the translation to all
   * contained sections.
   *
   * @see com.dlsc.formsfx.model.structure.Group ::translate
   */
  public void translate(TranslationService translationService) {
    if (translationService == null) {
      description.setValue(descriptionKey.getValue());
      return;
    }

    if (!Strings.isNullOrEmpty(descriptionKey.get())) {
      description.setValue(translationService.translate(descriptionKey.get()));
    }
  }

  public void updateGroupDescriptions() {
    groups.forEach(group -> group.getPreferencesGroup().translate());
  }

  public String getDescription() {
    return description.get();
  }

  public List<Group> getGroups() {
    return groups;
  }

  public List<Category> getChildren() {
    return children;
  }

  @Override
  public String toString() {
    return description.get();
  }

  public String getBreadcrumb() {
    return breadcrumb;
  }

  public void setBreadcrumb(String breadcrumb) {
    this.breadcrumb = breadcrumb;
  }

  public void setDescription(String description) {
    this.description.set(description);
  }


}
