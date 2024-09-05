package ninjabrainbot.gui.components.layout;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import ninjabrainbot.gui.components.panels.ThemedPanel;
import ninjabrainbot.gui.style.StyleManager;

public class StretchPanel extends ThemedPanel {

	final GridBagConstraints constraints;

	public StretchPanel(StyleManager styleManager, boolean horizontal) {
		super(styleManager);
		setLayout(new GridBagLayout());

		constraints = new GridBagConstraints();
		constraints.gridy = horizontal ? 0 : GridBagConstraints.RELATIVE;
		constraints.gridx = horizontal ? GridBagConstraints.RELATIVE : 0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weightx = 1;
		constraints.weighty = 1;
	}

	@Override
	public Component add(Component comp) {
		super.add(comp, constraints);
		return null;
	}

}
