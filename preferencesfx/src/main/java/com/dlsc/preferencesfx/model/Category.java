package com.dlsc.preferencesfx.model;

import static com.dlsc.preferencesfx.util.Constants.BREADCRUMB_DELIMITER;

import com.dlsc.formsfx.model.util.TranslationService;
import com.dlsc.preferencesfx.util.PreferencesFxUtils;
import com.dlsc.preferencesfx.view.CategoryView;
import com.google.common.base.Strings;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Represents a category, which is used to structure one to multiple groups with settings in a page.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class Category {
  private static final Logger LOGGER = LogManager.getLogger(Category.class.getName());

  private StringProperty description = new SimpleStringProperty();
  private StringProperty descriptionKey = new SimpleStringProperty();
  private List<Group> groups;
  private List<Category> children;
  private final StringProperty breadcrumb = new SimpleStringProperty("");

  /**
   * Creates a category without groups, for top-level categories without any settings.
   *
   * @param description Category name, for display in {@link CategoryView}
   */
  private Category(String description) {
    descriptionKey.setValue(description);
    translate(null);
    setBreadcrumb(description);
  }

  private Category(String description, Group... groups) {
    this(description);
    this.groups = Arrays.asList(groups);
  }

  /**
   * Creates an empty category.
   * Can be used for top-level categories without {@link Setting}.
   *
   * @param description Category name, for display in {@link CategoryView}
   * @return initialized Category object
   */
  public static Category of(String description) {
    return new Category(description);
  }

  /**
   * Creates a new category from groups.
   *
   * @param description Category name, for display in {@link CategoryView}
   * @param groups      {@link Group} with {@link Setting} to be shown in the {@link CategoryView}
   * @return initialized Category object
   */
  public static Category of(String description, Group... groups) {
    return new Category(description, groups);
  }

  /**
   * Creates a new category from settings, if the settings shouldn't be individually grouped.
   *
   * @param description Category name, for display in {@link CategoryView}
   * @param settings    {@link Setting} to be shown in the {@link CategoryView}
   * @return initialized Category object
   */
  public static Category of(String description, Setting... settings) {
    return new Category(description, Group.of(settings));
  }

  /**
   * Adds subcategories to this category. Can be used to build up a hierarchical tree of Categories.
   *
   * @param children the subcategories to assign to this category
   * @return this object for chaining with the fluent API
   */
  public Category subCategories(Category... children) {
    this.children = Arrays.asList(children);
    return this;
  }
  
  /**
   * Creates and defines all of the breadcrumbs for the children
   */
  public void createBreadcrumbs() {
    createBreadcrumbs(getChildren());
  }

  /**
   * Creates and defines all of the breadcrumbs for all of the categories.
   *
   * @param categories the categories to create breadcrumbs for
   */
  public void createBreadcrumbs(List<Category> categories) {
    categories.forEach(category -> {
      category.setBreadcrumb(getBreadcrumb() + BREADCRUMB_DELIMITER + category.getDescription());
      if (!Objects.equals(category.getGroups(), null)) {
        category.getGroups().forEach(group -> group.addToBreadcrumb(getBreadcrumb()));
      }
      if (!Objects.equals(category.getChildren(), null)) {
        category.createBreadcrumbs();
      }
    });
  }

  /**
   * Unmarks all settings.
   *
   * Used for the search, which marks and unmarks items depending on the match
   * as a form of visual feedback.
   */
  public void unmarkSettings() {
    if (getGroups() != null) {
      PreferencesFxUtils.groupsToSettings(getGroups())
          .forEach(Setting::unmark);
    }
  }

  /**
   * Unmarks all groups.
   *
   * Used for the search, which marks and unmarks items depending on the match
   * as a form of visual feedback.
   */
  public void unmarkGroups() {
    if (getGroups() != null) {
      getGroups().forEach(Group::unmark);
    }
  }

  /**
   * Unmarks all settings and groups.
   *
   * Used for the search, which marks and unmarks items depending on the match
   * as a form of visual feedback.
   */
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

  /**
   * Updates the group descriptions when translation changes.
   */
  public void updateGroupDescriptions() {
    if (groups != null) {
      groups.forEach(group -> group.getPreferencesGroup().translate());
    }
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
    return breadcrumb.get();
  }

  public StringProperty breadcrumbProperty() {
    return breadcrumb;
  }

  public void setBreadcrumb(String breadcrumb) {
    this.breadcrumb.set(breadcrumb);
  }

  public ReadOnlyStringProperty descriptionProperty() {
    return description;
  }
}
