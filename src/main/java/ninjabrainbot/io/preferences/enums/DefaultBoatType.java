package ninjabrainbot.io.preferences.enums;

import ninjabrainbot.io.preferences.IMultipleChoicePreferenceDataType;
import ninjabrainbot.util.I18n;

public enum DefaultBoatType implements IMultipleChoicePreferenceDataType {

	GRAY(I18n.get("settings.boat_eye.gray_boat")), BLUE(I18n.get("settings.boat_eye.blue_boat")), GREEN(I18n.get("settings.boat_eye.green_boat"));

	final String name;

	DefaultBoatType(String string) {
		name = string;
	}

	@Override
	public String choiceName() {
		return name;
	}
}