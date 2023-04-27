package ninjabrainbot.io.preferences.enums;

import ninjabrainbot.io.preferences.IMultipleChoicePreferenceDataType;
import ninjabrainbot.util.I18n;

public enum McVersion implements IMultipleChoicePreferenceDataType {

	PRE_119(I18n.get("settings.mc_version.1")), POST_119(I18n.get("settings.mc_version.2"));

	final String name;

	McVersion(String string) {
		name = string;
	}

	@Override
	public String choiceName() {
		return name;
	}
}