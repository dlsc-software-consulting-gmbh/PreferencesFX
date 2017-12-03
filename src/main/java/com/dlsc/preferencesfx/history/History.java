package com.dlsc.preferencesfx.history;

import com.dlsc.preferencesfx.Setting;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by François Martin on 02.12.17.
 */
public class History {

  private List<Change> changes = new ArrayList<>();
  private int position = 0;
  private int validPosition = 0;

  public void attachChangeListener(Setting setting) {
    setting.valueProperty().addListener((observable, oldValue, newValue) -> {
      addChange(new Change(setting, oldValue, newValue));
    });
  }

  public void addChange(Change change) {
    changes.set(position, change);
    validPosition = ++position;
  }

  public Change next() {
    if (hasNext()) {
      return changes.get(++position);
    }
    return null;
  }

  public boolean hasNext() {
    return position < validPosition;
  }

  public Change prev() {
    if (hasPrev()) {
      return changes.get(--position);
    }
    return null;
  }

  public boolean hasPrev() {
    return position > 0;
  }

}
