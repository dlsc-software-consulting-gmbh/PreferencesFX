package com.dlsc.preferencesfx2.view;

import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.model.util.BindingMode;
import com.dlsc.preferencesfx2.formsfx.view.renderer.PreferencesGroup;
import com.dlsc.preferencesfx2.model.Category;
import com.dlsc.preferencesfx2.model.Group;
import com.dlsc.preferencesfx2.model.PreferencesModel;
import com.dlsc.preferencesfx2.model.Setting;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CategoryPresenter implements Presenter {
  private static final Logger LOGGER =
      LogManager.getLogger(CategoryPresenter.class.getName());

  private PreferencesModel model;
  private Category categoryModel;
  private CategoryView categoryView;

  public CategoryPresenter(PreferencesModel model, Category categoryModel, CategoryView categoryView) {
    this.model = model;
    this.categoryModel = categoryModel;
    this.categoryView = categoryView;
    init();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeViewParts() {
    initializeForm(categoryView.form);
    categoryView.initializeFormRenderer();
  }

  /**
   * Fills the {@link Form} with {@link Group} and {@link Setting} of this {@link Category}.
   *
   * @param form the form to be initialized
   */
  private void initializeForm(Form form) {
    // assign groups from this category
    List<Group> groups = categoryModel.getGroups();
    // if there are no groups, initialize them anyways as a list
    if (groups == null) {
      groups = new ArrayList<>();
    }

    // get groups of this form
    List<com.dlsc.formsfx.model.structure.Group> formGroups = form.getGroups();

    // create PreferenceGroups from Groups
    for (int i = 0; i < groups.size(); i++) {
      PreferencesGroup preferencesGroup =
          (PreferencesGroup) PreferencesGroup.of().title(groups.get(i).getDescription());
      groups.get(i).setPreferencesGroup(preferencesGroup);
      formGroups.add(preferencesGroup);
      // fill groups with settings (as FormsFX fields)
      for (Setting setting : groups.get(i).getSettings()) {
        formGroups.get(i).getFields().add(setting.getField());
      }
    }

    // ensures instant persistance of value changes
    form.binding(BindingMode.CONTINUOUS);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setupEventHandlers() {

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setupValueChangedListeners() {

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setupBindings() {

  }

}
