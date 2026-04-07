package com.dlsc.preferencesfx.demo.dialog_i18n;

import com.dlsc.preferencesfx.PreferencesFx;
import com.dlsc.preferencesfx.model.Category;
import com.dlsc.preferencesfx.model.Setting;
import javafx.application.Application;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.Dialog;
import javafx.stage.Stage;

import java.util.Arrays;

/**
 * A demo using dialog mode, because embedded mode doesn't show dialog buttons.
 */
public class AppStarter extends Application {

    public static void main(String [] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        ListProperty<String> themeItems = new SimpleListProperty<>(
                FXCollections.observableArrayList(Arrays.asList("Light theme", "Dark theme"))
        );
        ObjectProperty<String> themeProperty = new SimpleObjectProperty<>();

        Category general = Category.of("إعدادات عامة",
                Setting.of("المظهر", themeItems, themeProperty)
        );
        PreferencesFx preferencesFx = PreferencesFx.of(this.getClass(), general);
        preferencesFx.getView().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);

        preferencesFx.setDialogButtonsText("إغلاق", "موافق","تطبيق","إلغاء");
        preferencesFx.show();
        Stage preferencesStage = (Stage) preferencesFx.getView().getScene().getWindow();
        preferencesStage.setMaximized(true);
    }
}
