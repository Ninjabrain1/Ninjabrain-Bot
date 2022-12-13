package ninjabrainbot.gui.components;

import java.awt.Color;

import ninjabrainbot.gui.style.ColorMap;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.util.Wrapper;

public class ColoredLabel extends ThemedLabel {

	private static final long serialVersionUID = 3745663434827561255L;

	Wrapper<ColorMap> colorMap;

	private double lastColor = 0.0;

	public ColoredLabel(StyleManager styleManager) {
		this(styleManager, false);
	}

	public ColoredLabel(StyleManager styleManager, boolean centered) {
		super(styleManager, centered);
		colorMap = styleManager.currentTheme.CERTAINTY_COLOR_MAP;
	}

	public void setText(String text, float color) {
		lastColor = color;
		setText(text);
		updateColors();
	}

	@Override
	public Color getForegroundColor() {
		return colorMap.get().get(lastColor);
	}

}
