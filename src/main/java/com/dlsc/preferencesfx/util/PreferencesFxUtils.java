package com.dlsc.preferencesfx.util;

import static com.dlsc.preferencesfx.util.StringUtils.containsIgnoreCase;

import com.dlsc.preferencesfx.Category;
import com.dlsc.preferencesfx.Group;
import com.dlsc.preferencesfx.Setting;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
   * @param o1
   * @param o2
   * @param o3
   * @param match1
   * @param match2
   * @param match3
   * @param <T>
   * @return the object with the least amount of matches, taking into account the priority
   */
  public static <T> T compareThree(T o1, T o2, T o3, int match1, int match2, int match3){
    if (match1 == 1) {
      return o1;
    } else if (match2 == 1) {
      return o2;
    } else if (match3 == 1) {
      return o3;
    } else if (match2 == 0 && match3 == 0) {
      return o1;
    } else if (match1 == 0 && match3 == 0 || (match1 == 0 && match2 <= match3)) {
      return o2;
    } else if (((match2 == 0 && match1 <= match3) || (match3 == 0 && match1 <= match2) || (match1 <= match2 && match1 <= match3)) && (match1 != 0 && match2 != 0 && match3 != 0)) {
      return o1;
    }
    return null; // if match1, match2 and match3 are all 0
  }
}
