/*******************************************************************************
 * Copyright (c) 2014 EM-SOFTWARE and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Christoph Keimel <c.keimel@emsw.de> - initial API and implementation
 *******************************************************************************/
package com.dlsc.preferencesfx.view;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.cell.CheckBoxTreeCell;

import java.util.function.Predicate;

/**
 * An extension of {@link TreeItem} with the possibility to filter its children. To enable filtering
 * it is necessary to set the {@link TreeItemPredicate}. If a predicate is set, then the tree item
 * will also use this predicate to filter its children (if they are of the type FilterableTreeItem).
 *
 * A tree item that has children will not be filtered. The predicate will only be evaluated, if the
 * tree item is a leaf. Since the predicate is also set for the child tree items, the tree item in question
 * can turn into a leaf if all its children are filtered.
 * 
 * This class extends {@link CheckBoxTreeItem} so it can, but does not need to be, used in conjunction
 * with {@link CheckBoxTreeCell} cells. 
 *
 * @param <T> The type of the {@link #getValue() value} property within {@link TreeItem}.
 */
public class FilterableTreeItem<T> extends CheckBoxTreeItem<T> {

	private final ObservableList<TreeItem<T>> sourceList = FXCollections.observableArrayList();
	private final FilteredList<TreeItem<T>> filteredList =new FilteredList<>(sourceList);
	private final ObjectProperty<TreeItemPredicate<T>> predicate = new SimpleObjectProperty<>();

	/**
	 * Creates a new {@link TreeItem} with sorted children.
	 *
	 * @param value the value of the {@link TreeItem}
	 */
	public FilterableTreeItem(T value) {
		super(value);

		filteredList.predicateProperty().bind(Bindings.createObjectBinding(() -> {
			Predicate<TreeItem<T>> p =  child -> {
				// Set the predicate of child items to force filtering
				if (child instanceof FilterableTreeItem) {
					FilterableTreeItem<T> filterableChild = (FilterableTreeItem<T>) child;
					filterableChild.setPredicate(predicate.get());
				}

				// If there is no predicate, keep this tree item
				if (predicate.get() == null) {
					return true;
				}

				// If there are children, keep this tree item
				if (child.getChildren().size() > 0) {
					return true;
				}

				// Otherwise ask the TreeItemPredicate
				return predicate.get().test(this, child.getValue());
			};
			return p;
		}, predicate));

		Bindings.bindContent(getChildren(), getBackingList());
	}
	
	/**
	 * @return the backing list
	 */
	protected ObservableList<TreeItem<T>> getBackingList() {
		return filteredList;
	}

	/**
	 * Returns the list of children that is backing the filtered list.
	 * @return underlying list of children
	 */
	public ObservableList<TreeItem<T>> getInternalChildren() {
		return sourceList;
	}

	/**
	 * @return the predicate property
	 */
	public final ObjectProperty<TreeItemPredicate<T>> predicateProperty() {
        return predicate;
    }

	/**
	 * @return the predicate
	 */
    public final TreeItemPredicate<T> getPredicate() {
        return predicate.get();
    }

    /**
     * Set the predicate
     * @param predicate the predicate
     */
    public final void setPredicate(TreeItemPredicate<T> predicate) {
    	this.predicate.set(predicate);
    }
}