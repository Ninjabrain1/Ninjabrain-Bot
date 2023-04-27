package ninjabrainbot.io.preferences.enums;

import ninjabrainbot.io.preferences.IMultipleChoicePreferenceDataType;
import ninjabrainbot.util.I18n;

public enum AllAdvancementsToggleType implements IMultipleChoicePreferenceDataType {

	Automatic(I18n.get("settings.all_advancements.automatic")), Hotkey(I18n.get("settings.all_advancements.hotkey"));

	final String name;

	AllAdvancementsToggleType(String string) {
		name = string;
	}

	@Override
	public String choiceName() {
		return name;
	}

}
