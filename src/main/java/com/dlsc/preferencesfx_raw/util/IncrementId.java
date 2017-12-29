package com.dlsc.preferencesfx_raw.util;

public class IncrementId {

  private static int id = 0;

  public static int get() {
    return id++;
  }
}
