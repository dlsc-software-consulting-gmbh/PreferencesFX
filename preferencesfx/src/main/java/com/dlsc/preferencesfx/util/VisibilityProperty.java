package com.dlsc.preferencesfx.util;

import java.util.function.Function;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;

@FunctionalInterface
public interface VisibilityProperty {
    BooleanProperty get();

    static <T> VisibilityProperty of(Property<T> property, Function<T, Boolean> visibilityFunc) {
        return () -> {
            BooleanProperty visibilityProperty = new SimpleBooleanProperty(true);

            property.addListener((observable, oldValue, newValue) -> visibilityProperty.set(visibilityFunc.apply(newValue)));

            return visibilityProperty;
        };
    }
}
