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

    for (int i = 0; i < stringArr.length; ++i) {
      String crumb = stringArr[0];
      for (int j = 1; j <= i; ++j) {
        crumb += BREADCRUMB_DELIMITER + stringArr[j];
      }
      String breadcrumb = crumb;
      Category category = model.getFlatCategoriesLst().stream().filter(
          cat -> cat.getBreadcrumb().equals(breadcrumb)
      ).findFirst().get();
      categories.add(category);
    }

    Category[] categoriesArr = new Category[categories.size()];
    for (int i = 0; i < categoriesArr.length; ++i) {
      categoriesArr[i] = categories.get(i);
    }
    breadCrumbView.breadcrumbsItm = BreadCrumbBar.buildTreeModel(categoriesArr);
    breadCrumbView.breadCrumbBar.setSelectedCrumb(breadCrumbView.breadcrumbsItm);
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
