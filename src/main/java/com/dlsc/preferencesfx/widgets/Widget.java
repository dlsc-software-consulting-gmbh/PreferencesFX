package com.dlsc.preferencesfx.widgets;

import com.dlsc.formsfx.model.structure.Field;
import javafx.beans.property.Property;

public class Widget<F extends Field, P extends Property> {
  private F field;
  private P value;

  public Widget(F field, P value) {
    this.field = field;
    this.value = value;
  }

  // ------- Getters and Setters -------------
  public F getField() {
    return field;
  }

  public void setField(F field) {
    this.field = field;
  }

  public P valueProperty() {
    return value;
  }
}