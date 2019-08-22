package com.dlsc.preferencesfx.util;

/**
 * Helper methods related to strings.
 */
class Ascii {

  /**
   * Indicates whether the character sequence {@code sequence} contains the {@code subSequence},
   * ignoring the case in the same manner as {@link String#equalsIgnoreCase(String)}.
   *
   * @param str       the string to search in
   * @param searchStr the string to search for
   */
  static boolean containsIgnoreCase(String str, String searchStr) {
    if (str == null || searchStr == null) {
      return false;
    }
    if (searchStr.isEmpty()) {
      return true;
    }

    final int len = searchStr.length();
    final int max = str.length() - len;
    for (int i = 0; i <= max; i++) {
      if (str.regionMatches(true, i, searchStr, 0, len)) {
        return true;
      }
    }
    return false;
  }
}
