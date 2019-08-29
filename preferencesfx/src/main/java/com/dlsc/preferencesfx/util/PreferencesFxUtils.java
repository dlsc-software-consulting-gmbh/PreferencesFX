package com.dlsc.preferencesfx.util;

import static com.dlsc.preferencesfx.util.Strings.containsIgnoreCase;

import com.dlsc.formsfx.model.structure.Element;
import com.dlsc.preferencesfx.model.Category;
import com.dlsc.preferencesfx.model.Group;
import com.dlsc.preferencesfx.model.Setting;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javafx.scene.layout.GridPane;

/**
 * Provides utility methods to do general transformations between different model objects of
 * PreferencesFX and or lists and maps of them.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class PreferencesFxUtils {

  /**
   * Returns a list of all the settings which are contained in a list of {@code categories}
   * recursively.
   *
   * @param categories the categories to fetch the settings from
   * @return all settings of the categories
   */
  public static List<Setting> categoriesToSettings(List<Category> categories) {
    return categories.stream()
        .map(Category::getGroups)     // get groups from categories
        .filter(Objects::nonNull)     // remove all null
        .flatMap(Collection::stream)  // recursively flatten all categories
        .map(Group::getSettings)      // get settings from groups
        .filter(Objects::nonNull)     // remove all null
        .flatMap(Collection::stream)  // recursively flatten all settings
        .collect(Collectors.toList());
  }

  /**
   * Returns a list of all the settings which are contained in a list of {@code categories}
   * recursively.
   *
   * @param categories the categories to fetch the elements from
   * @return all elements of the categories
   */
  public static List<Element> categoriesToElements(List<Category> categories) {
    return categories.stream()
        .map(Category::getGroups)     // get groups from categories
        .filter(Objects::nonNull)     // remove all null
        .flatMap(Collection::stream)  // recursively flatten all categories
        .map(Group::getSettings)      // get settings from groups
        .filter(Objects::nonNull)     // remove all null
        .flatMap(Collection::stream)  // recursively flatten all settings
        .map(Setting::getElement)
        .map(Element.class::cast)
        .collect(Collectors.toList());
  }

  /**
   * Creates a map which links {@link Setting} to {@link Category} in a list of {@code categories}.
   *
   * @param categories the categories of which to create a map of
   * @return a {@link HashMap} containing {@link Setting}, mapped to their
   *         corresponding {@link Category}
   * @apiNote does not flatten the categories
   */
  public static HashMap<Setting, Category> mapSettingsToCategories(List<Category> categories) {
    HashMap<Setting, Category> settingCategoryMap = new HashMap<>();
    for (Category category : categories) {
      if (category.getGroups() != null) {
        for (Group group : category.getGroups()) {
          if (group.getSettings() != null) {
            for (Setting setting : group.getSettings()) {
              settingCategoryMap.put(setting, category);
            }
          }
        }
      }
    }
    return settingCategoryMap;
  }

  /**
   * Creates a map which links {@link Group} to {@link Category} in a list of {@code categories}.
   *
   * @param categories the categories of which to create a map of
   * @return a {@link HashMap} containing {@link Group}, mapped to their
   *         corresponding {@link Category}
   * @apiNote does not flatten the categories
   */
  public static HashMap<Group, Category> mapGroupsToCategories(List<Category> categories) {
    HashMap<Group, Category> groupCategoryMap = new HashMap<>();
    for (Category category : categories) {
      if (category.getGroups() != null) {
        for (Group group : category.getGroups()) {
          groupCategoryMap.put(group, category);
        }
      }
    }
    return groupCategoryMap;
  }

  /**
   * Filters a list of {@code categories} by a given {@code description}.
   *
   * @param categories  the list of categories to be filtered
   * @param description to be searched for
   * @return a list of {@code categories}, containing (ignoring case) the given {@code description}
   */
  public static List<Category> filterCategoriesByDescription(List<Category> categories,
                                                             String description) {
    return categories.stream()
        .filter(category -> containsIgnoreCase(category.getDescription(), description))
        .collect(Collectors.toList());
  }

  /**
   * Filters a list of {@code settings} by a given {@code description}.
   *
   * @param settings    the list of settings to be filtered
   * @param description to be searched for
   * @return a list of {@code settings}, containing (ignoring case) the given {@code description}
   */
  public static List<Setting> filterSettingsByDescription(List<Setting> settings,
                                                          String description) {
    return settings.stream()
        .filter(setting -> containsIgnoreCase(setting.getDescription(), description))
        .collect(Collectors.toList());
  }

  /**
   * Returns a list of all the settings which are contained in a list of {@code groups}
   * recursively.
   *
   * @param groups the groups to fetch the settings from
   * @return all settings of the groups
   */
  public static List<Setting> groupsToSettings(List<Group> groups) {
    return groups.stream()
        .map(Group::getSettings)
        .flatMap(Collection::stream)
        .collect(Collectors.toList());
  }

  /**
   * Filters a list of {@code groups} by a given {@code description}.
   *
   * @param groups      the list of groups to be filtered
   * @param description to be searched for
   * @return a list of {@code groups}, containing (ignoring case) the given {@code description}
   */
  public static List<Group> filterGroupsByDescription(List<Group> groups, String description) {
    return groups.stream()
        .filter(group -> !Strings.isNullOrEmpty(group.getDescription()))
        .filter(group -> containsIgnoreCase(group.getDescription(), description))
        .collect(Collectors.toList());
  }

  /**
   * Returns a list of all the groups which are contained in a list of {@code categories}
   * recursively.
   *
   * @param categories the categories to fetch the groups from
   * @return all groups of the categories
   */
  public static List<Group> categoriesToGroups(List<Category> categories) {
    return categories.stream()
        .map(Category::getGroups)     // get groups from categories
        .filter(Objects::nonNull)     // remove all null
        .flatMap(Collection::stream)
        .collect(Collectors.toList());
  }

  /**
   * Returns the amount of rows of a given {@code gridPane}, if present, -1 else.
   *
   * @param gridPane the grid pane to count the rows in
   * @return the number of rows in the grid pane
   *     or {@code -1} to indicate no children in the grid pane
   */
  public static int getRowCount(GridPane gridPane) {
    return gridPane.getChildren().stream().mapToInt(n -> {
      Integer row = GridPane.getRowIndex(n);      // default: 0
      Integer rowSpan = GridPane.getRowSpan(n);   // default: 1
      return (row == null ? 0 : row) + (rowSpan == null ? 0 : rowSpan - 1);
    }).max().orElse(-1);
  }

  /**
   * Puts all categories and their respective children recursively flattened into a list.
   *
   * @param categoryLst list of parent categories
   * @return list of all parent categories and respective recursive children categories
   */
  public static List<Category> flattenCategories(List<Category> categoryLst) {
    if (categoryLst == null) {
      return null;
    }
    List<Category> flatCategoryLst = new ArrayList<>();
    flattenCategories(flatCategoryLst, categoryLst);
    return flatCategoryLst;
  }

  /**
   * Goes through the list of parent categories and adds each category it visists to the flatList.
   *
   * @param flatList   list to which the categories for the flattened list should be added
   * @param categories list of parent categories to go through recursively
   * @implNote Internal helper method for {@link PreferencesFxUtils#flattenCategories(List)}.
   */
  private static void flattenCategories(List<Category> flatList, List<Category> categories) {
    categories.forEach(category -> {
      flatList.add(category);
      List<Category> children = category.getChildren();
      if (children != null) {
        flattenCategories(flatList, children);
      }
    });
  }
}
