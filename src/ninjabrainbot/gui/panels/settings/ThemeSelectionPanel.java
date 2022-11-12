package ninjabrainbot.gui.panels.settings;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.border.EmptyBorder;

import ninjabrainbot.gui.buttons.FlatButton;
import ninjabrainbot.gui.panels.ThemedPanel;
import ninjabrainbot.gui.style.CustomTheme;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.gui.style.Theme;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;

public class ThemeSelectionPanel extends ThemedPanel {

	private static final long serialVersionUID = 7243421413680247952L;

	public ThemeSelectionPanel(StyleManager styleManager, NinjabrainBotPreferences preferences) {
		super(styleManager);

		setLayout(new BorderLayout());
		setBorder(new EmptyBorder(10, 10, 10, 10));

		ThemedScrollPane scrollPane = new ThemedScrollPane(styleManager, getThemesPanel(styleManager, preferences));
		scrollPane.setBorder(new EmptyBorder(0, 0, 10, 0));
		add(scrollPane, BorderLayout.CENTER);
		add(new FlatButton(styleManager, "asd"), BorderLayout.PAGE_END);
	}

	private ThemedPanel getThemesPanel(StyleManager styleManager, NinjabrainBotPreferences preferences) {
		ThemedPanel themesPanel = new ThemedPanel(styleManager);
		themesPanel.setBackgroundColor(styleManager.currentTheme.COLOR_STRONG);
		themesPanel.setLayout(new GridLayout(0, 3, 10, 10));
		themesPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		for (Theme theme : Theme.getStandardThemes())
			themesPanel.add(new ThemePanel(styleManager, preferences, theme));
		
		ThemedPanel customThemesPanel = new ThemedPanel(styleManager);
		customThemesPanel.setBackgroundColor(styleManager.currentTheme.COLOR_STRONG);
		customThemesPanel.setLayout(new GridLayout(0, 3, 10, 10));
		customThemesPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		for (CustomTheme theme : Theme.getCustomThemes())
			customThemesPanel.add(new CustomThemePanel(styleManager, preferences, theme));
		
		ThemedPanel panel = new ThemedPanel(styleManager);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(themesPanel);
		panel.add(customThemesPanel);
		
		return panel;
	}

}