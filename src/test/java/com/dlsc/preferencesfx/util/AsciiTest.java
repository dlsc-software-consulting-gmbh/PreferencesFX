package com.dlsc.preferencesfx.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Test class for {@link Ascii}.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class AsciiTest {
  @Test
  public void testContainsIgnoreCase() throws Exception {
    assertTrue(Ascii.containsIgnoreCase("Favorites", "F"));
    assertTrue(Ascii.containsIgnoreCase("Favorites", "Fa"));
    assertFalse(Ascii.containsIgnoreCase("Favorites", "Fo"));
    assertTrue(Ascii.containsIgnoreCase("Font Size", "Fo"));
    assertTrue(Ascii.containsIgnoreCase("Font Size", "fo"));
    assertTrue(Ascii.containsIgnoreCase("Font Size", "fO"));
    assertTrue(Ascii.containsIgnoreCase("Font Size", "nt si"));
    assertFalse(Ascii.containsIgnoreCase("Scaling & Ordering", "awdawdhwhd"));
    assertTrue(Ascii.containsIgnoreCase("Scaling & Ordering", "ord"));
  }

}
