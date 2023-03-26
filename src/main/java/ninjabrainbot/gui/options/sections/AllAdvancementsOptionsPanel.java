package ninjabrainbot.gui.options.sections;

import javax.swing.border.EmptyBorder;

import ninjabrainbot.gui.frames.OptionsFrame;
import ninjabrainbot.gui.components.preferences.CheckboxPanel;
import ninjabrainbot.gui.components.layout.StackPanel;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.util.I18n;

@SuppressWarnings("serial")
public class AllAdvancementsOptionsPanel extends StackPanel {

	public AllAdvancementsOptionsPanel(StyleManager styleManager, NinjabrainBotPreferences preferences) {
		setOpaque(false);
		setBorder(new EmptyBorder(OptionsFrame.PADDING, OptionsFrame.PADDING, OptionsFrame.PADDING, OptionsFrame.PADDING));

		add(new CheckboxPanel(styleManager, I18n.get("settings.enable_all_advancements_mode"), preferences.allAdvancements));
	}

}
