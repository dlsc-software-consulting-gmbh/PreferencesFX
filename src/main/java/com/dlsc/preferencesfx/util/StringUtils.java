package com.dlsc.preferencesfx.util;

import com.google.common.base.Strings;

/**
 * Created by François Martin on 27.11.17.
 */
public class StringUtils {
  public static boolean containsIgnoreCase(String source, String match) {
    return !Strings.isNullOrEmpty(source)
        && !Strings.isNullOrEmpty(match)
        && source.toLowerCase().contains(match.toLowerCase());
  }
}
