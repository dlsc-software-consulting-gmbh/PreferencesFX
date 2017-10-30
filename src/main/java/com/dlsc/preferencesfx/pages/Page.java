package com.dlsc.preferencesfx.pages;

import com.dlsc.preferencesfx.Setting;
import javafx.scene.layout.StackPane;

public class Page extends StackPane {

  Setting[] settings;

  Page(Setting[] settings) {
    this.settings = settings;
  }

}
