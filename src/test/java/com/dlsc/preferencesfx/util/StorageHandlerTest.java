package com.dlsc.preferencesfx.util;

import com.dlsc.preferencesfx_old.AppStarter;
import com.dlsc.preferencesfx_old.util.StorageHandler;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class StorageHandlerTest {
  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testClearPreferences() throws BackingStoreException {
    StorageHandler storageHandler = new StorageHandler(AppStarter.class);
    Preferences preferences = storageHandler.getPreferences();
    preferences.clear();
  }
}
