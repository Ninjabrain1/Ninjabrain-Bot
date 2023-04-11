package ninjabrainbot.gui.components.layout;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.Box;
import javax.swing.JPanel;

import ninjabrainbot.gui.frames.OptionsFrame;

public class StackPanel extends JPanel {

	final GridBagConstraints constraints;
	final int gapBetweenComponents;

	public StackPanel() {
		this(OptionsFrame.PADDING);
	}

	public StackPanel(int gapBetweenComponents) {
		setLayout(new GridBagLayout());
		this.gapBetweenComponents = gapBetweenComponents;

		constraints = new GridBagConstraints();
		constraints.gridy = GridBagConstraints.RELATIVE;
		constraints.gridx = 0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weightx = 1;
		constraints.weighty = 1;
		add(Box.createGlue(), constraints);
		constraints.insets = new Insets(0, 0, 0, 0);
		constraints.weighty = 0;
	}

	@Override
	public Component add(Component comp) {
		super.add(comp, constraints, getComponentCount() - 1);
		constraints.insets = new Insets(gapBetweenComponents, 0, 0, 0);
		return null;
	}

}
