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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Contains presenter logic of the {@link CategoryView}.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class CategoryPresenter implements Presenter {
  private static final Logger LOGGER =
      LoggerFactory.getLogger(CategoryPresenter.class.getName());

  private PreferencesFxModel model;
  private Category categoryModel;
  private CategoryView categoryView;
  private final BreadCrumbPresenter breadCrumbPresenter;
  private Form form;

  /**
   * Constructs a new presenter for the {@link CategoryView}.
   *
   * @param model               the model of PreferencesFX
   * @param categoryModel       the category which is being represented in the view
   * @param categoryView        corresponding view to this presenter
   * @param breadCrumbPresenter the presenter of the corresponding {@link BreadCrumbView} as found
   *                            in the corresponding view to this presenter
   */
  public CategoryPresenter(
      PreferencesFxModel model,
      Category categoryModel,
      CategoryView categoryView,
      BreadCrumbPresenter breadCrumbPresenter
  ) {
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
    form = createForm();
    categoryView.initializeFormRenderer(form);
    addI18nListener();
    addInstantPersistenceListener();
  }

  private void addInstantPersistenceListener() {
    model.instantPersistentProperty().addListener((observable, oldPersistence, newPersistence) -> {
      applyInstantPersistence(newPersistence, form);
    });
  }

  /**
   * Updates the internal FormsFX form with the most current TranslationService.
   * Makes sure the group descriptions are updated with changing locale.
   */
  private void addI18nListener() {
    model.translationServiceProperty().addListener((observable, oldValue, newValue) -> {
      if (oldValue != newValue) {
        form.i18n(newValue);
        newValue.addListener(categoryModel::updateGroupDescriptions);
        if (!Objects.equals(breadCrumbPresenter, null)) {
          newValue.addListener(breadCrumbPresenter::setupBreadCrumbBar);
        }
        categoryModel.updateGroupDescriptions();
      }
    });
  }

  /**
   * Creates a {@link Form} with {@link Group} and {@link Setting} of this {@link Category}.
   *
   * @return the created form.
   */
  private Form createForm() {
    Form form = Form.of();
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
        formGroups.get(i).getElements().add(setting.getField());
      }
    }

    applyInstantPersistence(model.isInstantPersistent(), form);

    return form;
  }

  private void applyInstantPersistence(boolean instantPersistent, Form form) {
    LOGGER.trace("Applying instant persistence: " + instantPersistent);
    BindingMode persistence;
    if (instantPersistent) {
      // instant persistence is on
      persistence = BindingMode.CONTINUOUS;
    } else {
      // instant persistence is off
      persistence = BindingMode.PERSISTENT;
    }
    form.binding(persistence);
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
