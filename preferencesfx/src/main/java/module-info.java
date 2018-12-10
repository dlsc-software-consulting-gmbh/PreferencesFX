module com.dlsc.preferencesfx {

    requires transitive javafx.controls;
    requires transitive com.dlsc.formsfx;

    requires de.jensd.fx.glyphs.fontawesome;

    requires org.apache.logging.log4j;
    requires java.prefs;
    requires gson;
    requires org.eclipse.fx.ui.controls;
    requires commons.collections4;
    requires com.google.guava;
    requires java.sql;
    requires org.controlsfx.controls;

    exports com.dlsc.preferencesfx;
    exports com.dlsc.preferencesfx.formsfx.view.controls;
    exports com.dlsc.preferencesfx.formsfx.view.renderer;
    exports com.dlsc.preferencesfx.history;
    exports com.dlsc.preferencesfx.history.view;
    exports com.dlsc.preferencesfx.model;
    exports com.dlsc.preferencesfx.util;
    exports com.dlsc.preferencesfx.view;
}