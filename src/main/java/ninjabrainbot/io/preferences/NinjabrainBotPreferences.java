package ninjabrainbot.io.preferences;

import ninjabrainbot.io.preferences.MultipleChoicePreferenceDataTypes.Language;
import ninjabrainbot.io.preferences.MultipleChoicePreferenceDataTypes.MainViewType;
import ninjabrainbot.io.preferences.MultipleChoicePreferenceDataTypes.McVersion;
import ninjabrainbot.io.preferences.MultipleChoicePreferenceDataTypes.SizeSetting;
import ninjabrainbot.io.preferences.MultipleChoicePreferenceDataTypes.StrongholdDisplayType;
import ninjabrainbot.util.I18n;

public class NinjabrainBotPreferences {

	IPreferenceSource source;

	public IntPreference windowX;
	public IntPreference windowY;
	public IntPreference theme;
	public HotkeyPreference hotkeyIncrement;
	public HotkeyPreference hotkeyDecrement;
	public HotkeyPreference hotkeyBoat;
	public HotkeyPreference hotkeyReset;
	public HotkeyPreference hotkeyUndo;
	public HotkeyPreference hotkeyRedo;
	public HotkeyPreference hotkeyMinimize;
	public HotkeyPreference hotkeyAltStd;
	public HotkeyPreference hotkeyLock;
	public FloatPreference sigma;
	public FloatPreference sigmaAlt;
	public FloatPreference sigmaManual;
	public FloatPreference sigmaBoat;
	public FloatPreference crosshairCorrection;
	public FloatPreference resolutionHeight;
	public FloatPreference sensitivity;
	public FloatPreference boatErrorLimit;
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
	public BooleanPreference useTallRes;
	public BooleanPreference usePreciseAngle;
	public BooleanPreference useOverlay;
	public BooleanPreference overlayAutoHide;
	public BooleanPreference overlayHideWhenLocked;
	public BooleanPreference allAdvancements;
	public BooleanPreference informationMismeasureEnabled;
	public BooleanPreference informationDirectionHelpEnabled;
	public BooleanPreference informationCombinedCertaintyEnabled;
	public BooleanPreference informationPortalLinkingEnabled;
	public StringPreference customThemesString;
	public StringPreference customThemesNames;
	public MultipleChoicePreference<SizeSetting> size;
	public MultipleChoicePreference<StrongholdDisplayType> strongholdDisplayType;
	public MultipleChoicePreference<MainViewType> view;
	public MultipleChoicePreference<McVersion> mcVersion;
	public MultipleChoicePreference<Language> language;

	public NinjabrainBotPreferences(IPreferenceSource source) {
		this.source = source;
		// Integer
		windowX = new IntPreference("window_x", 100, source);
		windowY = new IntPreference("window_y", 100, source);
		theme = new IntPreference("theme", 1, source);
		// Hotkey
		hotkeyIncrement = new HotkeyPreference("hotkey_increment", source);
		hotkeyDecrement = new HotkeyPreference("hotkey_decrement", source);
		hotkeyReset = new HotkeyPreference("hotkey_reset", source);
		hotkeyUndo = new HotkeyPreference("hotkey_undo", source);
		hotkeyRedo = new HotkeyPreference("hotkey_redo", source);
		hotkeyMinimize = new HotkeyPreference("hotkey_minimize", source);
		hotkeyAltStd = new HotkeyPreference("hotkey_alt_std", source);
		hotkeyLock = new HotkeyPreference("hotkey_lock", source);
		hotkeyBoat = new HotkeyPreference("hotkey_boat", source);
		// Float
		sigma = new FloatPreference("sigma", 0.1f, 0.001f, 1f, source);
		sigmaAlt = new FloatPreference("sigma_alt", 0.1f, 0.001f, 1f, source);
		sigmaManual = new FloatPreference("sigma_manual", 0.03f, 0.001f, 1f, source);
		sigmaBoat = new FloatPreference("sigma_boat", 0.001f, 0.0001f, 1f, source);
		crosshairCorrection = new FloatPreference("crosshair_correction", 0, -1f, 1f, source);
		resolutionHeight = new FloatPreference("resolution_height", 16384, 1f, 16384f, source);
		sensitivity = new FloatPreference("sensitivity", 0.012727597f, 0f, 1f, source);
		boatErrorLimit = new FloatPreference("boat_error", 0.03f, 0f, 0.7f, source);
		overlayHideDelay = new FloatPreference("overlay_hide_delay", 30f, 1f, 3600f, source);
		// Boolean
		checkForUpdates = new BooleanPreference("check_for_updates", true, source);
		translucent = new BooleanPreference("translucent", false, source);
		alwaysOnTop = new BooleanPreference("always_on_top", true, source);
		showNetherCoords = new BooleanPreference("show_nether_coords", true, source);
		showAngleUpdates = new BooleanPreference("show_angle_updates", false, source);
		showAngleErrors = new BooleanPreference("show_angle_errors", false, source);
		autoReset = new BooleanPreference("auto_reset", false, source);
		useAdvStatistics = new BooleanPreference("use_adv_statistics", true, source);
		altClipboardReader = new BooleanPreference("alt_clipboard_reader", false, source);
		useAltStd = new BooleanPreference("use_alt_std", false, source);
		useTallRes = new BooleanPreference("use_tall_res", false, source);
		usePreciseAngle = new BooleanPreference("use_precise_angle", false, source);
		useOverlay = new BooleanPreference("use_obs_overlay", false, source);
		overlayAutoHide = new BooleanPreference("overlay_auto_hide", false, source);
		overlayHideWhenLocked = new BooleanPreference("overlay_lock_hide", false, source);
		allAdvancements = new BooleanPreference("all_advancements", false, source);
		informationMismeasureEnabled = new BooleanPreference("mismeasure_warning_enabled", true, source);
		informationDirectionHelpEnabled = new BooleanPreference("direction_help_enabled", false, source);
		informationCombinedCertaintyEnabled = new BooleanPreference("combined_offset_information_enabled", true, source);
		informationPortalLinkingEnabled = new BooleanPreference("portal_linking_warning_enabled", true, source);
		// String
		customThemesString = new StringPreference("custom_themes", "", source);
		customThemesNames = new StringPreference("custom_themes_names", "", source);
		// Multiple choice
		size = new MultipleChoicePreference<SizeSetting>("size", SizeSetting.SMALL, new int[] { 0, 1, 2 }, new SizeSetting[] { SizeSetting.SMALL, SizeSetting.MEDIUM, SizeSetting.LARGE }, source);
		strongholdDisplayType = new MultipleChoicePreference<StrongholdDisplayType>("stronghold_display_type", StrongholdDisplayType.FOURFOUR, new int[] { 0, 1, 2 },
				new StrongholdDisplayType[] { StrongholdDisplayType.FOURFOUR, StrongholdDisplayType.EIGHTEIGHT, StrongholdDisplayType.CHUNK }, source);
		view = new MultipleChoicePreference<MainViewType>("view", MainViewType.BASIC, new int[] { 0, 1 }, new MainViewType[] { MainViewType.BASIC, MainViewType.DETAILED }, source);
		mcVersion = new MultipleChoicePreference<McVersion>("mc_version", McVersion.PRE_119, new int[] { 0, 1 }, new McVersion[] { McVersion.PRE_119, McVersion.POST_119 }, source);
		language = new MultipleChoicePreference<Language>("language", I18n.getDefaultName(), I18n.getLanguageIDs(), I18n.getLanguageNames(), Language.values(), source);
	}

}
