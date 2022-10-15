package ninjabrainbot.io.preferences;

import java.util.prefs.Preferences;

import ninjabrainbot.Main;
import ninjabrainbot.gui.style.SizePreference;
import ninjabrainbot.gui.style.Theme;
import ninjabrainbot.util.I18n;

public class NinjabrainBotPreferences {

	Preferences pref;

	public IntPreference windowX;
	public IntPreference windowY;
	public HotkeyPreference hotkeyIncrement;
	public HotkeyPreference hotkeyDecrement;
	public HotkeyPreference hotkeyReset;
	public HotkeyPreference hotkeyUndo;
	public HotkeyPreference hotkeyMinimize;
	public HotkeyPreference hotkeyAltStd;
	public HotkeyPreference hotkeyLock;
	public FloatPreference sigma;
	public FloatPreference sigmaAlt;
	public FloatPreference sigmaManual;
	public FloatPreference crosshairCorrection;
	public FloatPreference overlayHideDelay;
	public BooleanPreference checkForUpdates;
	public BooleanPreference translucent;
	public BooleanPreference alwaysOnTop;
	public BooleanPreference showNetherCoords;
	public BooleanPreference showAngleUpdates;
	public BooleanPreference showAngleErrors;
	public BooleanPreference autoReset;
	public BooleanPreference useAdvStatistics;
	public BooleanPreference altClipboardReader;
	public BooleanPreference useAltStd;
	public BooleanPreference useOverlay;
	public BooleanPreference overlayAutoHide;
	public BooleanPreference overlayHideWhenLocked;
	public MultipleChoicePreference strongholdDisplayType;
	public MultipleChoicePreference theme;
	public MultipleChoicePreference size;
	public MultipleChoicePreference stdToggleMode;
	public MultipleChoicePreference view;
	public MultipleChoicePreference language;
	public MultipleChoicePreference mcVersion;

	public static final String FOURFOUR = "(4, 4)";
	public static final String EIGHTEIGHT = "(8, 8)";
	public static final String CHUNK = I18n.get("chunk");
	public static final String BASIC = I18n.get("basic");
	public static final String DETAILED = I18n.get("detailed");

	public static final String PRE_119 = I18n.get("settings.mc_version.1");
	public static final String POST_119 = I18n.get("settings.mc_version.2");

	public NinjabrainBotPreferences() {
		pref = Preferences.userNodeForPackage(Main.class);
		// Hotkeys
		windowX = new IntPreference("window_x", 100, pref);
		windowY = new IntPreference("window_y", 100, pref);
		hotkeyIncrement = new HotkeyPreference("hotkey_increment", pref);
		hotkeyDecrement = new HotkeyPreference("hotkey_decrement", pref);
		hotkeyReset = new HotkeyPreference("hotkey_reset", pref);
		hotkeyUndo = new HotkeyPreference("hotkey_undo", pref);
		hotkeyMinimize = new HotkeyPreference("hotkey_minimize", pref);
		hotkeyAltStd = new HotkeyPreference("hotkey_alt_std", pref);
		hotkeyLock = new HotkeyPreference("hotkey_lock", pref);
		// Floats
		sigma = new FloatPreference("sigma", 0.1f, 0.001f, 1f, pref);
		sigmaAlt = new FloatPreference("sigma_alt", 0.1f, 0.001f, 1f, pref);
		sigmaManual = new FloatPreference("sigma_manual", 0.03f, 0.001f, 1f, pref);
		crosshairCorrection = new FloatPreference("crosshair_correction", 0, -1f, 1f, pref);
		overlayHideDelay = new FloatPreference("overlay_hide_delay", 30f, 1f, 3600f, pref);
		// Boolean
		checkForUpdates = new BooleanPreference("check_for_updates", true, pref);
		translucent = new BooleanPreference("translucent", false, pref);
		alwaysOnTop = new BooleanPreference("always_on_top", true, pref);
		showNetherCoords = new BooleanPreference("show_nether_coords", true, pref);
		showAngleUpdates = new BooleanPreference("show_angle_updates", false, pref);
		showAngleErrors = new BooleanPreference("show_angle_errors", false, pref);
		autoReset = new BooleanPreference("auto_reset", false, pref);
		useAdvStatistics = new BooleanPreference("use_adv_statistics", true, pref);
		altClipboardReader = new BooleanPreference("alt_clipboard_reader", false, pref);
		useAltStd = new BooleanPreference("use_alt_std", false, pref);
		useOverlay = new BooleanPreference("use_obs_overlay", false, pref);
		overlayAutoHide = new BooleanPreference("overlay_auto_hide", false, pref);
		overlayHideWhenLocked = new BooleanPreference("overlay_lock_hide", false, pref);
		// Multiple choice
		strongholdDisplayType = new MultipleChoicePreference("stronghold_display_type", FOURFOUR, new int[] { 0, 1, 2 }, new String[] { FOURFOUR, EIGHTEIGHT, CHUNK }, pref);
		theme = new MultipleChoicePreference("theme", Theme.DARK.name, new int[] { 0, 1, 2 }, new String[] { Theme.LIGHT.name, Theme.DARK.name, Theme.BLUE.name }, pref);
		size = new MultipleChoicePreference("size", SizePreference.REGULAR.name, new int[] { 0, 1, 2 },
				new String[] { SizePreference.REGULAR.name, SizePreference.LARGE.name, SizePreference.EXTRALARGE.name }, pref);
		view = new MultipleChoicePreference("view", BASIC, new int[] { 0, 1 }, new String[] { BASIC, DETAILED }, pref);
		language = new MultipleChoicePreference("language", I18n.getDefaultName(), I18n.getLanguageIDs(), I18n.getLanguageNames(), pref);
		mcVersion = new MultipleChoicePreference("mc_version", PRE_119, new int[] { 0, 1 }, new String[] { PRE_119, POST_119 }, pref);
	}

}
