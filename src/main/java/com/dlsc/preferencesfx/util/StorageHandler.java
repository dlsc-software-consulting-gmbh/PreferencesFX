package com.dlsc.preferencesfx.util;

import static com.dlsc.preferencesfx.PreferencesFx.DEFAULT_CATEGORY;
import static com.dlsc.preferencesfx.PreferencesFx.DEFAULT_DIVIDER_POSITION;
import static com.dlsc.preferencesfx.PreferencesFx.DEFAULT_PREFERENCES_HEIGHT;
import static com.dlsc.preferencesfx.PreferencesFx.DEFAULT_PREFERENCES_POS_X;
import static com.dlsc.preferencesfx.PreferencesFx.DEFAULT_PREFERENCES_POS_Y;
import static com.dlsc.preferencesfx.PreferencesFx.DEFAULT_PREFERENCES_WIDTH;
import static com.dlsc.preferencesfx.PreferencesFx.DIVIDER_POSITION;
import static com.dlsc.preferencesfx.PreferencesFx.SELECTED_CATEGORY;
import static com.dlsc.preferencesfx.PreferencesFx.WINDOW_HEIGHT;
import static com.dlsc.preferencesfx.PreferencesFx.WINDOW_POS_X;
import static com.dlsc.preferencesfx.PreferencesFx.WINDOW_POS_Y;
import static com.dlsc.preferencesfx.PreferencesFx.WINDOW_WIDTH;

import java.io.Serializable;
import java.util.prefs.Preferences;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import org.apache.commons.lang3.SerializationUtils;

public class StorageHandler {

  private Preferences preferences;

  public StorageHandler(Class<?> saveClass) {
    preferences = Preferences.userNodeForPackage(saveClass);
  }

  /**
   * Stores the last selected category in TreeView.
   *
   * @param categoryId the category id to be stored
   */
  public void putSelectedCategory(int categoryId) {
    preferences.putInt(SELECTED_CATEGORY, categoryId);
  }

  /**
   * Gets the last selected category in TreeView.
   *
   * @return the index of the selected category. 0 if none is found
   */
  public int getSelectedCategory() {
    return preferences.getInt(SELECTED_CATEGORY, DEFAULT_CATEGORY);
  }

  /**
   * Stores the given divider position of the MasterDetailPane.
   *
   * @param dividerPosition the divider position to be stored
   */
  public void putDividerPosition(double dividerPosition) {
    preferences.putDouble(DIVIDER_POSITION, dividerPosition);
  }

  /**
   * Gets the stored divider position of the MasterDetailPane.
   *
   * @return the double value of the divider position. 0.2 if none is found
   */
  public double getDividerPosition() {
    return preferences.getDouble(DIVIDER_POSITION, DEFAULT_DIVIDER_POSITION);
  }

  /**
   * Stores the window width of the PreferencesDialog.
   *
   * @param windowWidth the width of the window to be stored
   */
  public void putWindowWidth(double windowWidth) {
    preferences.putDouble(WINDOW_WIDTH, windowWidth);
  }

  /**
   * Searches for the window width of the PreferencesDialog.
   *
   * @return the double value of the window width. 1000 if none is found
   */
  public double getWindowWidth() {
    return preferences.getDouble(WINDOW_WIDTH, DEFAULT_PREFERENCES_WIDTH);
  }

  /**
   * Stores the window height of the PreferencesDialog.
   *
   * @param windowHeight the height of the window to be stored
   */
  public void putWindowHeight(double windowHeight) {
    preferences.putDouble(WINDOW_HEIGHT, windowHeight);
  }

  /**
   * Searches for the window height of the PreferencesDialog.
   *
   * @return the double value of the window height. 700 if none is found
   */
  public double getWindowHeight() {
    return preferences.getDouble(WINDOW_HEIGHT, DEFAULT_PREFERENCES_HEIGHT);
  }

  /**
   * Stores the position of the PreferencesDialog in horizontal orientation.
   *
   * @param windowPosX the double value of the window position in horizontal orientation
   */
  public void putWindowPosX(double windowPosX) {
    preferences.putDouble(WINDOW_POS_X, windowPosX);
  }

  /**
   * Searches for the horizontal window position.
   *
   * @return the double value of the horizontal window position
   */
  public double getWindowPosX() {
    return preferences.getDouble(WINDOW_POS_X, DEFAULT_PREFERENCES_POS_X);
  }

  /**
   * Stores the position of the PreferencesDialog in vertical orientation.
   *
   * @param windowPosY the double value of the window position in vertical orientation
   */
  public void putWindowPosY(double windowPosY) {
    preferences.putDouble(WINDOW_POS_Y, windowPosY);
  }

  /**
   * Searches for the vertical window position.
   *
   * @return the double value of the vertical window position
   */
  public double getWindowPosY() {
    return preferences.getDouble(WINDOW_POS_Y, DEFAULT_PREFERENCES_POS_Y);
  }

