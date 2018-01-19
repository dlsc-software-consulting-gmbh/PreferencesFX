package com.dlsc.preferencesfx.util;

import static com.dlsc.preferencesfx.util.Constants.DEFAULT_DIVIDER_POSITION;
import static com.dlsc.preferencesfx.util.Constants.DEFAULT_PREFERENCES_HEIGHT;
import static com.dlsc.preferencesfx.util.Constants.DEFAULT_PREFERENCES_POS_X;
import static com.dlsc.preferencesfx.util.Constants.DEFAULT_PREFERENCES_POS_Y;
import static com.dlsc.preferencesfx.util.Constants.DEFAULT_PREFERENCES_WIDTH;
import static com.dlsc.preferencesfx.util.Constants.DIVIDER_POSITION;
import static com.dlsc.preferencesfx.util.Constants.SELECTED_CATEGORY;
import static com.dlsc.preferencesfx.util.Constants.WINDOW_HEIGHT;
import static com.dlsc.preferencesfx.util.Constants.WINDOW_POS_X;
import static com.dlsc.preferencesfx.util.Constants.WINDOW_POS_Y;
import static com.dlsc.preferencesfx.util.Constants.WINDOW_WIDTH;

import com.dlsc.preferencesfx.model.Setting;
import com.google.gson.Gson;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Handles everything related to storing values of {@link Setting} using {@link Preferences}.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class StorageHandler {

  private static final Logger LOGGER =
      LogManager.getLogger(StorageHandler.class.getName());

  private Preferences preferences;
  private Gson gson;

  public StorageHandler(Class<?> saveClass) {
    preferences = Preferences.userNodeForPackage(saveClass);
    gson = new Gson();
  }

  /**
   * Stores the last selected category in TreeSearchView.
   *
   * @param breadcrumb the category path as a breadcrumb string
   */
  public void saveSelectedCategory(String breadcrumb) {
    preferences.put(SELECTED_CATEGORY, breadcrumb);
  }

  /**
   * Gets the last selected category in TreeSearchView.
   *
   * @return the breadcrumb string of the selected category. null if none is found
   */
  public String loadSelectedCategory() {
    return preferences.get(SELECTED_CATEGORY, null);
  }

  /**
   * Stores the given divider position of the MasterDetailPane.
   *
   * @param dividerPosition the divider position to be stored
   */
  public void saveDividerPosition(double dividerPosition) {
    preferences.putDouble(DIVIDER_POSITION, dividerPosition);
  }

  /**
   * Gets the stored divider position of the MasterDetailPane.
   *
   * @return the double value of the divider position. 0.2 if none is found
   */
  public double loadDividerPosition() {
    return preferences.getDouble(DIVIDER_POSITION, DEFAULT_DIVIDER_POSITION);
  }

  /**
   * Stores the window width of the PreferencesFxDialog.
   *
   * @param windowWidth the width of the window to be stored
   */
  public void saveWindowWidth(double windowWidth) {
    preferences.putDouble(WINDOW_WIDTH, windowWidth);
  }

  /**
   * Searches for the window width of the PreferencesFxDialog.
   *
   * @return the double value of the window width. 1000 if none is found
   */
  public double loadWindowWidth() {
    return preferences.getDouble(WINDOW_WIDTH, DEFAULT_PREFERENCES_WIDTH);
  }

  /**
   * Stores the window height of the PreferencesFxDialog.
   *
   * @param windowHeight the height of the window to be stored
   */
  public void saveWindowHeight(double windowHeight) {
    preferences.putDouble(WINDOW_HEIGHT, windowHeight);
  }

  /**
   * Searches for the window height of the PreferencesFxDialog.
   *
   * @return the double value of the window height. 700 if none is found
   */
  public double loadWindowHeight() {
    return preferences.getDouble(WINDOW_HEIGHT, DEFAULT_PREFERENCES_HEIGHT);
  }

  /**
   * Stores the position of the PreferencesFxDialog in horizontal orientation.
   *
   * @param windowPosX the double value of the window position in horizontal orientation
   */
  public void saveWindowPosX(double windowPosX) {
    preferences.putDouble(WINDOW_POS_X, windowPosX);
  }

  /**
   * Searches for the horizontal window position.
   *
   * @return the double value of the horizontal window position
   */
  public double loadWindowPosX() {
    return preferences.getDouble(WINDOW_POS_X, DEFAULT_PREFERENCES_POS_X);
  }

  /**
   * Stores the position of the PreferencesFxDialog in vertical orientation.
   *
   * @param windowPosY the double value of the window position in vertical orientation
   */
  public void saveWindowPosY(double windowPosY) {
    preferences.putDouble(WINDOW_POS_Y, windowPosY);
  }

  /**
   * Searches for the vertical window position.
   *
   * @return the double value of the vertical window position
   */
  public double loadWindowPosY() {
    return preferences.getDouble(WINDOW_POS_Y, DEFAULT_PREFERENCES_POS_Y);
  }

  /**
   * Serializes a given Object and saves it to the preferences using the given key.
   *
   * @param breadcrumb the key which is used to save the serialized Object
   * @param object     the Object which will be saved
   */
  // asciidoctor Documentation - tag::storageHandlerSave[]
  public void saveObject(String breadcrumb, Object object) {
    preferences.put(hash(breadcrumb), gson.toJson(object));
  }
  // asciidoctor Documentation - end::storageHandlerSave[]

  /**
   * Searches in the preferences after a serialized Object using the given key,
   * deserializes and returns it. Returns a default Object if nothing is found.
   *
   * @param breadcrumb    the key which is used to search the serialized Object
   * @param defaultObject the Object which will be returned if nothing is found
   * @return the deserialized Object or the default Object if nothing is found
   */
  // asciidoctor Documentation - tag::storageHandlerLoad[]
  public Object loadObject(String breadcrumb, Object defaultObject) {
    String serializedDefault = gson.toJson(defaultObject);
    String json = preferences.get(hash(breadcrumb), serializedDefault);
    return gson.fromJson(json, Object.class);
  }
  // asciidoctor Documentation - end::storageHandlerLoad[]

  /**
   * Searches in the preferences after a serialized ArrayList using the given key,
   * deserializes and returns it as ObservableArrayList.
   * When an ObservableList is deserialzed, Gson returns an ArrayList
   * and needs to be wrapped into an ObservableArrayList. This is only needed for loading.
   *
   * @param breadcrumb            the key which is used to search the serialized ArrayList
   * @param defaultObservableList the default ObservableList
   *                              which will be returned if nothing is found
   * @return the deserialized ObservableList or the default ObservableList if nothing is found
   */
  public ObservableList loadObservableList(
      String breadcrumb,
      ObservableList defaultObservableList
  ) {
    String serializedDefault = gson.toJson(defaultObservableList);
    String json = preferences.get(hash(breadcrumb), serializedDefault);
    return FXCollections.observableArrayList(gson.fromJson(json, ArrayList.class));
  }

  /**
   * Clears the preferences.
   *
   * @return true if successful, false if there was an exception.
   */
  public boolean clearPreferences() {
    try {
      preferences.clear();
    } catch (BackingStoreException e) {
      return false;
    }
    return true;
  }

  /**
   * Generates a SHA-256 hash of a String.
   * Since {@link Preferences#MAX_KEY_LENGTH} is 80, if the breadcrumb is over 80 characters, it
   * will lead to an exception while saving. This method generates a SHA-256 hash of the breadcrumb
   * to save / load as the key in {@link Preferences}, since those are guaranteed to be
   * maximum 64 chars long.
   *
   * @return SHA-256 representation of breadcrumb
   */
  public String hash(String key) {
    Objects.requireNonNull(key);
    MessageDigest messageDigest = null;
    try {
      messageDigest = MessageDigest.getInstance("SHA-256");
    } catch (NoSuchAlgorithmException e) {
      LOGGER.error("Hashing algorithm not found!");
    }
    messageDigest.update(key.getBytes());
    return new String(messageDigest.digest());
  }

  public Preferences getPreferences() {
    return preferences;
  }


}
