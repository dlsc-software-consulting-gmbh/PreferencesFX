package com.dlsc.preferencesfx.pages;

import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.preferencesfx.Setting;
import javafx.scene.layout.StackPane;

public class Page extends StackPane {

  Setting[] settings;
  Form form;

  Page(Setting[] settings) {
    this.settings = settings;

  }

}
