package com.dlsc.preferencesfx.view;

import static com.dlsc.preferencesfx.util.Constants.BREADCRUMB_DELIMITER;

import com.dlsc.preferencesfx.model.Category;
import com.dlsc.preferencesfx.model.PreferencesFxModel;
import java.util.ArrayList;
import java.util.List;
import org.controlsfx.control.BreadCrumbBar;

public class BreadCrumbPresenter {
  private final PreferencesFxModel model;
  private final BreadCrumbView breadCrumbView;

  public BreadCrumbPresenter(PreferencesFxModel model, BreadCrumbView breadCrumbView) {
    this.model = model;
    this.breadCrumbView = breadCrumbView;
    setupListeners();
    setupBreadCrumbBar();
  }

  private void setupBreadCrumbBar() {
    List<Category> categories = new ArrayList<>();
    String selectedBreadcrumb = model.getDisplayedCategory().getBreadcrumb();
    String[] stringArr = selectedBreadcrumb.split(BREADCRUMB_DELIMITER);

    categories = addToCategory(stringArr[0], categories);
    for (int i = 1; i < stringArr.length; ++i) {
      stringArr[i] = stringArr[i - 1] + BREADCRUMB_DELIMITER + stringArr[i];
      categories = addToCategory(stringArr[i], categories);
    }

    Category[] categoriesArr = new Category[categories.size()];
    for (int i = 0; i < categoriesArr.length; ++i) {
      categoriesArr[i] = categories.get(i);
    }

    breadCrumbView.breadcrumbsItm = BreadCrumbBar.buildTreeModel(categoriesArr);
    breadCrumbView.breadCrumbBar.setSelectedCrumb(breadCrumbView.breadcrumbsItm);
  }

  private List<Category> addToCategory(String breadcrumb, List<Category> categories) {
    Category category = model.getFlatCategoriesLst().stream().filter(
        cat -> cat.getBreadcrumb().equals(breadcrumb)
    ).findFirst().get();
    categories.add(category);
    return categories;
  }

  private void setupListeners() {
    model.displayedCategoryProperty().addListener(e -> setupBreadCrumbBar());

    breadCrumbView.breadCrumbBar.setOnCrumbAction(event ->
        model.setDisplayedCategory(
            model.getCategories().get(
                model.getCategories().indexOf(event.getSelectedCrumb().getValue())
            )
        )
    );
  }
}
