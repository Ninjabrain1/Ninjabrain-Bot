package ninjabrainbot.io.preferences.enums;

import ninjabrainbot.io.preferences.IMultipleChoicePreferenceDataType;
import ninjabrainbot.util.I18n;

public enum MainViewType implements IMultipleChoicePreferenceDataType {

	BASIC(I18n.get("basic")), DETAILED(I18n.get("detailed"));

	final String name;

	MainViewType(String string) {
		name = string;
	}

	@Override
	public String choiceName() {
		return name;
	}
}