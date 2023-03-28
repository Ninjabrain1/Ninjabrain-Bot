package ninjabrainbot.gui.options.sections;

import java.awt.FlowLayout;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import ninjabrainbot.gui.frames.OptionsFrame;
import ninjabrainbot.gui.components.preferences.RadioButtonPanel;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.util.I18n;

public class LanguageOptionsPanel extends JPanel {

	public LanguageOptionsPanel(StyleManager styleManager, NinjabrainBotPreferences preferences) {
		setOpaque(false);
		setAlignmentX(0);
		setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		setBorder(new EmptyBorder(2 * OptionsFrame.PADDING, 2 * OptionsFrame.PADDING, 2 * OptionsFrame.PADDING, 2 * OptionsFrame.PADDING));
		add(new RadioButtonPanel(styleManager, I18n.get("settings.language.hint"), preferences.language));
	}

}
