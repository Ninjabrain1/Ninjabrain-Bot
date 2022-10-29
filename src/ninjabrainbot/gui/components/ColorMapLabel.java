package ninjabrainbot.gui.components;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import ninjabrainbot.gui.style.StyleManager;

public class ColorMapLabel extends JPanel implements ILabel {

	private static final long serialVersionUID = 8926205242557099213L;

	public JLabel textLabel;
	public ColoredLabel coloredLabel;

	public ColorMapLabel(StyleManager styleManager, boolean textFirst) {
		this(styleManager, textFirst, false);
	}

	public ColorMapLabel(StyleManager styleManager, boolean textFirst, boolean centered) {
		textLabel = new ThemedLabel(styleManager, "");
		coloredLabel = new ColoredLabel(styleManager);
		setLayout(new FlowLayout(centered ? FlowLayout.CENTER : FlowLayout.LEFT, 0, 0));
		setOpaque(false);
		if (textFirst) {
			add(textLabel);
			add(coloredLabel);
		} else {
			add(coloredLabel);
			add(textLabel);
		}
		setAlignmentX(0);
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