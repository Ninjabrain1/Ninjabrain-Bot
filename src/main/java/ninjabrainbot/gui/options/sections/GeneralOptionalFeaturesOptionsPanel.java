package ninjabrainbot.gui.options.sections;

import javax.swing.border.EmptyBorder;

import ninjabrainbot.gui.components.layout.TitledDivider;
import ninjabrainbot.gui.frames.OptionsFrame;
import ninjabrainbot.gui.components.preferences.CheckboxPanel;
import ninjabrainbot.gui.components.layout.StackPanel;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.util.I18n;

public class GeneralOptionalFeaturesOptionsPanel extends StackPanel {

	public GeneralOptionalFeaturesOptionsPanel(StyleManager styleManager, NinjabrainBotPreferences preferences) {
		setOpaque(false);
		setBorder(new EmptyBorder(OptionsFrame.PADDING, OptionsFrame.PADDING, OptionsFrame.PADDING, OptionsFrame.PADDING));

		add(new CheckboxPanel(styleManager, I18n.get("settings.show_angle_updates"), preferences.showAngleUpdates));
		add(new TitledDivider(styleManager,"Information messages"));
		add(new CheckboxPanel(styleManager, I18n.get("settings.information.enable_mismeasure_warning"), preferences.informationMismeasureEnabled));
		add(new CheckboxPanel(styleManager, I18n.get("settings.information.enable_portal_linking_warning"), preferences.informationPortalLinkingEnabled));
		add(new CheckboxPanel(styleManager, I18n.get("settings.information.enable_combined_certainty_information"), preferences.informationCombinedCertaintyEnabled));
		add(new CheckboxPanel(styleManager, I18n.get("settings.information.enable_direction_help_information"), preferences.informationDirectionHelpEnabled));
	}

}
