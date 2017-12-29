package com.dlsc.preferencesfx.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.dlsc.preferencesfx_old.util.StringUtils;
import org.junit.Test;

/**
 * Created by François Martin on 27.11.17.
 */
public class StringUtilsTest {
  @Test
  public void testContainsIgnoreCase() throws Exception {
    assertTrue(StringUtils.containsIgnoreCase("Favorites", "F"));
    assertTrue(StringUtils.containsIgnoreCase("Favorites", "Fa"));
    assertFalse(StringUtils.containsIgnoreCase("Favorites", "Fo"));
    assertTrue(StringUtils.containsIgnoreCase("Font Size", "Fo"));
    assertTrue(StringUtils.containsIgnoreCase("Font Size", "fo"));
    assertTrue(StringUtils.containsIgnoreCase("Font Size", "fO"));
    assertTrue(StringUtils.containsIgnoreCase("Font Size", "nt si"));
    assertFalse(StringUtils.containsIgnoreCase("Scaling & Ordering", "awdawdhwhd"));
    assertTrue(StringUtils.containsIgnoreCase("Scaling & Ordering", "ord"));
  }

}
