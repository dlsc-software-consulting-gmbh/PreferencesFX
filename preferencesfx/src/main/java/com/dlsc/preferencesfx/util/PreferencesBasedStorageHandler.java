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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles everything related to storing values of {@link Setting} using {@link Preferences}.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public abstract class PreferencesBasedStorageHandler implements StorageHandler {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(PreferencesBasedStorageHandler.class.getName());

  private Preferences preferences;

  public PreferencesBasedStorageHandler(Class<?> saveClass) {
    preferences = Preferences.userNodeForPackage(saveClass);
  }

  // asciidoctor Documentation - tag::serializationDeserialization[]
  /**
   * Serializes an object to a string for storage.
   *
   * <p>Subclasses may use any way to serialize the object.
   * This should be the inverse operation of {@link #deserialize(String, Class)}.
   *
   * <p>Therefore the following must hold true for any x of type X:
   * <pre>
   *   Objects.equals(x, deserialize(serialize(x), X.class)
   * </pre>
   *
   * @param object the object to serialize
   * @return string representation of the object for storage
   */
  protected abstract String serialize(Object object);

  /**
   * Deserializes an object from a string for storage.
   *
   * <p>Subclasses may use any way to deserialize the object.
   * This should be the inverse operation of {@link #serialize(Object)}.
   *
   * <p>Therefore the following must hold true for any x of type X:
   * <pre>
   *   Objects.equals(x, deserialize(serialize(x), X.class)
   * </pre>
   *
   * @param <T>        the type of object into which to deserialze the string
   * @param serialized the string to deserialze
   * @param type       the class into which to deserialize the string
   * @return string representation of the object for storage
   */
  protected abstract <T> T deserialize(String serialized, Class<? extends T> type);
  // asciidoctor Documentation - end::serializationDeserialization[]


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
  public void saveObject(String breadcrumb, Object object) {
    preferences.put(hash(breadcrumb), serialize(object));
  }

  /**
   * Searches in the preferences after a serialized Object using the given key,
   * deserializes and returns it. Returns a default Object if nothing is found.
   *
   * @param breadcrumb    the key which is used to search the serialized Object
   * @param defaultObject the Object which will be returned if nothing is found
   * @return the deserialized Object or the default Object if nothing is found
   */
  public Object loadObject(String breadcrumb, Object defaultObject) {
    String serialized = getSerializedPreferencesValue(breadcrumb, serialize(defaultObject));
    return deserialize(serialized, Object.class);
  }

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
    String serialized = getSerializedPreferencesValue(breadcrumb, serialize(defaultObservableList));
    return FXCollections.observableArrayList(deserialize(serialized, ArrayList.class));
  }

  private String getSerializedPreferencesValue(String breadcrumb, String serializedDefault) {
    String serialized = preferences.get(hash(breadcrumb), serializedDefault);
    if (serialized == serializedDefault) {
      // try to get preferences value with legacy hashing method
      serialized = preferences.get(deprecatedHash(breadcrumb), serializedDefault);
      if (serialized != serializedDefault) {
        LOGGER.warn("Preferences value of {} was loaded using the legacy hashing method. "
            + "Value will be saved using the new hashing method with next save.", breadcrumb);
      }
    }
    return serialized;
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
   * Legacy hashing method to calculate the SHA256 hash of a key.
   * In some circumstances, this approach produces hashes with incorrect encoding, leading to issues
   * with loading preferences (see #53).
   * This method is only present for migration reasons, to ensure preferences with the old
   * hashing format can still be loaded and then saved using the new hashing method
   * ({@link #hash(String)}).
   * This method may get removed in a later release, so DON'T use this method to save settings!
   *
   * @return SHA-256 representation of breadcrumb
   */
  @Deprecated
  private String deprecatedHash(String key) {
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

  /**
   * Generates a SHA-256 hash of a String.
   * Since {@link Preferences#MAX_KEY_LENGTH} is 80, if the breadcrumb is over 80 characters, it
   * will lead to an exception while saving. This method generates a SHA-256 hash of the breadcrumb
   * to save / load as the key in {@link Preferences}, since those are guaranteed to be
   * maximum 64 chars long.
   *
   * @param key the string for which to calculate the hash
   * @return SHA-256 representation of breadcrumb
   */
  public String hash(String key) {
    return Strings.sha256(key);
  }

  public Preferences getPreferences() {
    return preferences;
  }
}
