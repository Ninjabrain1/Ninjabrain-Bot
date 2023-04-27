package ninjabrainbot.util;

import java.awt.Color;

import ninjabrainbot.gui.style.theme.Theme;

public class TestTheme extends Theme {

	public static final int UID = 1000;

	public TestTheme() {
		super("Test theme", UID);
		loadTheme();
	}

	@Override
	protected void loadTheme() {
		COLOR_NEUTRAL = createColor(Color.decode("#33383D"));
		COLOR_STRONGEST = createColor(Color.decode("#212529"));
		COLOR_EXIT_BUTTON_HOVER = createColor(Color.decode("#F04747"));
		COLOR_DIVIDER = createColor(Color.decode("#2A2E32"));
		COLOR_DIVIDER_DARK = createColor(Color.decode("#212529"));
		COLOR_SLIGHTLY_STRONG = createColor(Color.decode("#31353A"));
		COLOR_SLIGHTLY_WEAK = createColor(Color.decode("#373C42"));
		TEXT_COLOR_SLIGHTLY_WEAK = createColor(Color.WHITE);
		TEXT_COLOR_SLIGHTLY_STRONG = createColor(Color.decode("#E5E5E5"));
		TEXT_COLOR_WEAK = createColor(Color.GRAY);
		TEXT_COLOR_NEUTRAL = createColor(Color.LIGHT_GRAY);
		TEXT_COLOR_HEADER = createColor(Color.decode("#E5E5E5"));
		COLOR_STRONG = createColor(Color.decode("#2D3238"));
		COLOR_SATURATED = createColor(Color.decode("#57EBA3"));
		COLOR_POSITIVE = createColor(Color.decode("#75CC6C"));
		COLOR_NEGATIVE = createColor(Color.decode("#CC6E72"));
		TEXT_COLOR_TITLE = createColor(Color.WHITE);

		COLOR_GRADIENT_0 = createColor(Color.RED);
		COLOR_GRADIENT_50 = createColor(Color.YELLOW);
		COLOR_GRADIENT_100 = createColor(Color.decode("#00CE29"));
	}
}