package com.dlsc.preferencesfx.util;

import com.dlsc.preferencesfx.model.Setting;
import com.google.gson.Gson;
import java.util.prefs.Preferences;

/**
 * Handles everything related to storing values of {@link Setting} using {@link Preferences}.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class StorageHandlerImpl extends PreferencesBasedStorageHandler {

  private final Gson gson;

  public StorageHandlerImpl(Class<?> saveClass) {
    super(saveClass);
    gson = new Gson();
  }

  @Override
  protected String serialize(Object object) {
    return gson.toJson(object);
  }

  @Override
  protected <T> T deserialize(String serialized, Class<? extends T> type) {
    return gson.fromJson(serialized, type);
  }
}
