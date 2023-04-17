package ninjabrainbot.gui.options;

import java.util.Locale;

import ninjabrainbot.gui.components.preferences.IMultipleChoiceOption;
import ninjabrainbot.util.I18n;

public class LanguageOption implements IMultipleChoiceOption {

	private final String name;
	public final Locale locale;

	public LanguageOption(Locale locale) {
		this.name = locale.getDisplayName(I18n.LANGUAGE) + (I18n.LANGUAGE != Locale.US ? (" - " + locale.getDisplayLanguage(Locale.US)) : "");
		this.locale = locale;
	}

	@Override
	public String choiceName() {
		return name;
	}

}
