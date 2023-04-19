package ninjabrainbot.io.preferences.enums;

import ninjabrainbot.gui.style.SizePreference;
import ninjabrainbot.io.preferences.IMultipleChoicePreferenceDataType;

public enum SizeSetting implements IMultipleChoicePreferenceDataType {
	SMALL(SizePreference.REGULAR.name), MEDIUM(SizePreference.LARGE.name), LARGE(SizePreference.EXTRALARGE.name);

	final String name;

	SizeSetting(String string) {
		name = string;
	}

	@Override
	public String choiceName() {
		return name;
	}
}