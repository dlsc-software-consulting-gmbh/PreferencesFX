package com.dlsc.preferencesfx.util;

import static com.dlsc.preferencesfx.util.Ascii.containsIgnoreCase;

import com.dlsc.preferencesfx.model.Category;
import com.dlsc.preferencesfx.model.Group;
import com.dlsc.preferencesfx.model.PreferencesFxModel;
import com.dlsc.preferencesfx.model.Setting;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import org.eclipse.fx.ui.controls.tree.FilterableTreeItem;
import org.eclipse.fx.ui.controls.tree.TreeItemPredicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles everything related to searching in the{@link Category}, {@link Group}
 * and {@link Setting}.
 *
 * @author François Martin
 * @author Marco Sanfratello
 */
public class SearchHandler {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(SearchHandler.class.getName());

  private PreferencesFxModel model;

  private List<Category> flatCategoriesLst;
  private List<Setting> flatSettingsLst;
  private List<Group> flatGroupsLst;
  private List<Category> filteredCategoriesLst;
  private List<Setting> filteredSettingsLst;
  private List<Group> filteredGroupsLst;
  private int categoryMatches;
  private int settingMatches;
  private int groupMatches;
  private HashMap<Group, Category> groupCategoryMap;
  private HashMap<Setting, Category> settingCategoryMap;

  private StringProperty searchText = new SimpleStringProperty();

  /**
   * Represents the category which is matched by the search and should ultimately be displayed.
   */
  private ObjectProperty<Category> categoryMatch = new SimpleObjectProperty<>();

  /**
   * Decides whether a {@link TreeItem} should be shown in the {@link TreeView} or not.
   * If result is true, it will be shown, if the result is false, it will be hidden.
   */
  private Predicate<Category> filterPredicate = category -> {
    // look in category description for matches
    String searchText = model.getSearchText();
    boolean categoryMatch = containsIgnoreCase(category.getDescription(), searchText);
    boolean settingMatch = false;
    boolean groupMatch = false;
    if (category.getGroups() != null) {
      // look in settings too
      settingMatch = category.getGroups().stream()
          .map(Group::getSettings)      // get settings from groups
          .flatMap(Collection::stream)  // flatten all lists of settings to settings
          .filter(setting -> !Strings.isNullOrEmpty(setting.getDescription()))
          .anyMatch(setting -> containsIgnoreCase(setting.getDescription(), searchText));
      // look in groups too
      groupMatch = category.getGroups().stream()
          .filter(group -> !Strings.isNullOrEmpty(group.getDescription()))
          .anyMatch(group -> containsIgnoreCase(group.getDescription(), searchText));
    }
    return categoryMatch || settingMatch || groupMatch;
  };

  /**
   * Initializes the SearchHandler by initially creating all necessary lists
   * for filtering and setting up the bindings.
   *
   * @param searchText        textProperty of a TextField where the search string is being input
   * @param predicateProperty of the rootItem of a {@link FilterableTreeItem}
   * @apiNote Must be called to make the filtering work.
   */
  public void init(
      PreferencesFxModel model,
      StringProperty searchText,
      ObjectProperty<TreeItemPredicate<Category>> predicateProperty
  ) {
    this.model = model;
    initializeSearch();
    initializeSearchText(searchText);
    bindFilterPredicate(predicateProperty);
  }

  private void initializeSearch() {
    flatCategoriesLst = model.getFlatCategoriesLst();
    settingCategoryMap = PreferencesFxUtils.mapSettingsToCategories(flatCategoriesLst);
    groupCategoryMap = PreferencesFxUtils.mapGroupsToCategories(flatCategoriesLst);
    flatSettingsLst = PreferencesFxUtils.categoriesToSettings(flatCategoriesLst);
    flatGroupsLst = PreferencesFxUtils.categoriesToGroups(flatCategoriesLst);
  }

  /**
   * Initializes the search text by binding it and then adding a listener to react to changes.
   */
  public void initializeSearchText(StringProperty searchText) {
    bindSearchText(searchText);
    initializeSearchTextListener();
  }

  /**
   * Reacts upon changes in the search text.
   * If the search text is empty, everything will be unmarked, else the search will be updated.
   */
  private void initializeSearchTextListener() {
    searchText.addListener((observable, oldText, newText) -> {
      if (newText.equals("")) { // empty search -> doesn't match anything!
        resetSearch();
      } else {
        updateSearch(newText);
      }
    });
  }

  private void resetSearch() {
    setCategoryMatch(null); // no categories match
    unmarkEverything();
  }

  /**
   * Makes sure this class is aware of the current text which is being searched for.
   *
   * @param searchText textProperty of a TextField where the search string is being input
   */
  private void bindSearchText(StringProperty searchText) {
    this.searchText.bind(searchText);
  }

  /**
   * Binds the predicateProperty to ensure filtering according to the searchText.
   *
   * @param predicateProperty of the rootItem of a {@link FilterableTreeItem}
   */
  public void bindFilterPredicate(ObjectProperty<TreeItemPredicate<Category>> predicateProperty) {
    predicateProperty.bind(Bindings.createObjectBinding(() -> {
      if (searchText.get() == null || searchText.get().isEmpty()) {
        return null;
      }
      return TreeItemPredicate.create(filterPredicate);
    }, searchText));
  }

