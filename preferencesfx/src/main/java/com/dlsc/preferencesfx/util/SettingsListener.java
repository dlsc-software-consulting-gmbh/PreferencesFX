package com.dlsc.preferencesfx.util;

import com.dlsc.preferencesfx.model.Setting;

public interface SettingsListener<T> {
	void settingChanged(Setting setting, T oldValue, T newValue);
}
