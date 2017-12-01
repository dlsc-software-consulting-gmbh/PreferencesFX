package com.dlsc.preferencesfx.util;

import static org.junit.Assert.assertSame;

import org.junit.Ignore;
import org.junit.Test;

/**
 * Created by François Martin on 30.11.17.
 */
public class PreferencesFxUtilsTest {


  @Ignore
  @Test
  public void categoriesToSettings() throws Exception {
  }

  @Ignore
  @Test
  public void mapSettingsToCategories() throws Exception {
  }

  @Ignore
  @Test
  public void mapGroupsToCategories() throws Exception {
  }

  @Ignore
  @Test
  public void filterCategoriesByDescription() throws Exception {
  }

  @Ignore
  @Test
  public void filterSettingsByDescription() throws Exception {
  }

  @Ignore
  @Test
  public void groupsToSettings() throws Exception {
  }

  @Ignore
  @Test
  public void filterGroupsByDescription() throws Exception {
  }

  @Ignore
  @Test
  public void categoriesToGroups() throws Exception {
  }

  @Test
  public void testCompareMatches() throws Exception {
    String setting = new String("setting");
    String group = new String("group");
    String category = new String("category");
    int[] matches1 = {0, 0, 0, 0, 0, 0, 2, 2, 2, 3, 3, 3, 3, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3};
    int[] matches2 = {0, 2, 3, 0, 3, 0, 0, 2, 3, 0, 2, 3, 0, 3, 1, 2, 3, 1, 1, 2, 1, 2, 3, 1, 1, 1, 1, 1, 2, 1, 1, 2, 1, 2, 0, 0, 1, 2, 3, 0, 1, 2, 3, 0, 1, 2, 3, 0, 1, 2, 3, 0, 2, 3, 0, 2, 3, 0, 2, 3, 0, 3, 0, 3};
    int[] matches3 = {1, 1, 1, 2, 2, 3, 1, 1, 1, 1, 1, 1, 2, 2, 0, 0, 0, 1, 2, 2, 3, 3, 3, 0, 1, 2, 3, 0, 0, 1, 2, 2, 3, 3, 0, 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 0, 0, 0, 2, 2, 2, 3, 3, 3, 0, 0, 3, 3};
    Object[] expected = {category, category, category, category, category, category, category, category, category, category, category, category, category, category, group, group, group, group, group, group, group, group, group, group, group, group, group, group, group, group, group, group, group, group, null, setting, setting, setting, setting, setting, setting, setting, setting, setting, setting, setting, setting, setting, setting, setting, setting, setting, setting, setting, setting, setting, setting, setting, setting, setting, setting, setting, setting, setting};
    for (int i = 0; i < matches1.length; i++) {
      assertSame(
          "match1: " + matches1[i] + ", match2: " + matches2[i] + ", match3: " + matches3[i],
          expected[i],
          PreferencesFxUtils.compareMatches(setting, group, category, matches1[i], matches2[i], matches3[i])
      );
    }
  }

}