  /**
   * Updates the search based on a new {@code searchText}.
   *
   * @param searchText the new text to be searched for
   * @implNote Filters the lists, sets the category match, unmarks everything and marks all matches.
   */
  public void updateSearch(String searchText) {
    updateFilteredLists(searchText);
    setCategoryMatch(getSelectedCategoryByMatch());
    unmarkEverything();
    markMatches();
  }

  private void updateFilteredLists(String searchText) {
    filteredCategoriesLst =
        PreferencesFxUtils.filterCategoriesByDescription(flatCategoriesLst, searchText);
    filteredSettingsLst =
        PreferencesFxUtils.filterSettingsByDescription(flatSettingsLst, searchText);
    filteredGroupsLst =
        PreferencesFxUtils.filterGroupsByDescription(flatGroupsLst, searchText);
    categoryMatches = filteredCategoriesLst.size();
    settingMatches = filteredSettingsLst.size();
    groupMatches = filteredGroupsLst.size();
    LOGGER.trace("Matched Categories: " + categoryMatches);
    LOGGER.trace("Matched Settings: " + settingMatches);
    LOGGER.trace("Matched Groups: " + groupMatches);
  }

  // asciidoctor Documentation - tag::compareMatches[]
  private Category getSelectedCategoryByMatch() {
    // Strategy: Go from most specific match to most unspecific match
    Category firstFilteredSetting =
        filteredSettingsLst.size() == 0 ? null : settingCategoryMap.get(filteredSettingsLst.get(0));
    Category firstFilteredGroup =
        filteredGroupsLst.size() == 0 ? null : groupCategoryMap.get(filteredGroupsLst.get(0));
    Category firstFilteredCategory =
        filteredCategoriesLst.size() == 0 ? null : filteredCategoriesLst.get(0);
    return compareMatches(
        firstFilteredSetting, firstFilteredGroup, firstFilteredCategory,
        settingMatches, groupMatches, categoryMatches
    );
  }
  // asciidoctor Documentation - end::compareMatches[]

  private void unmarkEverything() {
    flatCategoriesLst.forEach(Category::unmarkAll);
  }

  private void markMatches() {
    if (settingMatches >= 1) {
      filteredSettingsLst.forEach(Setting::mark);
    }
    if (groupMatches >= 1) {
      filteredGroupsLst.forEach(Group::mark);
    }
  }

  /**
   * Compares three categories with decreasing priority from the first to the last category.
   * {@see developer reference} for further information
   *
   * @param setting       category to return, if settingsMatch is chosen
   * @param group         category to return, if groupMatch is chosen
   * @param category      category to return, if categoryMatch is chosen
   * @param settingMatch  amount of settings which match
   * @param groupMatch    amount of groups which match
   * @param categoryMatch amount of categories which match
   * @return the category with the least amount of matches, taking into account the priority
   */
  public Category compareMatches(Category setting, Category group, Category category,
                                 int settingMatch, int groupMatch, int categoryMatch) {
    LOGGER.trace(String.format("compareMatches: settingMatch: %s, groupMatch: %s, "
        + "categoryMatch: %s", settingMatch, groupMatch, categoryMatch));
    if (settingMatch == 0 && groupMatch == 0 && categoryMatch == 0) { // if all values are 0
      return null;
      // if all values are equal to each other
    } else if (settingMatch == groupMatch && settingMatch == categoryMatch) {
      return setting;
    } else if (settingMatch == 1) {
      return setting;
    } else if (groupMatch == 1) {
      return group;
    } else if (categoryMatch == 1) {
      return category;
    } else if (settingMatch != 0 && groupMatch == 0 && categoryMatch == 0) {
      return setting;
    } else if (settingMatch == 0 && groupMatch != 0 && categoryMatch == 0) {
      return group;
    } else if (settingMatch == 0 && groupMatch == 0 && categoryMatch != 0) {
      return category;
    } else if (settingMatch == 0) {
      // can only be categoryMatch, if it's smaller than groupMatch
      if (categoryMatch < groupMatch) {
        return category;
      } else {                // from here it can only be groupMatch if settingMatch is 0
        return group;
      }
    } else if (groupMatch == 0) {
      // can only be settingMatch, if it's smaller or equal to categoryMatch
      if (settingMatch <= categoryMatch) {
        return setting;
      } else {                // from here it can only be categoryMatch
        return category;
      }
    } else if (categoryMatch == 0) {
      if (groupMatch < settingMatch) { // can only be groupMatch, if it's smaller than settingMatch
        return group;
      } else {                // from here it can only be settingMatch
        return setting;
      } // from here, no more 0 or 1 values are present => comparisons can be made safely!
    } else if (settingMatch <= groupMatch && settingMatch <= categoryMatch) {
      return setting;
    } else if (groupMatch <= categoryMatch) {
      return group;
    }
    return category;
  }

  public Category getCategoryMatch() {
    return categoryMatch.get();
  }

  private void setCategoryMatch(Category categoryMatch) {
    LOGGER.trace("Set Category Match to: " + categoryMatch);
    this.categoryMatch.set(categoryMatch);
  }

  public ReadOnlyObjectProperty<Category> categoryMatchProperty() {
    return categoryMatch;
  }
}
