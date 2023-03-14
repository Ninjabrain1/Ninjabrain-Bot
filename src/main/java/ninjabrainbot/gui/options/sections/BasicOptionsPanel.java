package ninjabrainbot.gui.options.sections;

import java.awt.GridLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import ninjabrainbot.gui.frames.OptionsFrame;
import ninjabrainbot.gui.options.components.CheckboxPanel;
import ninjabrainbot.gui.options.components.RadioButtonPanel;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.util.I18n;

@SuppressWarnings("serial")
public class BasicOptionsPanel extends JPanel {

	public BasicOptionsPanel(StyleManager styleManager, NinjabrainBotPreferences preferences) {
		setOpaque(false);
		setLayout(new GridLayout(1, 2));
		setBorder(new EmptyBorder(OptionsFrame.PADDING - 3, OptionsFrame.PADDING, OptionsFrame.PADDING, OptionsFrame.PADDING));
		JPanel column1 = new JPanel();
		JPanel column2 = new JPanel();
		column1.setOpaque(false);
		column2.setOpaque(false);
		add(column1);
		add(column2);
		column1.setLayout(new BoxLayout(column1, BoxLayout.Y_AXIS));
		column2.setLayout(new BoxLayout(column2, BoxLayout.Y_AXIS));

		// Column 1
		column1.add(new CheckboxPanel(styleManager, I18n.get("settings.show_nether_coordinates"), preferences.showNetherCoords));
		column1.add(new CheckboxPanel(styleManager, I18n.get("settings.auto_reset"), preferences.autoReset));
		column1.add(new CheckboxPanel(styleManager, I18n.get("settings.always_on_top"), preferences.alwaysOnTop));
		column1.add(new CheckboxPanel(styleManager, I18n.get("settings.translucent_window"), preferences.translucent));
		column1.add(new CheckboxPanel(styleManager, I18n.get("settings.notify_when_a_new_version_is_available"), preferences.checkForUpdates));
		column1.add(Box.createGlue());

		// Column 2
		column2.add(Box.createVerticalStrut(10));
		column2.add(new RadioButtonPanel(styleManager, I18n.get("settings.display_stronghold_location_using"), preferences.strongholdDisplayType));
		column2.add(new RadioButtonPanel(styleManager, I18n.get("settings.view_type"), preferences.view));
		column2.add(new RadioButtonPanel(styleManager, I18n.get("settings.window_size"), preferences.size));
		column2.add(new RadioButtonPanel(styleManager, I18n.get("settings.mc_version"), preferences.mcVersion));
		column2.add(Box.createGlue());
	}

}
