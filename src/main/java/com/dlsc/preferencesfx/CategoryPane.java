package com.dlsc.preferencesfx;

import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.model.structure.Section;
import com.dlsc.formsfx.model.util.BindingMode;
import com.dlsc.preferencesfx.util.PreferencesFormRenderer;
import com.dlsc.preferencesfx.util.PreferencesGroup;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.layout.StackPane;

public class CategoryPane extends StackPane {

  private List<Group> groups;
  private Form form;

  /**
   * Creates an empty CategoryPane, in case of a {@link Category} without settings.
   */
  public CategoryPane() {
    groups = new ArrayList<>();
    initForm();
    getChildren().add(new PreferencesFormRenderer(form));
  }

  public CategoryPane(List<Group> groups) {
    this.groups = groups;
    initForm();
    getChildren().add(new PreferencesFormRenderer(form));
  }

  void initForm() {
    form = Form.of();
    List<com.dlsc.formsfx.model.structure.Group> formGroups = form.getGroups();
    for (int i = 0; i < groups.size(); i++) {
      formGroups.add(PreferencesGroup.of().title(groups.get(i).getDescription()));
      for (Setting setting : groups.get(i).getSettings()) {
        formGroups.get(i).getFields().add(setting.getField());
      }
    }
    form.binding(BindingMode.CONTINUOUS);
  }

}