  /**
   * Saves a String to the preferences using the given key.
   *
   * @param breadCrumb the key to be used to store the String
   * @param value      the String to be saved in the preferences
   */
  public void saveSetting(String breadCrumb, String value) {
    preferences.put(breadCrumb, value);
  }

  /**
   * Saves a boolean to the preferences using the given key.
   *
   * @param breadCrumb the key to be used to store the boolean
   * @param value      the boolean to be saved in the preferences
   */
  public void saveSetting(String breadCrumb, boolean value) {
    preferences.putBoolean(breadCrumb, value);
  }

  /**
   * Saves an int to the preferences using the given key.
   *
   * @param breadCrumb the key to be used to store the int
   * @param value      the int to be saved in the preferences
   */
  public void saveSetting(String breadCrumb, int value) {
    preferences.putInt(breadCrumb, value);
  }

  /**
   * Saves a double to the preferences using the given key.
   *
   * @param breadCrumb the key to be used to store the double
   * @param value      the double to be saved in the preferences
   */
  public void saveSetting(String breadCrumb, double value) {
    preferences.putDouble(breadCrumb, value);
  }

  /**
   * Saves an Object to the preferences using the given key.
   *
   * @param breadCrumb the key to be used to store the Object
   * @param value      the ObjectProperty whose value is used to be saved in the preferences
   */
  public void saveSetting(String breadCrumb, ObjectProperty value) {
    byte[] serializedObjectProperty = SerializationUtils.serialize((Serializable) value.getValue());
    preferences.putByteArray(breadCrumb, serializedObjectProperty);
  }

  /**
   * Saves an ObservableList to the preferences using the given key.
   *
   * @param breadCrumb the key to be used to store the ObservableList
   * @param value      the ObservableList to be saved in the preferences
   */
  public void saveSetting(String breadCrumb, ObservableList value) {
    byte[] serializedObjectProperty = SerializationUtils.serialize((Serializable) value);
    preferences.putByteArray(breadCrumb, serializedObjectProperty);
  }

  /**
   * Finds a String which is stored in the preferences using the given key
   * or a default value if no such is found.
   *
   * @param breadCrumb the key to be used to search the String
   * @param value      the default String to be returned if nothing is found
   * @return the String which is stored in the preferences or the default value
   */
  public String getValue(String breadCrumb, String value) {
    return preferences.get(breadCrumb, value);
  }

  /**
   * Finds a boolean which is stored in the preferences using the given key
   * or a default value if no such is found.
   *
   * @param breadCrumb the key to be used to search the boolean
   * @param value      the default boolean to be returned if nothing is found
   * @return the boolean which is stored in the preferences or the default value
   */
  public boolean getValue(String breadCrumb, boolean value) {
    return preferences.getBoolean(breadCrumb, value);
  }

  /**
   * Finds an int which is stored in the preferences using the given key
   * or a default value if no such is found.
   *
   * @param breadCrumb the key to be used to search the int
   * @param value      the default int to be returned if nothing is found
   * @return the int which is stored in the preferences or the default value
   */
  public int getValue(String breadCrumb, int value) {
    return preferences.getInt(breadCrumb, value);
  }

  /**
   * Finds a double which is stored in the preferences using the given key
   * or a default value if no such is found.
   *
   * @param breadCrumb the key to be used to search the double
   * @param value      the default double to be returned if nothing is found
   * @return the double which is stored in the preferences or the default value
   */
  public double getValue(String breadCrumb, double value) {
    return preferences.getDouble(breadCrumb, value);
  }

  /**
   * Finds an Object which is stored in the preferences using the given key
   * or a default value if no such is found.
   *
   * @param breadCrumb the key to be used to search the Object
   * @param value      the ObjectProperty whose value is used as the default Object
   *                   to be returned if nothing is found
   * @return the Object which is stored in the preferences or the default value
   */
  public Object getValue(String breadCrumb, ObjectProperty value) {
    byte[] defaultObject = SerializationUtils.serialize((Serializable) value.getValue());
    byte[] finalValue = preferences.getByteArray(breadCrumb, defaultObject);
    return SerializationUtils.deserialize(finalValue);
  }

  /**
   * Finds an ObservableList which is stored in the preferences using the given key
   * or a default value if no such is found.
   *
   * @param breadCrumb the key to be used to search the ObservableList
   * @param value      the default ObservableList to be returned if nothing is found
   * @return the ObservableList which is stored in the preferences or the default value
   */
  public ObservableList getValue(String breadCrumb, ObservableList value) {
    byte[] defaultObject = SerializationUtils.serialize((Serializable) value);
    byte[] finalValue = preferences.getByteArray(breadCrumb, defaultObject);
    return SerializationUtils.deserialize(finalValue);
  }

}
