package com.dlsc.preferencesfx.history;

import com.dlsc.preferencesfx.Setting;
import java.time.LocalDate;

/**
 * Created by François Martin on 02.12.17.
 */
public class Change<P> {

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
