package com.dlsc.preferencesfx;

import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.model.structure.Section;
import com.dlsc.formsfx.model.util.BindingMode;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import java.util.Arrays;
import java.util.List;
import javafx.scene.layout.StackPane;

public class CategoryPane extends StackPane {

  List<Group> groups;
  Form form;

  public CategoryPane(Group[] groups) {
    this.groups = Arrays.asList(groups);
    initForm();
    getChildren().add(new FormRenderer(form));
  }

  void initForm() {
    form = Form.of();
    List<com.dlsc.formsfx.model.structure.Group> formGroups = form.getGroups();
    for (int i = 0; i < groups.size(); i++) {
      formGroups.add(Section.of().title(groups.get(0).getDescription()));
      for (Setting setting : groups.get(i).getSettings()) {
        formGroups.get(i).getFields().add(setting.getField());
      }
    }
    form.binding(BindingMode.CONTINUOUS);
  }

}
