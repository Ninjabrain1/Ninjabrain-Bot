package ninjabrainbot.gui.panels.settings;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.border.EmptyBorder;

import ninjabrainbot.gui.buttons.FlatButton;
import ninjabrainbot.gui.components.TitledDivider;
import ninjabrainbot.gui.panels.ThemedPanel;
import ninjabrainbot.gui.style.CustomTheme;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.gui.style.Theme;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.util.I18n;

public class ThemeSelectionPanel extends ThemedPanel {

	private static final long serialVersionUID = 7243421413680247952L;

	private ThemedPanel themesPanel;

	public ThemeSelectionPanel(StyleManager styleManager, NinjabrainBotPreferences preferences, JFrame owner) {
		super(styleManager);

		setLayout(new BorderLayout());
		setBorder(new EmptyBorder(10, 10, 10, 10));

		themesPanel = new ThemedPanel(styleManager);
		themesPanel.setLayout(new BoxLayout(themesPanel, BoxLayout.Y_AXIS));
		themesPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		themesPanel.setBackgroundColor(styleManager.currentTheme.COLOR_HEADER);
		populateThemesPanel(styleManager, preferences, owner);

		ThemedScrollPane scrollPane = new ThemedScrollPane(styleManager, themesPanel);
		scrollPane.setBorder(new EmptyBorder(0, 0, 10, 0));

		ThemedPanel buttonsPanel = new ThemedPanel(styleManager);
		buttonsPanel.setLayout(new GridLayout(1, 0, 10, 0));
		FlatButton createThemeButton = new FlatButton(styleManager, I18n.get("settings.theme.createNew"));
		createThemeButton.addActionListener(__ -> createNewTheme(styleManager, preferences, owner));
		buttonsPanel.add(createThemeButton);

		add(scrollPane, BorderLayout.CENTER);
		add(buttonsPanel, BorderLayout.PAGE_END);
	}

	private void populateThemesPanel(StyleManager styleManager, NinjabrainBotPreferences preferences, JFrame owner) {
		TitledDivider standardThemesDivider = new TitledDivider(styleManager, I18n.get("settings.theme.defaultThemes"));
		standardThemesDivider.setBackgroundColor(styleManager.currentTheme.COLOR_HEADER);
		standardThemesDivider.setBorder(new EmptyBorder(0, 0, 10, 0));

		ThemedPanel defaultThemesPanel = new ThemedPanel(styleManager);
		defaultThemesPanel.setBackgroundColor(styleManager.currentTheme.COLOR_HEADER);
		defaultThemesPanel.setLayout(new GridLayout(0, 3, 10, 10));
		for (Theme theme : Theme.getStandardThemes())
			defaultThemesPanel.add(new ThemePanel(styleManager, preferences, theme));

		TitledDivider customThemesDivider = new TitledDivider(styleManager, I18n.get("settings.theme.customThemes"));
		customThemesDivider.setBorder(new EmptyBorder(10, 0, 10, 0));
		customThemesDivider.setBackgroundColor(styleManager.currentTheme.COLOR_HEADER);

		ThemedPanel customThemesPanel = new ThemedPanel(styleManager);
		customThemesPanel.setBackgroundColor(styleManager.currentTheme.COLOR_HEADER);
		customThemesPanel.setLayout(new GridLayout(0, 3, 10, 10));
		for (CustomTheme theme : Theme.getCustomThemes())
			customThemesPanel.add(new CustomThemePanel(styleManager, preferences, owner, theme, __ -> deleteTheme(styleManager, preferences, owner, theme)));

		themesPanel.removeAll();
		themesPanel.add(standardThemesDivider);
		themesPanel.add(defaultThemesPanel);
		themesPanel.add(customThemesDivider);
		themesPanel.add(customThemesPanel);
	}

	private void createNewTheme(StyleManager styleManager, NinjabrainBotPreferences preferences, JFrame owner) {
		Theme.createCustomTheme(preferences);
		populateThemesPanel(styleManager, preferences, owner);
		styleManager.init();
	}

	private void deleteTheme(StyleManager styleManager, NinjabrainBotPreferences preferences, JFrame owner, CustomTheme theme) {
		int result = JOptionPane.showConfirmDialog(owner, I18n.get("settings.theme.areyousure", theme.name), I18n.get("settings.theme.deletetheme"), JOptionPane.YES_NO_OPTION,
				JOptionPane.PLAIN_MESSAGE);
		if (result != JOptionPane.YES_OPTION)
			return;
		Theme.deleteCustomTheme(styleManager, preferences, theme);
		populateThemesPanel(styleManager, preferences, owner);
		styleManager.init();
	}

}