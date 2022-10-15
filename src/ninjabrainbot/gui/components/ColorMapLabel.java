package ninjabrainbot.gui.components;

import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.gui.style.Theme;

public class ColorMapLabel extends JPanel implements ILabel {

	private static final long serialVersionUID = 8926205242557099213L;

	public JLabel textLabel;
	public JLabel coloredLabel;

	private double lastColor = 0.0;

	public ColorMapLabel(StyleManager styleManager, boolean textFirst) {
		this(styleManager, textFirst, false);
	}

	public ColorMapLabel(StyleManager styleManager, boolean textFirst, boolean centered) {
		textLabel = new ThemedLabel(styleManager, "");
		coloredLabel = new ThemedLabel(styleManager, "") {
			private static final long serialVersionUID = -6995689057641195351L;

			@Override
			public Color getForegroundColor(Theme theme) {
				return theme.CERTAINTY_COLOR_MAP.get(lastColor);
			}
		};
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
		lastColor = color;
		coloredLabel.setText(text);
	}

	public void setText(String text) {
		textLabel.setText(text);
	}

	public void updateColor(StyleManager styleManager) {
		coloredLabel.setForeground(styleManager.theme.CERTAINTY_COLOR_MAP.get(lastColor));
	}

	public void clear() {
		textLabel.setText("");
		coloredLabel.setText("");
	}

}