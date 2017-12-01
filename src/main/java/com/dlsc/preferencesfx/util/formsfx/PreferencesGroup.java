package com.dlsc.preferencesfx.util.formsfx;

/*-
 * ========================LICENSE_START=================================
 * FormsFX
 * %%
 * Copyright (C) 2017 DLSC Software & Consulting
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * =========================LICENSE_END==================================
 */

import com.dlsc.formsfx.model.structure.Field;
import com.dlsc.formsfx.model.structure.Group;
import com.dlsc.formsfx.model.util.TranslationService;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * A section is a kind of group with more options. It can have a title and can
 * be collapsed by the user. Sections represent a more semantically heavy
 * grouping of fields, compared to groups.
 *
 * @author Sacha Schmid
 * @author Rinesch Murugathas
 */
public class PreferencesGroup extends Group {

  /**
   * The title acts as a description for the group. It is always visible to
   * the user and tells them how the contained fields are grouped.
   * This property is translatable if a {@link TranslationService} is set on
   * the containing form.
   */
  private final StringProperty titleKey = new SimpleStringProperty("");
  private final StringProperty title = new SimpleStringProperty("");

  private PreferencesGroupRenderer renderer;

  /**
   * {@inheritDoc}
   */
  private PreferencesGroup(Field... fields) {
    super(fields);

    // Whenever the title's key changes, update the displayed value based
    // on the new translation.

    titleKey.addListener((observable, oldValue, newValue) ->
        title.setValue(translationService.translate(newValue)));
  }

  /**
   * Creates a new section containing the given fields.
   *
   * @param fields The fields to be included in the section.
   * @return Returns a new {@code Section}.
   */
  public static PreferencesGroup of(Field... fields) {
    return new PreferencesGroup(fields);
  }

  /**
   * Sets the title property of the current group.
   *
   * @param newValue The new value for the title property. This can be the title
   *                 itself or a key that is then used for translation.
   * @return Returns the current group to allow for chaining.
   * @see TranslationService
   */
  public Group title(String newValue) {
    if (isI18N()) {
      titleKey.set(newValue);
    } else {
      title.set(newValue);
    }

    return this;
  }

  /**
   * {@inheritDoc}
   */
  void translate(TranslationService newValue) {
    translationService = newValue;

    if (!isI18N()) {
      return;
    }

    if (titleKey.get() == null || titleKey.get().isEmpty()) {
      titleKey.setValue(title.get());
    } else {
      title.setValue(translationService.translate(titleKey.get()));
    }

    fields.forEach(f -> f.translate(translationService));
  }

  public String getTitle() {
    return title.get();
  }

  public StringProperty titleProperty() {
    return title;
  }

  public PreferencesGroupRenderer getRenderer() {
    return renderer;
  }

  public void setRenderer(PreferencesGroupRenderer renderer) {
    this.renderer = renderer;
  }
}
