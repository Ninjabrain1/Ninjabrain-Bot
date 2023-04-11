package ninjabrainbot.gui.style.theme;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ninjabrainbot.event.DisposeHandler;
import ninjabrainbot.event.ISubscribable;
import ninjabrainbot.event.ObservableField;
import ninjabrainbot.event.ObservableProperty;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.util.Assert;
import ninjabrainbot.util.Wrapper;

public abstract class Theme {

	protected final ObservableField<String> name = new ObservableField<String>();

	public WrappedColor COLOR_STRONGEST;
	public WrappedColor COLOR_DIVIDER;
	public WrappedColor COLOR_DIVIDER_DARK;
	public WrappedColor COLOR_STRONG;
	public WrappedColor COLOR_SLIGHTLY_STRONG;
	public WrappedColor COLOR_NEUTRAL;
	public WrappedColor COLOR_SLIGHTLY_WEAK;
	public WrappedColor COLOR_EXIT_BUTTON_HOVER;

	public WrappedColor TEXT_COLOR_SLIGHTLY_WEAK;
	public WrappedColor TEXT_COLOR_SLIGHTLY_STRONG;
	public WrappedColor TEXT_COLOR_NEUTRAL;
	public WrappedColor TEXT_COLOR_WEAK;
	public WrappedColor TEXT_COLOR_HEADER;
	public WrappedColor TEXT_COLOR_TITLE;

	public WrappedColor COLOR_SATURATED;
	public WrappedColor COLOR_POSITIVE;
	public WrappedColor COLOR_NEGATIVE;

	public WrappedColor COLOR_GRADIENT_0;
	public WrappedColor COLOR_GRADIENT_50;
	public WrappedColor COLOR_GRADIENT_100;

	public final int UID;
	protected boolean loaded = false;
	protected final ObservableProperty<Theme> whenModified;

	protected static final HashMap<Integer, Theme> THEMES = new HashMap<Integer, Theme>();
	protected static final ArrayList<Theme> STANDARD_THEMES = new ArrayList<Theme>();
	protected static final ArrayList<CustomTheme> CUSTOM_THEMES = new ArrayList<CustomTheme>();
	protected static final DisposeHandler disposeHandler = new DisposeHandler();

	private static Theme dark;
	private static int nextCustomThemeUID = -1;

	public static void loadThemes(NinjabrainBotPreferences preferences) {
		dark = new DarkTheme();
		addStandardTheme(dark);
		addStandardTheme(new LightTheme());
		addStandardTheme(new BlueTheme());
		addStandardTheme(new BastionTheme());
		addStandardTheme(new NetherbrickTheme());
		addStandardTheme(new BambooTheme());
		addStandardTheme(new NinjabrainTheme());
		addStandardTheme(new CouriwayTheme());
		addStandardTheme(new FeinbergTheme());
		addStandardTheme(new DarklavenderTheme());

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
		Assert.isFalse(THEMES.containsKey(theme.UID));
		THEMES.put(theme.UID, theme);
		STANDARD_THEMES.add(theme);
	}

	private static void addCustomTheme(CustomTheme theme, NinjabrainBotPreferences preferences) {
		Assert.isFalse(THEMES.containsKey(theme.UID));
		nextCustomThemeUID--;
		THEMES.put(theme.UID, theme);
		CUSTOM_THEMES.add(theme);
		disposeHandler.add(theme.whenThemeStringChanged().subscribe(__ -> serializeCustomThemes(preferences)));
		disposeHandler.add(theme.whenNameChanged().subscribe(__ -> serializeCustomThemes(preferences)));
	}

	private static void serializeCustomThemes(NinjabrainBotPreferences preferences) {
		StringBuilder names = new StringBuilder();
		StringBuilder themeStrings = new StringBuilder();

		boolean first = true;
		for (CustomTheme c : CUSTOM_THEMES) {
			String name = c.name.get().replace(".", "");
			String themeString = c.getThemeString();
			Assert.isFalse(themeString.contains("."));
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
		System.out.println("serialize");
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
		this.name.set(name);
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

	@Override
	public String toString() {
		return name.get();
	}

	public ISubscribable<Theme> whenModified() {
		return whenModified;
	}

	public ISubscribable<String> whenNameChanged() {
		return name;
	}

	protected abstract void loadTheme();

}
