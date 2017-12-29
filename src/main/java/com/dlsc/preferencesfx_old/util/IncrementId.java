package com.dlsc.preferencesfx_old.util;

public class IncrementId {

  private static int id = 0;

  public static int get() {
    return id++;
  }
}
