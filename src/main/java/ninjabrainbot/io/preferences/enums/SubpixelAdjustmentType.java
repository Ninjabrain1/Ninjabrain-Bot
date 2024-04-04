package ninjabrainbot.io.preferences.enums;

import ninjabrainbot.io.preferences.IMultipleChoicePreferenceDataType;
import ninjabrainbot.util.I18n;

public enum SubpixelAdjustmentType implements IMultipleChoicePreferenceDataType {

	DEFAULT(I18n.get("settings.subpixel_adjustment.default")),
	TALL(I18n.get("settings.subpixel_adjustment.tall_resolution")),
	CUSTOM(I18n.get("settings.subpixel_adjustment.custom_adjustment"));

	final String name;

	SubpixelAdjustmentType(String string) {
		name = string;
	}

	@Override
	public String choiceName() {
		return name;
	}
}