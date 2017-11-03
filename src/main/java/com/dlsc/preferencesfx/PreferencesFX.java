package com.dlsc.preferencesfx;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.jmx.MXNodeAlgorithm;
import com.sun.javafx.jmx.MXNodeAlgorithmContext;
import com.sun.javafx.sg.prism.NGNode;
import java.util.Arrays;
import java.util.List;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class PreferencesFX extends StackPane {

  private List<Category> categories;

  PreferencesFX(Category[] categories) {
    this.categories = Arrays.asList(categories);

    getChildren().add(this.categories.get(0).getPage());
  }

  public static PreferencesFX of(Category... categories) {
    return new PreferencesFX(categories);
  }

  public List<Category> getCategories() {
    return categories;
  }
}
