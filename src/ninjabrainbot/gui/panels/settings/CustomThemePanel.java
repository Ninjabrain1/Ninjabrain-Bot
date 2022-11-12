package ninjabrainbot.gui.panels.settings;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.border.MatteBorder;

import ninjabrainbot.gui.buttons.FlatButton;
import ninjabrainbot.gui.frames.ThemeEditorFrame;
import ninjabrainbot.gui.frames.ThemedDialog;
import ninjabrainbot.gui.panels.ThemedPanel;
import ninjabrainbot.gui.style.CustomTheme;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.gui.style.WrappedColor;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;

public class CustomThemePanel extends ThemePanel {

	private static final long serialVersionUID = -7608715642093152581L;

	ThemedPanel buttonPanel;

	WrappedColor dividerCol;

	public CustomThemePanel(StyleManager styleManager, NinjabrainBotPreferences preferences, JFrame owner, CustomTheme theme, ActionListener deleteAction) {
		super(styleManager, preferences, theme);

		dividerCol = theme.COLOR_DIVIDER_DARK;

		FlatButton editButton = new FlatButton(styleManager, "Edit");
		editButton.setBackgroundColor(theme.COLOR_STRONG);
		editButton.setHoverColor(theme.COLOR_SLIGHTLY_STRONG);
		editButton.setForegroundColor(theme.TEXT_COLOR_STRONG);
		editButton.addActionListener(__ -> editTheme(styleManager, preferences, owner, theme));

		FlatButton deleteButton = new FlatButton(styleManager, "Delete");
		deleteButton.setBackgroundColor(theme.COLOR_STRONG);
		deleteButton.setHoverColor(theme.COLOR_EXIT_BUTTON_HOVER);
		deleteButton.setForegroundColor(theme.TEXT_COLOR_STRONG);
		deleteButton.addActionListener(deleteAction);

		buttonPanel = new ThemedPanel(styleManager);
		buttonPanel.setBackgroundColor(theme.COLOR_STRONG);
		buttonPanel.setLayout(new GridLayout(1, 0));
		buttonPanel.add(editButton);
		buttonPanel.add(deleteButton);

		add(buttonPanel);
	}

	@Override
	public void updateColors() {
		super.updateColors();
		buttonPanel.setBorder(new MatteBorder(1, 0, 0, 0, dividerCol.color()));
	}

	private void editTheme(StyleManager styleManager, NinjabrainBotPreferences preferences, JFrame owner, CustomTheme theme) {
		ThemedDialog d = new ThemeEditorFrame(styleManager, preferences, owner, theme);
		d.setLocation(owner.getX() -140, owner.getY() + 30);
		styleManager.init();
		SwingUtilities.invokeLater(() -> d.setVisible(true));
	}

}
