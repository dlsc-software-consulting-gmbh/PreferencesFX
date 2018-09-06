package com.dlsc.preferencesfx.history;

import com.dlsc.preferencesfx.model.Setting;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a change, which consists of a new and old value.
 *
 * @param <P> the data type of the change
 * @author François Martin
 * @author Marco Sanfratello
 */
public class Change<P> {
  private static final Logger LOGGER = LogManager.getLogger(Change.class.getName());

  public final Setting setting;
  /** The value before the change */
  public final P oldValue;
  /** The value after the change */
  public final P newValue;
  /** Timestamp marking the creation of this change */
  private final LocalDateTime timestamp = LocalDateTime.now();

  /**
   * Constructs a change.
   *
   * @param setting  the setting that was changed
   * @param oldValue the "before" value of the change
   * @param newValue the "after" value of the change
   */
  public Change(Setting setting, P oldValue, P newValue) {
    this.setting = setting;
    this.oldValue = oldValue;
    this.newValue = newValue;
  }

  /**
   * Compares newValue and oldValue to see if they are the same.
   * If this is the case, this change is redundant, since it doesn't represent a true change.
   * This can happen on compounded changes.
   *
   * @return true if redundant, else if otherwise.
   */
  public boolean isRedundant() {
    return newValue instanceof Collection ?
        CollectionUtils.isEqualCollection((Collection) oldValue, (Collection) newValue) :
        Objects.equals(oldValue, newValue);
  }

  /**
   * Undos this change.
   *
   * The value of the {@link Setting} is set to {@link #oldValue}
   */
  public void undo() {
    setting.valueProperty().setValue(oldValue);
  }

  /**
   * Redos this change.
   *
   * The value of the {@link Setting} is set to {@link #newValue}
   */
  public void redo() {
    setting.valueProperty().setValue(newValue);
  }

  public String getTimestamp() {
    DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
    return timestamp.format(formatter);
  }

}
