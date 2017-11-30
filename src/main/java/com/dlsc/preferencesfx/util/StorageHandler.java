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

import java.util.Arrays;
import java.util.List;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.commons.lang3.SerializationException;
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
   * @param breadcrumb the key to be used to store the String
   * @param value      the String to be saved in the preferences
   */
  public void saveSetting(String breadcrumb, String value) {
    preferences.put(breadcrumb, value);
  }

  /**
   * Saves a boolean to the preferences using the given key.
   *
   * @param breadcrumb the key to be used to store the boolean
   * @param value      the boolean to be saved in the preferences
   */
  public void saveSetting(String breadcrumb, boolean value) {
    preferences.putBoolean(breadcrumb, value);
  }

  /**
   * Saves an int to the preferences using the given key.
   *
   * @param breadcrumb the key to be used to store the int
   * @param value      the int to be saved in the preferences
   */
  public void saveSetting(String breadcrumb, int value) {
    preferences.putInt(breadcrumb, value);
  }

  /**
   * Saves a double to the preferences using the given key.
   *
   * @param breadcrumb the key to be used to store the double
   * @param value      the double to be saved in the preferences
   */
  public void saveSetting(String breadcrumb, double value) {
    preferences.putDouble(breadcrumb, value);
  }

  /**
   * Saves an Object to the preferences using the given key.
   *
   * @param breadcrumb the key to be used to store the Object
   * @param value      the ObjectProperty whose value is used to be saved in the preferences
   */
  public void saveSetting(String breadcrumb, ObjectProperty value) {
    preferences.put(breadcrumb, value.getValue().toString());
  }

  /**
   * Saves an ObservableList to the preferences using the given key.
   *
   * @param breadcrumb the key to be used to store the ObservableList
   * @param value      the ObservableList to be saved in the preferences
   */
  public void saveSetting(String breadcrumb, ObservableList value) {
    String[] strings = new String[value.size()];
    for (int i = 0; i < strings.length; ++i) {
      strings[i] = value.get(i).toString();
    }
    byte[] serializedStrings = SerializationUtils.serialize(strings);
    preferences.putByteArray(breadcrumb, serializedStrings);
  }

  /**
   * Finds a String which is stored in the preferences using the given key
   * or a default value if no such is found.
   *
   * @param breadcrumb the key to be used to search the String
   * @param value      the default String to be returned if nothing is found
   * @return the String which is stored in the preferences or the default value
   */
  public String getValue(String breadcrumb, String value) {
    return preferences.get(breadcrumb, value);
  }

  /**
   * Finds a boolean which is stored in the preferences using the given key
   * or a default value if no such is found.
   *
   * @param breadcrumb the key to be used to search the boolean
   * @param value      the default boolean to be returned if nothing is found
   * @return the boolean which is stored in the preferences or the default value
   */
  public boolean getValue(String breadcrumb, boolean value) {
    return preferences.getBoolean(breadcrumb, value);
  }

  /**
   * Finds an int which is stored in the preferences using the given key
   * or a default value if no such is found.
   *
   * @param breadcrumb the key to be used to search the int
   * @param value      the default int to be returned if nothing is found
   * @return the int which is stored in the preferences or the default value
   */
  public int getValue(String breadcrumb, int value) {
    return preferences.getInt(breadcrumb, value);
  }

  /**
   * Finds a double which is stored in the preferences using the given key
   * or a default value if no such is found.
   *
   * @param breadcrumb the key to be used to search the double
   * @param value      the default double to be returned if nothing is found
   * @return the double which is stored in the preferences or the default value
   */
  public double getValue(String breadcrumb, double value) {
    return preferences.getDouble(breadcrumb, value);
  }

  /**
   * Finds an Object which is stored in the preferences using the given key
   * or a default value if no such is found.
   *
   * @param breadcrumb    the key to be used to search the Object
   * @param defaultObject the default ObjectProperty whose value will be returned
   *                      if nothing is found
   * @param allObjects    all possible Objects which can possibly be selected
   * @return the Object which is stored in the preferences or the default value
   */
  public Object getValue(
      String breadcrumb, ObjectProperty defaultObject, ObservableList allObjects) {
    for (Object object : allObjects) {
      String toStringOfFoundObject = preferences.get(breadcrumb, null);
      if (object.toString().equals(toStringOfFoundObject)) {
        return object;
      }
    }
    return defaultObject.getValue();
  }

  /**
   * Finds an ObservableList which is stored in the preferences using the given key
   * or a default value if no such is found.
   *
   * @param breadcrumb        the key to be used to search the ObservableList
   * @param defaultSelections the default Objects which are returned if nothing is found
   * @param allObjects        all possible Objects which can possibly be selected
   * @return the selection which is stored in the preferences or the default value
   */
  public ObservableList getValue(
      String breadcrumb, ListProperty defaultSelections, ObservableList allObjects) {
    try {
      byte[] serializedStrings = preferences.getByteArray(breadcrumb, null);
      String[] strings = SerializationUtils.deserialize(serializedStrings);
      List<String> stringsLst = Arrays.asList(strings);
      List list = (List) allObjects
          .stream()
          .filter(obj -> stringsLst.contains(obj.toString()))
          .collect(Collectors.toList());
      return FXCollections.observableList(list);
    } catch (SerializationException e) {
      return defaultSelections;
    }
  }

}
