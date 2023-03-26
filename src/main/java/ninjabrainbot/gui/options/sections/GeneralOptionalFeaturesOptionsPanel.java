package ninjabrainbot.gui.options.sections;

import javax.swing.border.EmptyBorder;

import ninjabrainbot.gui.frames.OptionsFrame;
import ninjabrainbot.gui.components.preferences.CheckboxPanel;
import ninjabrainbot.gui.components.layout.StackPanel;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.util.I18n;

@SuppressWarnings("serial")
public class GeneralOptionalFeaturesOptionsPanel extends StackPanel {

	public GeneralOptionalFeaturesOptionsPanel(StyleManager styleManager, NinjabrainBotPreferences preferences) {
		setOpaque(false);
		setBorder(new EmptyBorder(OptionsFrame.PADDING, OptionsFrame.PADDING, OptionsFrame.PADDING, OptionsFrame.PADDING));

		add(new CheckboxPanel(styleManager, I18n.get("settings.show_angle_updates"), preferences.showAngleUpdates));
	}

}
