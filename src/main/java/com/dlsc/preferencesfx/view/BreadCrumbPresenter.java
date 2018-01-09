package com.dlsc.preferencesfx.view;

import static com.dlsc.preferencesfx.util.Constants.BREADCRUMB_DELIMITER;

import com.dlsc.preferencesfx.model.Category;
import com.dlsc.preferencesfx.model.PreferencesFxModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.BreadCrumbBar;

public class BreadCrumbPresenter implements Presenter {
  private static final Logger LOGGER =
      LogManager.getLogger(BreadCrumbPresenter.class.getName());
  private final PreferencesFxModel model;
  private final BreadCrumbView breadCrumbView;

  public BreadCrumbPresenter(PreferencesFxModel model, BreadCrumbView breadCrumbView) {
    this.model = model;
    this.breadCrumbView = breadCrumbView;
    init();
    setupBreadCrumbBar();
  }

  /**
   * {@inheritDoc}
   */
  public void setupValueChangedListeners() {
    // When the displayed category changes, it reloads the BreadcrumbBar
    model.displayedCategoryProperty().addListener(e -> setupBreadCrumbBar());

    // Sets the displayed category when clicking on a breadcrumb
    breadCrumbView.breadCrumbBar.setOnCrumbAction(event ->
        model.setDisplayedCategory(event.getSelectedCrumb().getValue())
    );
  }

  /**
   * Sets up the BreadcrumbBar depending on the displayed category.
   */
  public void setupBreadCrumbBar() {
    String[] stringArr = model.getDisplayedCategory().getBreadcrumb().split(BREADCRUMB_DELIMITER);
    Category[] categories = new Category[stringArr.length];

    // Collecting all parent categories from the displayed category using the breadcrumb.
    // There will always be at least one category which will be added to the breadcrumb.
    categories[0] = searchCategory(stringArr[0]);

    // If there are more than one category in the stringArr[], they will be added. For this reason
    // the Integer in the loop starts with one, thus only the second element in the array is needed.
    for (int i = 1; i < stringArr.length; ++i) {
      stringArr[i] = stringArr[i - 1] + BREADCRUMB_DELIMITER + stringArr[i];
      categories[i] = searchCategory(stringArr[i]);
    }

    breadCrumbView.breadcrumbsItm = BreadCrumbBar.buildTreeModel(categories);
    breadCrumbView.breadCrumbBar.setSelectedCrumb(breadCrumbView.breadcrumbsItm);
  }

  /**
   * Searches in all categories for the category that matches a breadcrumb.
   *
   * @param breadcrumb the breadcrumb, which the matching category should have
   * @return a matching category or null if nothing is found
   */
  private Category searchCategory(String breadcrumb) {
    return model.getFlatCategoriesLst().stream().filter(
        cat -> cat.getBreadcrumb().equals(breadcrumb)
    ).findAny().orElse(null);
  }
}
