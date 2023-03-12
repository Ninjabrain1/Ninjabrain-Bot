package ninjabrainbot.gui.options.sections;

import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.util.I18n;

@SuppressWarnings("serial")
public class OptionalFeaturesPanel extends ThemedTabbedPane {

	public OptionalFeaturesPanel(StyleManager styleManager, NinjabrainBotPreferences preferences) {
		super(styleManager);
		addTab(I18n.get("settings.all_advancements"), new AllAdvancementsOptionsPanel(styleManager, preferences));
	}

}
