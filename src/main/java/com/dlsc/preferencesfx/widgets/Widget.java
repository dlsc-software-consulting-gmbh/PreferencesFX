package com.dlsc.preferencesfx.widgets;

import javafx.beans.property.Property;
import javafx.scene.Node;

public class Widget<N extends Node, P extends Property> {
  private N node;
  private P value;

  public Widget(N node, P value) {
    this.node = node;
    this.value = value;
  }

  // ------- Getters and Setters -------------
  public N getNode() {
    return node;
  }

  public void setNode(N node) {
    this.node = node;
  }

  public P valueProperty() {
    return value;
  }
}
