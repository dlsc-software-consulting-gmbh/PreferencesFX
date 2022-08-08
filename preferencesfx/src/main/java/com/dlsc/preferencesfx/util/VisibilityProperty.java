package com.dlsc.preferencesfx.util;

import java.util.function.Function;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;

@FunctionalInterface
public interface VisibilityProperty {
    BooleanProperty get();

    /**
     * Creates a {@link VisibilityProperty} that is based on the given {@link Property}.
     *
     * The {@link VisibilityProperty} value will be set automatically when the given {@link Property} value changes.
     * The new value of the {@link VisibilityProperty} will be the result of the given {@link Function} applied to the
     * new value of the {@link Property}.
     *
     * @param property
     * @param visibilityFunc
     * @return
     * @param <T>
     */
    static <T> VisibilityProperty of(Property<T> property, Function<T, Boolean> visibilityFunc) {
        return () -> {
            BooleanProperty visibilityProperty = new SimpleBooleanProperty(true);

            property.addListener((observable, oldValue, newValue) -> visibilityProperty.set(visibilityFunc.apply(newValue)));

            return visibilityProperty;
        };
    }

    /**
     * Simplified constructor for {@link VisibilityProperty} that is based on the given Boolean type {@link Property}
     *
     * The value of the {@link VisibilityProperty} will be set to the value of the given {@link Property}, i.e.,
     * whenever the referenced {@link Property} is true, the value of the @link VisibilityProperty} will be true as well.
     *
     * @param property
     * @return
     * @param <T>
     */
    static <T> VisibilityProperty of(Property<Boolean> property) {
        return VisibilityProperty.of(property, (newValue) -> newValue);
    }

}
