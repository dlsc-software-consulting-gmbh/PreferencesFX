package com.dlsc.preferencesfx.util;

public class IncrementId {

  private static int id = 0;

  public static int get() {
    return id++;
  }
}
