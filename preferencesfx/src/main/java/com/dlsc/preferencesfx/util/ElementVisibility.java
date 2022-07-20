package com.dlsc.preferencesfx.util;

import javafx.beans.property.BooleanProperty;

@FunctionalInterface
public interface ElementVisibility {
    BooleanProperty visible();
}
