package ninjabrainbot.gui.components.layout;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import ninjabrainbot.gui.components.labels.ThemedLabel;
import ninjabrainbot.gui.components.panels.ThemedPanel;
import ninjabrainbot.gui.style.StyleManager;

public class TitledDivider extends ThemedPanel {

	public TitledDivider(StyleManager styleManager, String title) {
		super(styleManager);
		setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = GridBagConstraints.RELATIVE;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0;
		gbc.ipadx = 5;
		add(new ThemedLabel(styleManager, title), gbc);
		gbc.weightx = 1;
		add(new Divider(styleManager), gbc);
		setBackgroundColor(styleManager.currentTheme.COLOR_NEUTRAL);
	}

}
