package com.dlsc.preferencesfx.util;

import com.dlsc.preferencesfx.model.Setting;
import java.util.prefs.Preferences;
import javafx.collections.ObservableList;

/**
 * Abstraction for storing values of {@link Setting}.
 * API user able to provide custom implementations by creating a new class and
 * implementing this interface.
 *
 * @author François Martin
 * @author Marco Sanfratello
 * @author kivimango
 */

public interface StorageHandler {

    /**
     * Stores the last selected category in TreeSearchView.
     *
     * @param breadcrumb the category path as a breadcrumb string
     */
    void saveSelectedCategory(String breadcrumb);

    /**
     * Gets the last selected category in TreeSearchView.
     *
     * @return the breadcrumb string of the selected category
     */
    String loadSelectedCategory();

    /**
     * Stores the given divider position of the MasterDetailPane.
     *
     * @param dividerPosition the divider position to be stored
     */
    void saveDividerPosition(double dividerPosition);

    /**
     * Gets the stored divider position of the MasterDetailPane.
     *
     * @return the double value of the divider position
     */
    double loadDividerPosition();

    /**
     * Stores the window width of the PreferencesFxDialog.
     *
     * @param windowWidth the width of the window to be stored
     */
    void saveWindowWidth(double windowWidth);

    /**
     * Searches for the window width of the PreferencesFxDialog.
     *
     * @return the double value of the window width
     */
    double loadWindowWidth();

    /**
     * Stores the window height of the PreferencesFxDialog.
     *
     * @param windowHeight the height of the window to be stored
     */
    void saveWindowHeight(double windowHeight);

    /**
     * Searches for the window height of the PreferencesFxDialog.
     *
     * @return the double value of the window height
     */
    double loadWindowHeight();

    /**
     * Stores the position of the PreferencesFxDialog in horizontal orientation.
     *
     * @param windowPosX the double value of the window position in horizontal orientation
     */
    void saveWindowPosX(double windowPosX);

    /**
     * Searches for the horizontal window position.
     *
     * @return the double value of the horizontal window position
     */
    double loadWindowPosX();

    /**
     * Stores the position of the PreferencesFxDialog in vertical orientation.
     *
     * @param windowPosY the double value of the window position in vertical orientation
     */
    void saveWindowPosY(double windowPosY);

    /**
     * Searches for the vertical window position.
     *
     * @return the double value of the vertical window position
     */
    double loadWindowPosY();

    /**
     * Serializes a given Object and saves it to the storage using the given key.
     *
     * @param breadcrumb the key which is used to save the serialized Object
     * @param object     the Object which will be saved
     */
    // asciidoctor Documentation - tag::storageHandlerSave[]
    void saveObject(String breadcrumb, Object object);
    // asciidoctor Documentation - end::storageHandlerSave[]

    /**
     * Searches in the storage after a serialized Object using the given key,
     * deserializes and returns it. Returns a default Object if nothing is found.
     *
     * @param breadcrumb    the key which is used to search the serialized Object
     * @param defaultObject the Object which will be returned if nothing is found
     * @return the deserialized Object or the default Object if nothing is found
     */
    // asciidoctor Documentation - tag::storageHandlerLoad[]
    Object loadObject(String breadcrumb, Object defaultObject);
    // asciidoctor Documentation - end::storageHandlerLoad[]

    /**
     * Searches in the storage after a serialized ArrayList using the given key,
     * deserializes and returns it as ObservableArrayList.
     * When an ObservableList is deserialzed, Gson returns an ArrayList
     * and needs to be wrapped into an ObservableArrayList. This is only needed for loading.
     *
     * @param breadcrumb            the key which is used to search the serialized ArrayList
     * @param defaultObservableList the default ObservableList
     *                              which will be returned if nothing is found
     * @return the deserialized ObservableList or the default ObservableList if nothing is found
     */
    ObservableList loadObservableList(String breadcrumb, ObservableList defaultObservableList);

    /**
     * Clears the storage.
     * @return true if successful, false if there was an exception.
     */
    boolean clearPreferences();

    Preferences getPreferences();

}
