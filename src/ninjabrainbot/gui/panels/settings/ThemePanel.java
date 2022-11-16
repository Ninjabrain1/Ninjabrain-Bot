package ninjabrainbot.gui.panels.settings;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import ninjabrainbot.gui.buttons.FlatButton;
import ninjabrainbot.gui.panels.ThemedPanel;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.gui.style.Theme;
import ninjabrainbot.gui.style.WrappedColor;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;

public class ThemePanel extends ThemedPanel {

	WrappedColor borderCol;

	private static final long serialVersionUID = -2461721723680709056L;

	public ThemePanel(StyleManager styleManager, NinjabrainBotPreferences preferences, Theme theme) {
		super(styleManager);
		setLayout(new GridLayout(0, 1));
		borderCol = styleManager.currentTheme.COLOR_DIVIDER_DARK;

		FlatButton nameLabel = new LeftAlignedButton(styleManager, theme.name);
		nameLabel.setForegroundColor(theme.TEXT_COLOR_SLIGHTLY_WEAK);
		nameLabel.setHoverColor(theme.COLOR_STRONGEST);
		nameLabel.setBackgroundColor(theme.COLOR_STRONGEST);
		nameLabel.setBorder(new EmptyBorder(5, 5, 5, 5));

		ThemedPanel colorPreviewPanels = new ThemedPanel(styleManager);
		colorPreviewPanels.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = GridBagConstraints.RELATIVE;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weighty = 1;
		gbc.weightx = 1;
		colorPreviewPanels.add(new ColorPreviewPanel(styleManager, theme.COLOR_NEUTRAL), gbc);
		colorPreviewPanels.add(new ColorPreviewPanel(styleManager, theme.COLOR_SLIGHTLY_WEAK), gbc);
		colorPreviewPanels.add(new ColorPreviewPanel(styleManager, theme.COLOR_STRONG), gbc);
		colorPreviewPanels.add(new ColorPreviewPanel(styleManager, theme.COLOR_DIVIDER), gbc);
		colorPreviewPanels.add(new ColorPreviewPanel(styleManager, theme.COLOR_DIVIDER_DARK), gbc);
		colorPreviewPanels.setBackgroundColor(theme.COLOR_DIVIDER_DARK);

		add(nameLabel);
		add(colorPreviewPanels);

		nameLabel.addActionListener(__ -> preferences.theme.set(theme.UID));
		theme.whenModified().subscribe(__ -> styleManager.init());
	}

	@Override
	public void updateColors() {
		super.updateColors();
		setBorder(new LineBorder(borderCol.color(), 1));
	}

}

class LeftAlignedButton extends FlatButton {

	private static final long serialVersionUID = 1439936402421359939L;

	LeftAlignedButton(StyleManager styleManager, String name) {
		super(styleManager, "  " + name);
		label.setHorizontalAlignment(SwingConstants.LEFT);
	}

}