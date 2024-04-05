package ninjabrainbot.gui.options.sections;

import ninjabrainbot.event.DisposeHandler;
import ninjabrainbot.gui.components.layout.ThemedTabbedPane;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.util.I18n;

public class OptionalFeaturesPanel extends ThemedTabbedPane {

	public OptionalFeaturesPanel(StyleManager styleManager, NinjabrainBotPreferences preferences, DisposeHandler disposeHandler) {
		super(styleManager);
		addTab(I18n.get("settings.general"), new GeneralOptionalFeaturesOptionsPanel(styleManager, preferences));
		addTab(I18n.get("settings.all_advancements"), new AllAdvancementsOptionsPanel(styleManager, preferences));
		addTab(I18n.get("settings.angle_adjustment"), new AngleAdjustmentOptionsPanel(styleManager, preferences, disposeHandler));
		addTab(I18n.get("settings.boat_eye"), new BoatMeasurementOptionsPanel(styleManager, preferences, disposeHandler));
	}

}
