package com.dlsc.preferencesfx.util;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link StorageHandlerImpl}.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class StorageHandlerImplTest {
  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testClearPreferences() throws BackingStoreException {
    StorageHandler storageHandler = new StorageHandlerImpl(StorageHandlerImplTest.class);
    Preferences preferences = storageHandler.getPreferences();
    preferences.clear();
  }

  @Test
  public void saveSelectedCategory() {
  }

  @Test
  public void loadSelectedCategory() {
  }

  @Test
  public void saveDividerPosition() {
  }

  @Test
  public void loadDividerPosition() {
  }

  @Test
  public void saveWindowWidth() {
  }

  @Test
  public void loadWindowWidth() {
  }

  @Test
  public void saveWindowHeight() {
  }

  @Test
  public void loadWindowHeight() {
  }

  @Test
  public void saveWindowPosX() {
  }

  @Test
  public void loadWindowPosX() {
  }

  @Test
  public void saveWindowPosY() {
  }

  @Test
  public void loadWindowPosY() {
  }

  @Test
  public void saveObject() {
  }

  @Test
  public void loadObject() {
  }

  @Test
  public void loadObservableList() {
  }
}
