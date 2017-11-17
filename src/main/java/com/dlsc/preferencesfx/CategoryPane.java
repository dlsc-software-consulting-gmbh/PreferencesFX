package com.dlsc.preferencesfx;

import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.model.structure.Group;
import com.dlsc.formsfx.model.util.BindingMode;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import javafx.scene.layout.StackPane;

public class CategoryPane extends StackPane {

  Setting[] settings;
  Form form;

  public CategoryPane(Setting[] settings) {
    this.settings = settings;
    initForm();
    getChildren().add(new FormRenderer(form));
  }

  void initForm() {
    form = Form.of(
        Group.of()
    ).title("form_label");
    for (Setting setting : settings) {
      form.getGroups().get(0).getFields().add(setting.getField());
    }
    form.binding(BindingMode.CONTINUOUS);
  }

}
