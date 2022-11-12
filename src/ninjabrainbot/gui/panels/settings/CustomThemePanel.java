package ninjabrainbot.gui.panels.settings;

import java.awt.GridLayout;

import ninjabrainbot.gui.buttons.FlatButton;
import ninjabrainbot.gui.panels.ThemedPanel;
import ninjabrainbot.gui.style.CustomTheme;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;

public class CustomThemePanel extends ThemePanel {

	private static final long serialVersionUID = -7608715642093152581L;

	public CustomThemePanel(StyleManager styleManager, NinjabrainBotPreferences preferences, CustomTheme theme) {
		super(styleManager, preferences, theme);

		FlatButton editButton = new FlatButton(styleManager, "Edit");
		editButton.setBackgroundColor(theme.COLOR_STRONG);
		editButton.setHoverColor(theme.COLOR_SLIGHTLY_STRONG);
		editButton.setForegroundColor(theme.TEXT_COLOR_STRONG);
		FlatButton deleteButton = new FlatButton(styleManager, "Delete");
		deleteButton.setBackgroundColor(theme.COLOR_STRONG);
		deleteButton.setHoverColor(theme.COLOR_EXIT_BUTTON_HOVER);
		deleteButton.setForegroundColor(theme.TEXT_COLOR_STRONG);

		ThemedPanel buttonPanel = new ThemedPanel(styleManager);
		buttonPanel.setBackgroundColor(theme.COLOR_STRONG);
		buttonPanel.setLayout(new GridLayout(1, 0));
		buttonPanel.add(editButton);
		buttonPanel.add(deleteButton);

		add(buttonPanel);
	}

}
