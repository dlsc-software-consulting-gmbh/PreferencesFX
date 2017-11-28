package com.dlsc.preferencesfx.util;

/**
 * Created by François Martin on 27.11.17.
 */
public class StringUtils {
  public static boolean containsIgnoreCase(String source, String match) {
    return source.toLowerCase().contains(match.toLowerCase());
  }
}
