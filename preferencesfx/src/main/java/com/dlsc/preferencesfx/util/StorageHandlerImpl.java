package com.dlsc.preferencesfx.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles everything related to serializing and de-serializing values.
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
  protected <T> T deserialize(String serialized, Class<T> type) {
    return gson.fromJson(serialized, type);
  }

  @Override
  protected <T> List<T> deserializeList(String serialized, Class<T> type) {
    final TypeToken typeToken = new TypeToken<List<JsonElement>>() {};
    final List<JsonElement> list = gson.fromJson(serialized, typeToken.getType());

    if (list == null) {
      return null;
    }

    return list.stream()
        .map(jsonElement -> gson.fromJson(jsonElement, type))
        .collect(Collectors.toList());
  }
}
