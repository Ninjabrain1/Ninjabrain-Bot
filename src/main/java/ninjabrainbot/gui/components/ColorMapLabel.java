package ninjabrainbot.gui.components;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.gui.style.WrappedColor;

public class ColorMapLabel extends JPanel implements ILabel {

	private static final long serialVersionUID = 8926205242557099213L;

	public ThemedLabel textLabel;
	public ColoredLabel coloredLabel;

	public ColorMapLabel(StyleManager styleManager, boolean textFirst) {
		this(styleManager, textFirst, false);
	}

	public ColorMapLabel(StyleManager styleManager, boolean textFirst, boolean centered) {
		textLabel = new ThemedLabel(styleManager, "");
		coloredLabel = new ColoredLabel(styleManager);
		setLayout(new GridBagLayout());
		setOpaque(false);
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = GridBagConstraints.RELATIVE;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.fill = GridBagConstraints.VERTICAL;
		gbc.weightx = 0;
		if (textFirst) {
			add(textLabel, gbc);
			gbc.weightx = 1;
			add(coloredLabel, gbc);
		} else {
			add(coloredLabel, gbc);
			gbc.weightx = 1;
			add(textLabel, gbc);
		}
		setAlignmentX(0);
	}

	public void setForegroundColor(WrappedColor color) {
		textLabel.setForegroundColor(color);
	}

	public void setColoredText(String text, float color) {
		coloredLabel.setText(text, color);
	}

	public void setText(String text) {
		textLabel.setText(text);
	}

	public void updateColor() {
		coloredLabel.updateColors();
	}

	public void clear() {
		textLabel.setText("");
		coloredLabel.setText("");
	}

}