package ninjabrainbot.gui.options.sections;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import ninjabrainbot.gui.frames.OptionsFrame;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;

@SuppressWarnings("serial")
public class AllAdvancementsOptionsPanel extends JPanel {

	public AllAdvancementsOptionsPanel(StyleManager styleManager, NinjabrainBotPreferences preferences) {
		setOpaque(false);
		setBorder(new EmptyBorder(OptionsFrame.PADDING - 3, OptionsFrame.PADDING, OptionsFrame.PADDING, OptionsFrame.PADDING));

	}

}
