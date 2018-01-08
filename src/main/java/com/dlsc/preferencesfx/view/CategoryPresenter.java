package com.dlsc.preferencesfx.view;

import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.model.util.BindingMode;
import com.dlsc.preferencesfx.formsfx.view.renderer.PreferencesFxGroup;
import com.dlsc.preferencesfx.model.Category;
import com.dlsc.preferencesfx.model.Group;
import com.dlsc.preferencesfx.model.PreferencesFxModel;
import com.dlsc.preferencesfx.model.Setting;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CategoryPresenter implements Presenter {
  private static final Logger LOGGER =
      LogManager.getLogger(CategoryPresenter.class.getName());

  private PreferencesFxModel model;
  private Category categoryModel;
  private CategoryView categoryView;
  private final BreadCrumbPresenter breadCrumbPresenter;

  public CategoryPresenter(PreferencesFxModel model, Category categoryModel, CategoryView categoryView, BreadCrumbPresenter breadCrumbPresenter) {
    this.model = model;
    this.categoryModel = categoryModel;
    this.categoryView = categoryView;
    this.breadCrumbPresenter = breadCrumbPresenter;
    init();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeViewParts() {
    initializeForm(categoryView.form);
    categoryView.initializeFormRenderer();
    addI18nListener();
  }

  /**
   * Updates the internal FormsFX form with the most current TranslationService.
   * Makes sure the group descriptions are updated with changing locale.
   */
  void addI18nListener() {
    model.translationServiceProperty().addListener((observable, oldValue, newValue) -> {
      if (oldValue != newValue) {
        categoryView.form.i18n(newValue);
        newValue.addListener(categoryModel::updateGroupDescriptions);
        if (!Objects.equals(breadCrumbPresenter, null)) {
          newValue.addListener(breadCrumbPresenter::setupBreadCrumbBar);
        }
        categoryModel.updateGroupDescriptions();
      }
    });
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
      PreferencesFxGroup preferencesGroup =
          (PreferencesFxGroup) PreferencesFxGroup.of().title(groups.get(i).getDescription());
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
