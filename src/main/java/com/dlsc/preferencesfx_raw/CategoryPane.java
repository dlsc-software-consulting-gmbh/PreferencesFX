package com.dlsc.preferencesfx_raw;

import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.model.util.BindingMode;
import com.dlsc.preferencesfx2.model.Category;
import com.dlsc.preferencesfx2.model.Group;
import com.dlsc.preferencesfx2.model.Setting;
import com.dlsc.preferencesfx2.formsfx.view.renderer.PreferencesFormRenderer;
import com.dlsc.preferencesfx2.formsfx.view.renderer.PreferencesGroup;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.layout.StackPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CategoryPane extends StackPane {

  private static final Logger LOGGER =
      LogManager.getLogger(CategoryPane.class.getName());

  private List<Group> groups = new ArrayList<>();
  private Form form;

  /**
   * Creates a CategoryPane.
   *
   * @param groups if null, an empty CategoryPane will be created,
   *               in case of a {@link Category} without settings.
   */
  public CategoryPane(List<Group> groups) {
    if (groups != null) {
      this.groups = groups;
    }
    initForm();
    getChildren().add(new PreferencesFormRenderer(form));
  }

  void initForm() {
    form = Form.of();
    List<com.dlsc.formsfx.model.structure.Group> formGroups = form.getGroups();
    for (int i = 0; i < groups.size(); i++) {
      PreferencesGroup preferencesGroup =
          (PreferencesGroup) PreferencesGroup.of().title(groups.get(i).getDescription());
      groups.get(i).setPreferencesGroup(preferencesGroup);
      formGroups.add(preferencesGroup);
      for (Setting setting : groups.get(i).getSettings()) {
        formGroups.get(i).getFields().add(setting.getField());
      }
    }
    form.binding(BindingMode.CONTINUOUS);
  }
}
