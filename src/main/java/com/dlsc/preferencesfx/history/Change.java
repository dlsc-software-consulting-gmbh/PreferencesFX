package com.dlsc.preferencesfx.history;

import com.dlsc.preferencesfx.CategoryTree;
import com.dlsc.preferencesfx.Setting;
import java.time.LocalDate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by François Martin on 02.12.17.
 */
public class Change<P> {

  private static final Logger LOGGER =
      LogManager.getLogger(Change.class.getName());

  final Setting setting;

  final P oldValue;
  final P newValue;

  final LocalDate timestamp;

  public Change(Setting setting, P oldValue, P newValue) {
    this.setting = setting;
    this.oldValue = oldValue;
    this.newValue = newValue;
    timestamp = LocalDate.now();
  }

  public void undo() {
    setting.valueProperty().setValue(oldValue);
  }

  public void redo() {
    setting.valueProperty().setValue(newValue);
  }

}
