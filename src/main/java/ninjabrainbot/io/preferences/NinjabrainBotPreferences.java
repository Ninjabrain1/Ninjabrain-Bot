package ninjabrainbot.io.preferences;

import ninjabrainbot.io.preferences.MultipleChoicePreferenceDataTypes.Language;
import ninjabrainbot.io.preferences.MultipleChoicePreferenceDataTypes.MainViewType;
import ninjabrainbot.io.preferences.MultipleChoicePreferenceDataTypes.McVersion;
import ninjabrainbot.io.preferences.MultipleChoicePreferenceDataTypes.SizeSetting;
import ninjabrainbot.io.preferences.MultipleChoicePreferenceDataTypes.StrongholdDisplayType;
import ninjabrainbot.util.I18n;

public class NinjabrainBotPreferences {

	final IPreferenceSource source;

	public final IntPreference windowX;
	public final IntPreference windowY;
	public final IntPreference theme;
	public final HotkeyPreference hotkeyIncrement;
	public final HotkeyPreference hotkeyDecrement;
	public final HotkeyPreference hotkeyBoat;
	public final HotkeyPreference hotkeyReset;
	public final HotkeyPreference hotkeyUndo;
	public final HotkeyPreference hotkeyRedo;
	public final HotkeyPreference hotkeyMinimize;
	public final HotkeyPreference hotkeyAltStd;
	public final HotkeyPreference hotkeyLock;
	public final FloatPreference sigma;
	public final FloatPreference sigmaAlt;
	public final FloatPreference sigmaManual;
	public final FloatPreference sigmaBoat;
	public final FloatPreference crosshairCorrection;
	public final FloatPreference resolutionHeight;
	public final FloatPreference sensitivity;
	public final FloatPreference boatErrorLimit;
	public final FloatPreference overlayHideDelay;
	public final BooleanPreference checkForUpdates;
	public final BooleanPreference translucent;
	public final BooleanPreference alwaysOnTop;
	public final BooleanPreference showNetherCoords;
	public final BooleanPreference showAngleUpdates;
	public final BooleanPreference showAngleErrors;
	public final BooleanPreference autoReset;
	public final BooleanPreference autoResetWhenChangingInstance;
	public final BooleanPreference useAdvStatistics;
	public final BooleanPreference altClipboardReader;
	public final BooleanPreference useAltStd;
	public final BooleanPreference useTallRes;
	public final BooleanPreference usePreciseAngle;
	public final BooleanPreference useOverlay;
	public final BooleanPreference overlayAutoHide;
	public final BooleanPreference overlayHideWhenLocked;
	public final BooleanPreference allAdvancements;
	public final BooleanPreference informationMismeasureEnabled;
	public final BooleanPreference informationDirectionHelpEnabled;
	public final BooleanPreference informationCombinedCertaintyEnabled;
	public final BooleanPreference informationPortalLinkingEnabled;
	public final StringPreference customThemesString;
	public final StringPreference customThemesNames;
	public final MultipleChoicePreference<SizeSetting> size;
	public final MultipleChoicePreference<StrongholdDisplayType> strongholdDisplayType;
	public final MultipleChoicePreference<MainViewType> view;
	public final MultipleChoicePreference<McVersion> mcVersion;
	public final MultipleChoicePreference<Language> language;

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
		autoResetWhenChangingInstance = new BooleanPreference("auto_reset_on_instance_change", false, source);
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
