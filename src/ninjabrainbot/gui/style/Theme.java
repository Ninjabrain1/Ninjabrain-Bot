package ninjabrainbot.gui.style;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ninjabrainbot.event.ISubscribable;
import ninjabrainbot.event.ObservableProperty;
import ninjabrainbot.event.SubscriptionHandler;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.util.Wrapper;

public abstract class Theme {

	public String name;
	public WrappedColor COLOR_STRONGEST;
	public WrappedColor COLOR_DIVIDER;
	public WrappedColor COLOR_DIVIDER_DARK;
	public WrappedColor COLOR_STRONG;
	public WrappedColor COLOR_SLIGHTLY_STRONG;
	public WrappedColor COLOR_NEUTRAL;
	public WrappedColor COLOR_SLIGHTLY_WEAK;
	public WrappedColor COLOR_EXIT_BUTTON_HOVER;
	public WrappedColor TEXT_COLOR_STRONG;
	public WrappedColor TEXT_COLOR_SLIGHTLY_STRONG;
	public WrappedColor TEXT_COLOR_NEUTRAL;
	public WrappedColor TEXT_COLOR_WEAK;
	public WrappedColor COLOR_SATURATED;
	public WrappedColor COLOR_POSITIVE;
	public WrappedColor COLOR_NEGATIVE;
	public WrappedColor ICON_COLOR;

	public Wrapper<ColorMap> CERTAINTY_COLOR_MAP;

	public final int UID;
	protected boolean loaded = false;
	protected ObservableProperty<Theme> whenModified;

	protected static final HashMap<Integer, Theme> THEMES = new HashMap<Integer, Theme>();
	protected static final ArrayList<Theme> STANDARD_THEMES = new ArrayList<Theme>();
	protected static final ArrayList<CustomTheme> CUSTOM_THEMES = new ArrayList<CustomTheme>();
	protected static final SubscriptionHandler sh = new SubscriptionHandler();

	public static int light_uid = 1;
	public static int dark_uid = 2;
	public static int blue_uid = 3;
	private static Theme dark;
	private static int nextCustomThemeUID = -1;

	public static void loadThemes(NinjabrainBotPreferences preferences) {
		dark = new DarkTheme();
		addStandardTheme(dark);
		addStandardTheme(new LightTheme());
		addStandardTheme(new BlueTheme());

		loadCustomThemes(preferences);
	}

	private static void loadCustomThemes(NinjabrainBotPreferences preferences) {
		String[] names = preferences.customThemesNames.get().split("\\.");
		String[] themeStrings = preferences.customThemesString.get().split("\\.");
		
		for (int i = 0; i < themeStrings.length; i++) {
			String themeString = themeStrings[i];
			String name = i < names.length ? names[i] : "Custom theme";
			addCustomTheme(new CustomTheme(name, themeString, nextCustomThemeUID), preferences);
		}
	}

	private static void addStandardTheme(Theme theme) {
		assert !THEMES.containsKey(theme.UID);
		THEMES.put(theme.UID, theme);
		STANDARD_THEMES.add(theme);
	}

	private static void addCustomTheme(CustomTheme theme, NinjabrainBotPreferences preferences) {
		assert !THEMES.containsKey(theme.UID);
		nextCustomThemeUID--;
		THEMES.put(theme.UID, theme);
		CUSTOM_THEMES.add(theme);
		sh.add(theme.whenThemeStringChanged().subscribe(__ -> serializeCustomThemes(preferences)));
	}

	private static void serializeCustomThemes(NinjabrainBotPreferences preferences) {
		StringBuilder names = new StringBuilder();
		StringBuilder themeStrings = new StringBuilder();

		boolean first = true;
		for (CustomTheme c : CUSTOM_THEMES) {
			String name = c.name.replace(".", "");
			String themeString = c.getThemeString();
			assert !themeString.contains(".");
			if (!first) {
				names.append(".");
				themeStrings.append(".");
			}
			first = false;
			names.append(name);
			themeStrings.append(themeString);
		}
		preferences.customThemesNames.set(names.toString());
		preferences.customThemesString.set(themeStrings.toString());
	}

	public static Theme get(int uid) {
		Theme theme = THEMES.getOrDefault(uid, dark);
		if (!theme.loaded)
			theme.loadTheme();
		return theme;
	}

	public static CustomTheme getCustomTheme(int uid) {
		Theme theme = THEMES.getOrDefault(uid, dark);
		if (!theme.loaded)
			theme.loadTheme();
		if (!(theme instanceof CustomTheme))
			return null;
		return (CustomTheme) theme;
	}

	public Theme(String name, int uid) {
		this.UID = uid;
		this.name = name;
		whenModified = new ObservableProperty<Theme>();
	}

	public static List<Theme> getStandardThemes() {
		for (Theme t : STANDARD_THEMES) {
			if (!t.loaded)
				t.loadTheme();
		}
		return STANDARD_THEMES;
	}

	public static List<CustomTheme> getCustomThemes() {
		for (CustomTheme t : CUSTOM_THEMES) {
			if (!t.loaded)
				t.loadTheme();
		}
		return CUSTOM_THEMES;
	}

