package com.dlsc.preferencesfx.pages;

import com.dlsc.formsfx.model.structure.Field;
import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.model.structure.Group;
import com.dlsc.formsfx.model.util.BindingMode;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import com.dlsc.preferencesfx.Setting;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
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
    form = Form.of(
        Group.of()
    ).title("form_label");
    for (Setting setting : settings) {
      String propertyString = setting.getWidget().valueProperty().getClass().toString();
      propertyString = propertyString.substring(28, propertyString.length());

      Field field = null;

      switch (propertyString) {
        case "SimpleBooleanProperty":
          field = Field.ofBooleanType((BooleanProperty) setting.getWidget().valueProperty()).label(setting.getDescription());
          break;
        case "SimpleStringProperty":
          field = Field.ofStringType((StringProperty) setting.getWidget().valueProperty()).label(setting.getDescription());
          break;
        case "SimpleIntegerProperty":
          field = Field.ofIntegerType((IntegerProperty) setting.getWidget().valueProperty()).label(setting.getDescription());
          break;
      }

      form.getGroups().get(0).getFields().add(field);

    }

    form.binding(BindingMode.CONTINUOUS);

  }

}
