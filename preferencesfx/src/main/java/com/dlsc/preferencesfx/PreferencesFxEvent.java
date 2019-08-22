package com.dlsc.preferencesfx;

import javafx.event.Event;
import javafx.event.EventType;

/**
 * Identifies events triggered by {@code PreferencesFx} when the dialog is closed.
 *
 * @author Andres Almiray
 */
public class PreferencesFxEvent extends Event {
  /**
   * When preferences are saved (dialog was closed).
   */
  public static final EventType<PreferencesFxEvent> EVENT_PREFERENCES_SAVED =
      new EventType<>(ANY, "EVENT_PREFERENCES_SAVED");

  /**
   * When preferences are not saved (dialog was cancelled).
   */
  public static final EventType<PreferencesFxEvent> EVENT_PREFERENCES_NOT_SAVED =
      new EventType<>(ANY, "EVENT_PREFERENCES_NOT_SAVED");

  /**
   * Creates a new instance of {@code PreferencesFxEvent} with event type set to {@code
   * EVENT_PREFERENCES_SAVED}.
   *
   * @return the newly created PreferencesFxEvent.
   */
  public static PreferencesFxEvent preferencesSavedEvent() {
    return new PreferencesFxEvent(EVENT_PREFERENCES_SAVED);
  }

  /**
   * Creates a new instance of {@code PreferencesFxEvent} with event type set to {@code
   * EVENT_PREFERENCES_NOT_SAVED}.
   *
   * @return the newly created PreferencesFxEvent.
   */
  public static PreferencesFxEvent preferencesNotSavedEvent() {
    return new PreferencesFxEvent(EVENT_PREFERENCES_NOT_SAVED);
  }

  private PreferencesFxEvent(EventType<? extends Event> eventType) {
    super(eventType);
  }
}