	public static void createCustomTheme(NinjabrainBotPreferences preferences) {
		CustomTheme theme = new CustomTheme("New theme", "", nextCustomThemeUID);
		theme.setFromTheme(dark);
		addCustomTheme(theme, preferences);
		serializeCustomThemes(preferences);
	}

	public static void deleteCustomTheme(StyleManager styleManager, NinjabrainBotPreferences preferences, CustomTheme theme) {
		if (styleManager.currentTheme.isTheme(theme))
			styleManager.currentTheme.setTheme(dark);
		THEMES.remove(theme.UID);
		CUSTOM_THEMES.remove(theme);
		serializeCustomThemes(preferences);
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

	@Override
	public String toString() {
		return name;
	}

	public ISubscribable<Theme> whenModified() {
		return whenModified;
	}

	protected abstract void loadTheme();

}

class LightTheme extends Theme {
	public LightTheme() {
		super("Light", light_uid);
	}

	@Override
	protected void loadTheme() {
		COLOR_NEUTRAL = createColor(Color.decode("#F5F5F5"));
		COLOR_DIVIDER = createColor(Color.decode("#D8D8D8"));
		COLOR_DIVIDER_DARK = createColor(Color.decode("#C1C1C1"));
		COLOR_EXIT_BUTTON_HOVER = createColor(Color.decode("#F04747"));
		COLOR_STRONGEST = createColor(Color.decode("#C1C1C1"));
		COLOR_SLIGHTLY_STRONG = createColor(Color.decode("#EFEFEF"));
		COLOR_SLIGHTLY_WEAK = createColor(Color.decode("#F9F9F9"));
		TEXT_COLOR_STRONG = createColor(Color.BLACK);
		TEXT_COLOR_SLIGHTLY_STRONG = createColor(Color.decode("#191919"));
		TEXT_COLOR_WEAK = createColor(Color.decode("#888888"));
		TEXT_COLOR_NEUTRAL = createColor(Color.DARK_GRAY);
		COLOR_STRONG = createColor(Color.decode("#E5E5E5"));
		COLOR_SATURATED = createColor(Color.decode("#BAD7EF"));
		COLOR_POSITIVE = createColor(Color.decode("#1E9910"));
		COLOR_NEGATIVE = createColor(Color.decode("#991017"));
		ICON_COLOR = createColor(Color.decode("#373737"));

		CERTAINTY_COLOR_MAP = createColorMap(new ColorMap(Color.RED, Color.decode("#BFBF00"), Color.decode("#00CE29")));
	}
}

class DarkTheme extends Theme {
	public DarkTheme() {
		super("Dark", dark_uid);
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
		TEXT_COLOR_STRONG = createColor(Color.WHITE);
		TEXT_COLOR_SLIGHTLY_STRONG = createColor(Color.decode("#E5E5E5"));
		TEXT_COLOR_WEAK = createColor(Color.GRAY);
		TEXT_COLOR_NEUTRAL = createColor(Color.LIGHT_GRAY);
		COLOR_STRONG = createColor(Color.decode("#2D3238"));
		COLOR_SATURATED = createColor(Color.decode("#57EBA3"));
		COLOR_POSITIVE = createColor(Color.decode("#75CC6C"));
		COLOR_NEGATIVE = createColor(Color.decode("#CC6E72"));
		ICON_COLOR = createColor(Color.WHITE);

		CERTAINTY_COLOR_MAP = createColorMap(new ColorMap(Color.RED, Color.YELLOW, Color.decode("#00CE29")));
	}
}

class BlueTheme extends Theme {
	public BlueTheme() {
		super("Blue", blue_uid);
	}

	@Override
	protected void loadTheme() {
		COLOR_STRONGEST = createColor(Color.decode("#1C1C27"));
		COLOR_DIVIDER = createColor(Color.decode("#212130"));
		COLOR_DIVIDER_DARK = createColor(Color.decode("#1C1C27"));
		COLOR_STRONG = createColor(Color.decode("#252538"));
		COLOR_SLIGHTLY_STRONG = createColor(Color.decode("#27273D"));
		COLOR_NEUTRAL = createColor(Color.decode("#28293D"));
		COLOR_SLIGHTLY_WEAK = createColor(Color.decode("#2B2D44"));
		COLOR_EXIT_BUTTON_HOVER = createColor(Color.decode("#F04747"));
		TEXT_COLOR_STRONG = createColor(Color.WHITE);
		TEXT_COLOR_SLIGHTLY_STRONG = createColor(Color.decode("#E5E5E5"));
		TEXT_COLOR_WEAK = createColor(Color.GRAY);
		TEXT_COLOR_NEUTRAL = createColor(Color.LIGHT_GRAY);
		COLOR_SATURATED = createColor(Color.decode("#57EBA3"));
		COLOR_POSITIVE = createColor(Color.decode("#75CC6C"));
		COLOR_NEGATIVE = createColor(Color.decode("#CC6E72"));
		ICON_COLOR = createColor(Color.WHITE);

		CERTAINTY_COLOR_MAP = createColorMap(new ColorMap(Color.RED, Color.YELLOW, Color.decode("#00CE29")));
	}
}
