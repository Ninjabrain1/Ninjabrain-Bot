package ninjabrainbot.io.preferences.enums;

import ninjabrainbot.io.preferences.IMultipleChoicePreferenceDataType;
import ninjabrainbot.util.I18n;

public enum StrongholdDisplayType implements IMultipleChoicePreferenceDataType {
	FOURFOUR("(4, 4)"), EIGHTEIGHT("(8, 8)"), CHUNK(I18n.get("chunk"));

	final String name;

	StrongholdDisplayType(String string) {
		name = string;
	}

	@Override
	public String choiceName() {
		return name;
	}
}