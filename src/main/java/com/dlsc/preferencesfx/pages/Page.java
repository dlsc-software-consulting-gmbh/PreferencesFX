package com.dlsc.preferencesfx.pages;

import com.dlsc.formsfx.model.structure.Field;
import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.model.structure.Group;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import com.dlsc.formsfx.view.util.ColSpan;
import com.dlsc.preferencesfx.Setting;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.layout.StackPane;

public class Page extends StackPane {

  Setting[] settings;
  Form form;

  public Page(Setting[] settings) {
    this.settings = settings;
    initForm();
    getChildren().add(new FormRenderer(form));
  }

  void initForm() {
    StringProperty s = new SimpleStringProperty("Test");
    form = Form.of(
        Group.of()
    ).title("form_label");
    form.getGroups().get(0).getFields().add(
        Field.ofStringType(s)
            .label("currency_label")
            .placeholder("currency_placeholder")
            .span(ColSpan.HALF)
    );
  }

}
