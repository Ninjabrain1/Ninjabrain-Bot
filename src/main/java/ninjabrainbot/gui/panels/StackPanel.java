package ninjabrainbot.gui.panels;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.Box;
import javax.swing.JPanel;

import ninjabrainbot.gui.frames.OptionsFrame;

@SuppressWarnings("serial")
public class StackPanel extends JPanel {

	GridBagConstraints constraints;

	public StackPanel() {
		setLayout(new GridBagLayout());

		constraints = new GridBagConstraints();
		constraints.gridy = GridBagConstraints.RELATIVE;
		constraints.gridx = 0;
		constraints.insets = new Insets(0, 0, OptionsFrame.PADDING, 0);
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weightx = 1;
		constraints.weighty = 1;
		add(Box.createGlue(), constraints);
		constraints.weighty = 0;
	}

	@Override
	public Component add(Component comp) {
		super.add(comp, constraints, getComponentCount() - 1);
		return null;
	}

}
