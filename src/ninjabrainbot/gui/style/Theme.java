package ninjabrainbot.gui.style;

import java.awt.Color;
import java.util.HashMap;

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
	public Color TEXT_COLOR_SLIGHTLY_STRONG;
	public Color TEXT_COLOR_NEUTRAL;
	public Color TEXT_COLOR_WEAK;
	public Color COLOR_SATURATED;
	public Color COLOR_POSITIVE;
	public Color COLOR_NEGATIVE;
	public boolean BLACK_ICONS;
	public transient ColorMap CERTAINTY_COLOR_MAP;

	public static final HashMap<String, Theme> THEMES = new HashMap<String, Theme>();
	public static final Theme LIGHT = new LightTheme();
	public static final Theme DARK = new DarkTheme();
	public static final Theme BLUE = new BlueTheme();

	public static Theme get(String name) {
		return THEMES.getOrDefault(name, DARK);
	}

	public Theme(String name) {
		this.name = name;
		THEMES.put(name, this);
	}

}

class LightTheme extends Theme {
	public LightTheme() {
		super("Light");
		COLOR_NEUTRAL = Color.decode("#F5F5F5");
		COLOR_STRONGER = Color.decode("#D8D8D8");
		COLOR_EXIT_BUTTON_HOVER = Color.decode("#F04747");
		COLOR_STRONGEST = Color.decode("#C1C1C1");
		COLOR_SLIGHTLY_STRONG = Color.decode("#EFEFEF");
		COLOR_SLIGHTLY_WEAK = Color.decode("#F9F9F9");
		TEXT_COLOR_STRONG = Color.BLACK;
		TEXT_COLOR_SLIGHTLY_STRONG = Color.decode("#191919");
		;
		TEXT_COLOR_WEAK = Color.decode("#888888");
		TEXT_COLOR_NEUTRAL = Color.DARK_GRAY;
		COLOR_STRONG = Color.decode("#E5E5E5");
		COLOR_REMOVE_BUTTON_HOVER = Color.decode("#F04747");
		COLOR_SATURATED = Color.decode("#BAD7EF");
		COLOR_POSITIVE = Color.decode("#1E9910");
		COLOR_NEGATIVE = Color.decode("#991017");
		CERTAINTY_COLOR_MAP = new ColorMap(Color.RED, Color.decode("#BFBF00"), Color.decode("#00CE29"));
		BLACK_ICONS = true;
	}
}

class DarkTheme extends Theme {
	public DarkTheme() {
		super("Dark");
		COLOR_NEUTRAL = Color.decode("#33383D");
		COLOR_STRONGEST = Color.decode("#212529");
		COLOR_EXIT_BUTTON_HOVER = Color.decode("#F04747");
		COLOR_STRONGER = Color.decode("#2A2E32");
		COLOR_SLIGHTLY_STRONG = Color.decode("#31353A");
		COLOR_SLIGHTLY_WEAK = Color.decode("#373C42");
		TEXT_COLOR_STRONG = Color.WHITE;
		TEXT_COLOR_SLIGHTLY_STRONG = Color.decode("#E5E5E5");
		;
		TEXT_COLOR_WEAK = Color.GRAY;
		TEXT_COLOR_NEUTRAL = Color.LIGHT_GRAY;
		COLOR_STRONG = Color.decode("#2D3238");
		COLOR_REMOVE_BUTTON_HOVER = Color.decode("#F04747");
		COLOR_SATURATED = Color.decode("#57EBA3");
		COLOR_POSITIVE = Color.decode("#75CC6C");
		COLOR_NEGATIVE = Color.decode("#CC6E72");
		CERTAINTY_COLOR_MAP = new ColorMap(Color.RED, Color.YELLOW, Color.decode("#00CE29"));
		BLACK_ICONS = false;
	}
}

class BlueTheme extends Theme {
	public BlueTheme() {
		super("Blue");
		COLOR_STRONGEST = Color.decode("#1C1C27");
		COLOR_STRONGER = Color.decode("#212130");
		COLOR_STRONG = Color.decode("#252538");
		COLOR_SLIGHTLY_STRONG = Color.decode("#27273D");
		COLOR_NEUTRAL = Color.decode("#28293D");
		COLOR_SLIGHTLY_WEAK = Color.decode("#2B2D44");
		COLOR_EXIT_BUTTON_HOVER = Color.decode("#F04747");
		TEXT_COLOR_STRONG = Color.WHITE;
		TEXT_COLOR_SLIGHTLY_STRONG = Color.decode("#E5E5E5");
		;
		TEXT_COLOR_WEAK = Color.GRAY;
		TEXT_COLOR_NEUTRAL = Color.LIGHT_GRAY;
		COLOR_REMOVE_BUTTON_HOVER = Color.decode("#F04747");
		COLOR_SATURATED = Color.decode("#57EBA3");
		COLOR_POSITIVE = Color.decode("#75CC6C");
		COLOR_NEGATIVE = Color.decode("#CC6E72");
		CERTAINTY_COLOR_MAP = new ColorMap(Color.RED, Color.YELLOW, Color.decode("#00CE29"));
		BLACK_ICONS = false;
	}
}
