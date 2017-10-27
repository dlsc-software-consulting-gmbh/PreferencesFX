package com.dlsc.preferencesfx.widgets;

import javafx.beans.property.Property;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;

public class Toggle {

  private Widget widget;

  public Toggle() {
    CheckBox checkBox = new CheckBox();
    this.widget = new Widget(checkBox, checkBox.selectedProperty());
  }
}
