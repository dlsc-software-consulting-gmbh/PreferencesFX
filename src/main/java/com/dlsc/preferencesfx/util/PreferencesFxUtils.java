package com.dlsc.preferencesfx.util;

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

}
