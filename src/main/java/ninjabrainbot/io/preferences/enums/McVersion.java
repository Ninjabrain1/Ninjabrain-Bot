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

	public static McVersion fromVersionString(String versionString) {
		if (versionString == null || versionString.isEmpty())
			return null;

		String[] versionNumbers = versionString.split("\\.");
		if (versionNumbers.length < 2)
			return null;

		try {
			int majorVersion = Integer.parseInt(versionNumbers[0]);
			if (majorVersion < 26 && majorVersion != 1)
				return null;

			int minorVersion = majorVersion != 1 ? majorVersion : Integer.parseInt(versionNumbers[1]);
			return minorVersion < 19 ? McVersion.PRE_119 : McVersion.POST_119;
		} catch (NumberFormatException ignored) {
			return null;
		}
	}
}