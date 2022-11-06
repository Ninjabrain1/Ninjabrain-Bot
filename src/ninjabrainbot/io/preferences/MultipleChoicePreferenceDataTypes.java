package ninjabrainbot.io.preferences;

import java.util.Locale;

import ninjabrainbot.gui.style.SizePreference;
import ninjabrainbot.gui.style.Theme;
import ninjabrainbot.util.I18n;

public class MultipleChoicePreferenceDataTypes {

	public enum Language implements IMultipleChoicePreferenceDataType {
		EN_US(I18n.getLanguageName(Locale.US)), KO_KR(I18n.getLanguageName(Locale.KOREA)), ZH_CN(I18n.getLanguageName(Locale.SIMPLIFIED_CHINESE)), IT_IT(I18n.getLanguageName(Locale.ITALY));

		final String name;

		Language(String string) {
			name = string;
		}

		@Override
		public String choiceName() {
			return name;
		}
	}

	public enum ThemeSetting implements IMultipleChoicePreferenceDataType {
		LIGHT(Theme.LIGHT.name), DARK(Theme.DARK.name), BLUE(Theme.BLUE.name);

		final String name;

		ThemeSetting(String string) {
			name = string;
		}

		@Override
		public String choiceName() {
			return name;
		}
	}

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

}
