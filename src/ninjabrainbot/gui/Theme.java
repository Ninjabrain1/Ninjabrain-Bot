package ninjabrainbot.gui;

import java.awt.Color;

public abstract class Theme {
	
	public String name;
	public Color COLOR_STRONGEST;
	public Color COLOR_STRONGER;
	public Color COLOR_STRONG;
	public Color COLOR_SLIGHTLY_STRONG;
	public Color COLOR_NEUTRAL;
	public Color COLOR_SLIGHTLY_WEAK;
	public Color COLOR_EXIT_BUTTON_HOVER;
	public Color COLOR_REMOVE_BUTTON_HOVER;
	public Color TEXT_COLOR_STRONG;
	public Color TEXT_COLOR_NEUTRAL;
	public Color TEXT_COLOR_WEAK;
	public Color COLOR_SATURATED;
	public boolean BLACK_ICONS;
	public ColorMap CERTAINTY_COLOR_MAP;

	public static final Theme DARK = new DarkTheme();
	public static final Theme LIGHT = new LightTheme();

}

class DarkTheme extends Theme {
	public DarkTheme() {
		name = "Dark";
		COLOR_NEUTRAL = Color.decode("#33383D");
		COLOR_STRONGEST = Color.decode("#212529");
		COLOR_EXIT_BUTTON_HOVER = Color.decode("#F04747");
		COLOR_STRONGER = Color.decode("#2A2E32");
		COLOR_SLIGHTLY_STRONG = Color.decode("#31353A");
		COLOR_SLIGHTLY_WEAK = Color.decode("#373C42");
		TEXT_COLOR_STRONG = Color.WHITE;
		TEXT_COLOR_WEAK = Color.GRAY;
		TEXT_COLOR_NEUTRAL = Color.LIGHT_GRAY;
		COLOR_STRONG = Color.decode("#2D3238");
		COLOR_REMOVE_BUTTON_HOVER  = Color.decode("#F04747");
		COLOR_SATURATED =  Color.decode("#245782");
		CERTAINTY_COLOR_MAP = new ColorMap(Color.RED, Color.YELLOW, Color.decode("#00CE29"));
		BLACK_ICONS = false;
	}
}

class LightTheme extends Theme {
	public LightTheme() {
		name = "Light";
		COLOR_NEUTRAL = Color.decode("#F5F5F5");
		COLOR_STRONGEST = Color.decode("#D8D8D8");
		COLOR_EXIT_BUTTON_HOVER = Color.decode("#F04747");
		COLOR_STRONGER = Color.decode("#C1C1C1");
		COLOR_SLIGHTLY_STRONG = Color.decode("#EFEFEF");
		COLOR_SLIGHTLY_WEAK = Color.decode("#FFFFFF");
		TEXT_COLOR_STRONG = Color.BLACK;
		TEXT_COLOR_WEAK = Color.decode("#888888");
		TEXT_COLOR_NEUTRAL = Color.DARK_GRAY;
		COLOR_STRONG = Color.decode("#E5E5E5");
		COLOR_REMOVE_BUTTON_HOVER  = Color.decode("#F04747");
		COLOR_SATURATED =  Color.decode("#BAD7EF");
		CERTAINTY_COLOR_MAP = new ColorMap(Color.RED, Color.YELLOW, Color.decode("#00CE29"));
		BLACK_ICONS = true;
	}
}
