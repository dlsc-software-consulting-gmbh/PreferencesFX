package com.dlsc.preferencesfx2.util;

import static com.dlsc.preferencesfx.util.StringUtils.containsIgnoreCase;

import com.dlsc.preferencesfx2.model.Category;
import com.dlsc.preferencesfx2.model.Group;
import com.dlsc.preferencesfx2.model.Setting;
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
   * Compares three objects with decreasing priority from the first to the last object.
   * {@see developer reference} for further information
   *
   * @param o1
   * @param o2
   * @param o3
   * @param match1
   * @param match2
   * @param match3
   * @param <T>
   * @return the object with the least amount of matches, taking into account the priority
   */
  public static <T> T compareMatches(T o1, T o2, T o3, int match1, int match2, int match3) {
    if (match1 == 0 && match2 == 0 && match3 == 0) { // if all values are 0
      return null;
    } else if (match1 == match2 && match1 == match3) { // if all values are equal to each other
      return o1;
    } else if (match1 == 1) {
      return o1;
    } else if (match2 == 1) {
      return o2;
    } else if (match3 == 1) {
      return o3;
    } else if (match1 != 0 && match2 == 0 && match3 == 0) {
      return o1;
    } else if (match1 == 0 && match2 != 0 && match3 == 0) {
      return o2;
    } else if (match1 == 0 && match2 == 0 && match3 != 0) {
      return o3;
    } else if (match1 == 0) {
      if (match3 < match2) {  // can only be match3, if it's smaller than match2
        return o3;
      } else {                // from here it can only be match2 if match1 is 0
        return o2;
      }
    } else if (match2 == 0) {
      if (match1 <= match3) { // can only be match1, if it's smaller or equal to match3
        return o1;
      } else {                // from here it can only be match3
        return o3;
      }
    } else if (match3 == 0) {
      if (match2 < match1) {  // can only be match2, if it's smaller than match1
        return o2;
      } else {                // from here it can only be match1
        return o1;
      } // from here, no more 0 or 1 values are present => comparisons can be made safely!
    } else if (match1 <= match2 && match1 <= match3) {
      return o1;
    } else if (match2 <= match3) {
      return o2;
    }
    return o3;
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

  public static List<Category> flattenCategories(List<Category> categoryLst){
    return categoryLst.stream()
        .flatMap(category -> category.getChildren().stream())
        .collect(Collectors.toList());
  }
}
