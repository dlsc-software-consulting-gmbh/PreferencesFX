package com.dlsc.preferencesfx.history;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dlsc.preferencesfx.model.Setting;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by François Martin on 03.12.2017.
 */
public class HistoryTest {
  History history;
  StringProperty property;
  StringProperty mockProperty;
  Setting mockSetting;

  @Before
  public void setUp() throws Exception {
    history = new History();
    property = new SimpleStringProperty("");
    mockProperty = mock(SimpleStringProperty.class);
    mockSetting = mock(Setting.class);
    setupProperty();
    setupMockProperty();
  }

  private void setupMockProperty() {
    when(mockSetting.valueProperty()).thenReturn(mockProperty);
  }

  private void setupProperty() {
    when(mockSetting.valueProperty()).thenReturn(property);
  }

  @Test
  public void attachChangeListener() throws Exception {
    setupMockProperty();
    verify(mockProperty, never()).addListener(any(ChangeListener.class));
    history.attachChangeListener(mockSetting);
    verify(mockProperty).addListener(any(ChangeListener.class));
  }

  @Test
  public void doWithoutListeners() throws Exception {
  }

  @Test
  public void undo() throws Exception {
  }

  @Test
  public void redo() throws Exception {
  }

  @Test
  public void isUndoAvailable() throws Exception {
  }

  @Test
  public void undoAvailableProperty() throws Exception {
  }

  @Test
  public void isRedoAvailable() throws Exception {
  }

  @Test
  public void redoAvailableProperty() throws Exception {
  }

  @Test
  public void attachChangeListener1() {
  }

  @Test
  public void doWithoutListeners1() {
  }

  @Test
  public void undo1() {
  }

  @Test
  public void undoAll() {
  }

  @Test
  public void redo1() {
  }

  @Test
  public void redoAll() {
  }

  @Test
  public void clear() {
  }
}
