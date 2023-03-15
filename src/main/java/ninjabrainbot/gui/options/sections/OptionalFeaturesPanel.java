package ninjabrainbot.gui.options.sections;

import ninjabrainbot.event.SubscriptionHandler;
import ninjabrainbot.gui.panels.ThemedTabbedPane;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.util.I18n;

@SuppressWarnings("serial")
public class OptionalFeaturesPanel extends ThemedTabbedPane {

	public OptionalFeaturesPanel(StyleManager styleManager, NinjabrainBotPreferences preferences, SubscriptionHandler subscriptionHandler) {
		super(styleManager);
		addTab(I18n.get("settings.general"), new GeneralOptionalFeaturesOptionsPanel(styleManager, preferences));
		addTab(I18n.get("settings.all_advancements"), new AllAdvancementsOptionsPanel(styleManager, preferences));
		addTab(I18n.get("settings.high_precision"), new HighPrecisionOptionsPanel(styleManager, preferences, subscriptionHandler));
	}

}
