package com.dlsc.preferencesfx.util;

import static com.dlsc.preferencesfx.util.StringUtils.containsIgnoreCase;

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
 * Created by François Martin on 29.11.17.
 */
public class PreferencesFxUtils {
  public static List<Setting> categoriesToSettings(List<Category> categories) {
    return categories.stream()
        .map(Category::getGroups)     // get groups from categories
        .filter(Objects::nonNull)     // remove all null
        .flatMap(Collection::stream)
        .map(Group::getSettings)      // get settings from groups
        .filter(Objects::nonNull)     // remove all null
        .flatMap(Collection::stream)
        .collect(Collectors.toList());
  }

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

  public static List<Category> filterCategoriesByDescription(List<Category> categories,
                                                             String description) {
    return categories.stream()
        .filter(category -> containsIgnoreCase(category.getDescription(), description))
        .collect(Collectors.toList());
  }

  public static List<Setting> filterSettingsByDescription(List<Setting> settings,
                                                          String description) {
    return settings.stream()
        .filter(setting -> containsIgnoreCase(setting.getDescription(), description))
        .collect(Collectors.toList());
  }

  public static List<Setting> groupsToSettings(List<Group> groups) {
    return groups.stream()
        .map(Group::getSettings)
        .flatMap(Collection::stream)
        .collect(Collectors.toList());
  }

  public static List<Group> filterGroupsByDescription(List<Group> groups, String description) {
    return groups.stream()
        .filter(group -> containsIgnoreCase(group.getDescription(), description))
        .collect(Collectors.toList());
  }

  public static List<Group> categoriesToGroups(List<Category> categories) {
    return categories.stream()
        .map(Category::getGroups)     // get groups from categories
        .filter(Objects::nonNull)     // remove all null
        .flatMap(Collection::stream)
        .collect(Collectors.toList());
  }

  /**
   * Gets the amount of rows of a given GridPane.
   *
   * @return the amount of rows, if present, -1 else
   */
  public static int getRowCount(GridPane grid) {
    return grid.getChildren().stream().mapToInt(n -> {
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
   * Internal helper method for {@link PreferencesFxUtils#flattenCategories(List)}.
   * Goes through the list of parent categories and adds each category it visists to the flatList.
   *
   * @param flatList   list to which the categories for the flattened list should be added
   * @param categories list of parent categories to go through recursively
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
