package com.dlsc.preferencesfx.util;

import java.util.prefs.Preferences;

public class Constants {
  public static final String SELECTED_CATEGORY = "SELECTED_CATEGORY";

  public static final String DIVIDER_POSITION = "DIVIDER_POSITION";
  public static final String BREADCRUMB_DELIMITER = "#";
  public static final double DEFAULT_DIVIDER_POSITION = 0.2;
  public static final int DEFAULT_CATEGORY = 0;

  public static final int DEFAULT_PREFERENCES_WIDTH = 1000;
  public static final int DEFAULT_PREFERENCES_HEIGHT = 700;
  public static final int DEFAULT_PREFERENCES_POS_X = 700;
  public static final int DEFAULT_PREFERENCES_POS_Y = 500;

  public static final int SCROLLBAR_SUBTRACT = 20;

  public static final String WINDOW_WIDTH = "WINDOW_WIDTH";
  public static final String WINDOW_HEIGHT = "WINDOW_HEIGHT";
  public static final String WINDOW_POS_X = "WINDOW_POS_X";
  public static final String WINDOW_POS_Y = "WINDOW_POS_Y";

  /**
   * Calculates how long the breadcrumb part of the key to save for {@link Preferences} can be.
   * Since the hashcode method returns an int, the longest number is the one with a minus, so that
   * length is taken and is subtracted off the maximum key length of {@link Preferences}.
   */
  public static final int PREFERENCES_KEY_BREADCRUMB_TRIM
      = Preferences.MAX_KEY_LENGTH-(String.valueOf(Integer.MIN_VALUE).length());
}
