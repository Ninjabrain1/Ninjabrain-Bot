package ninjabrainbot.gui.options.sections;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import ninjabrainbot.gui.frames.OptionsFrame;
import ninjabrainbot.gui.options.components.HotkeyPanel;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.io.KeyboardListener;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.util.I18n;

@SuppressWarnings("serial")
public class HotkeyOptionsPanel extends JPanel {

	public HotkeyOptionsPanel(StyleManager styleManager, NinjabrainBotPreferences preferences) {
		setOpaque(false);
		setLayout(new GridBagLayout());
		setBorder(new EmptyBorder(OptionsFrame.PADDING, OptionsFrame.PADDING, OptionsFrame.PADDING, OptionsFrame.PADDING));

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridy = GridBagConstraints.RELATIVE;
		constraints.gridx = 0;
		constraints.insets = new Insets(0, 0, OptionsFrame.PADDING, 0);
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weightx = 1;
		if (KeyboardListener.registered) {
			add(new HotkeyPanel(styleManager, I18n.get("settings.up_001_to_last_angle"), preferences.hotkeyIncrement), constraints);
			add(new HotkeyPanel(styleManager, I18n.get("settings.down_001_to_last_angle"), preferences.hotkeyDecrement), constraints);
			add(new HotkeyPanel(styleManager, I18n.get("reset"), preferences.hotkeyReset), constraints);
			add(new HotkeyPanel(styleManager, I18n.get("undo"), preferences.hotkeyUndo), constraints);
			add(new HotkeyPanel(styleManager, I18n.get("lock"), preferences.hotkeyLock), constraints);
			add(new HotkeyPanel(styleManager, I18n.get("hide_show_window"), preferences.hotkeyMinimize), constraints);
		}
		constraints.weighty = 1;
		add(Box.createGlue(), constraints);
	}

}
