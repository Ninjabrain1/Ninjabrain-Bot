package ninjabrainbot.gui.style;

import java.awt.Color;
import java.util.HashMap;

import ninjabrainbot.util.Wrapper;

public abstract class Theme {

	public String name;
	public WrappedColor COLOR_STRONGEST;
	public WrappedColor COLOR_STRONGER;
	public WrappedColor COLOR_STRONG;
	public WrappedColor COLOR_SLIGHTLY_STRONG;
	public WrappedColor COLOR_NEUTRAL;
	public WrappedColor COLOR_SLIGHTLY_WEAK;
	public WrappedColor COLOR_EXIT_BUTTON_HOVER;
	public WrappedColor COLOR_REMOVE_BUTTON_HOVER;
	public WrappedColor TEXT_COLOR_STRONG;
	public WrappedColor TEXT_COLOR_SLIGHTLY_STRONG;
	public WrappedColor TEXT_COLOR_NEUTRAL;
	public WrappedColor TEXT_COLOR_WEAK;
	public WrappedColor COLOR_SATURATED;
	public WrappedColor COLOR_POSITIVE;
	public WrappedColor COLOR_NEGATIVE;

	public Wrapper<Boolean> BLACK_ICONS;
	public Wrapper<ColorMap> CERTAINTY_COLOR_MAP;

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

	protected WrappedColor createColor(Color color) {
		WrappedColor c = new WrappedColor();
		c.set(color);
		return c;
	}
	
	protected Wrapper<Boolean> createBoolean(boolean bool) {
		Wrapper<Boolean> b = new Wrapper<Boolean>();
		b.set(bool);
		return b;
	}
	
	protected Wrapper<ColorMap> createColorMap(ColorMap colorMap) {
		Wrapper<ColorMap> cmap = new Wrapper<ColorMap>();
		cmap.set(colorMap);
		return cmap;
	}
	
}

class LightTheme extends Theme {
	public LightTheme() {
		super("Light");
		COLOR_NEUTRAL = createColor(Color.decode("#F5F5F5"));
		COLOR_STRONGER = createColor(Color.decode("#D8D8D8"));
		COLOR_EXIT_BUTTON_HOVER = createColor(Color.decode("#F04747"));
		COLOR_STRONGEST = createColor(Color.decode("#C1C1C1"));
		COLOR_SLIGHTLY_STRONG = createColor(Color.decode("#EFEFEF"));
		COLOR_SLIGHTLY_WEAK = createColor(Color.decode("#F9F9F9"));
		TEXT_COLOR_STRONG = createColor(Color.BLACK);
		TEXT_COLOR_SLIGHTLY_STRONG = createColor(Color.decode("#191919"));
		TEXT_COLOR_WEAK = createColor(Color.decode("#888888"));
		TEXT_COLOR_NEUTRAL = createColor(Color.DARK_GRAY);
		COLOR_STRONG = createColor(Color.decode("#E5E5E5"));
		COLOR_REMOVE_BUTTON_HOVER = createColor(Color.decode("#F04747"));
		COLOR_SATURATED = createColor(Color.decode("#BAD7EF"));
		COLOR_POSITIVE = createColor(Color.decode("#1E9910"));
		COLOR_NEGATIVE = createColor(Color.decode("#991017"));

		CERTAINTY_COLOR_MAP = createColorMap(new ColorMap(Color.RED, Color.decode("#BFBF00"), Color.decode("#00CE29")));
		BLACK_ICONS = createBoolean(true);
	}
}

class DarkTheme extends Theme {
	public DarkTheme() {
		super("Dark");
		COLOR_NEUTRAL = createColor(Color.decode("#33383D"));
		COLOR_STRONGEST = createColor(Color.decode("#212529"));
		COLOR_EXIT_BUTTON_HOVER = createColor(Color.decode("#F04747"));
		COLOR_STRONGER = createColor(Color.decode("#2A2E32"));
		COLOR_SLIGHTLY_STRONG = createColor(Color.decode("#31353A"));
		COLOR_SLIGHTLY_WEAK = createColor(Color.decode("#373C42"));
		TEXT_COLOR_STRONG = createColor(Color.WHITE);
		TEXT_COLOR_SLIGHTLY_STRONG = createColor(Color.decode("#E5E5E5"));
		TEXT_COLOR_WEAK = createColor(Color.GRAY);
		TEXT_COLOR_NEUTRAL = createColor(Color.LIGHT_GRAY);
		COLOR_STRONG = createColor(Color.decode("#2D3238"));
		COLOR_REMOVE_BUTTON_HOVER = createColor(Color.decode("#F04747"));
		COLOR_SATURATED = createColor(Color.decode("#57EBA3"));
		COLOR_POSITIVE = createColor(Color.decode("#75CC6C"));
		COLOR_NEGATIVE = createColor(Color.decode("#CC6E72"));

		CERTAINTY_COLOR_MAP = createColorMap(new ColorMap(Color.RED, Color.YELLOW, Color.decode("#00CE29")));
		BLACK_ICONS = createBoolean(false);
	}
}

class BlueTheme extends Theme {
	public BlueTheme() {
		super("Blue");
		COLOR_STRONGEST = createColor(Color.decode("#1C1C27"));
		COLOR_STRONGER = createColor(Color.decode("#212130"));
		COLOR_STRONG = createColor(Color.decode("#252538"));
		COLOR_SLIGHTLY_STRONG = createColor(Color.decode("#27273D"));
		COLOR_NEUTRAL = createColor(Color.decode("#28293D"));
		COLOR_SLIGHTLY_WEAK = createColor(Color.decode("#2B2D44"));
		COLOR_EXIT_BUTTON_HOVER = createColor(Color.decode("#F04747"));
		TEXT_COLOR_STRONG = createColor(Color.WHITE);
		TEXT_COLOR_SLIGHTLY_STRONG = createColor(Color.decode("#E5E5E5"));
		TEXT_COLOR_WEAK = createColor(Color.GRAY);
		TEXT_COLOR_NEUTRAL = createColor(Color.LIGHT_GRAY);
		COLOR_REMOVE_BUTTON_HOVER = createColor(Color.decode("#F04747"));
		COLOR_SATURATED = createColor(Color.decode("#57EBA3"));
		COLOR_POSITIVE = createColor(Color.decode("#75CC6C"));
		COLOR_NEGATIVE = createColor(Color.decode("#CC6E72"));

		CERTAINTY_COLOR_MAP = createColorMap(new ColorMap(Color.RED, Color.YELLOW, Color.decode("#00CE29")));
		BLACK_ICONS = createBoolean(false);
	}
}