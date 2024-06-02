package ninjabrainbot.io.preferences;

import com.sun.jna.Platform;
import ninjabrainbot.io.KeyConverter;
import ninjabrainbot.io.preferences.enums.*;
import ninjabrainbot.util.Assert;

public class NinjabrainBotPreferences {

	final IPreferenceSource source;

	public final IntPreference windowX;
	public final IntPreference windowY;
	public final IntPreference theme;
	private final IntPreference settingsVersion;
	public final HotkeyPreference hotkeyIncrement;
	public final HotkeyPreference hotkeyDecrement;
	public final HotkeyPreference hotkeyBoat;
	public final HotkeyPreference hotkeyMod360;
	public final HotkeyPreference hotkeyReset;
	public final HotkeyPreference hotkeyUndo;
	public final HotkeyPreference hotkeyRedo;
	public final HotkeyPreference hotkeyMinimize;
	public final HotkeyPreference hotkeyAltStd;
	public final HotkeyPreference hotkeyLock;
	public final HotkeyPreference hotkeyToggleAllAdvancementsMode;
	public final FloatPreference sensitivityManual;
	public final FloatPreference sigma;
	public final FloatPreference sigmaAlt;
	public final FloatPreference sigmaManual;
	public final FloatPreference sigmaBoat;
	public final FloatPreference resolutionHeight;
	public final FloatPreference boatErrorLimit;
	public final FloatPreference overlayHideDelay;
	public final DoublePreference sensitivityAutomatic;
	public final DoublePreference customAdjustment;
	public final DoublePreference crosshairCorrection;
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
	public final BooleanPreference colorCodeNegativeCoords;
	public final BooleanPreference usePreciseAngle;
	public final BooleanPreference useOverlay;
	public final BooleanPreference enableHttpServer;
	public final BooleanPreference overlayAutoHide;
	public final BooleanPreference overlayHideWhenLocked;
	public final BooleanPreference allAdvancements;
	public final BooleanPreference informationMismeasureEnabled;
	public final BooleanPreference informationDirectionHelpEnabled;
	public final BooleanPreference informationCombinedCertaintyEnabled;
	public final BooleanPreference informationPortalLinkingEnabled;
	public final StringPreference customThemesString;
	public final StringPreference customThemesNames;
	public final StringPreference language;
	public final MultipleChoicePreference<SizeSetting> size;
	public final MultipleChoicePreference<StrongholdDisplayType> strongholdDisplayType;
	public final MultipleChoicePreference<MainViewType> view;
	public final MultipleChoicePreference<McVersion> mcVersion;
	public final MultipleChoicePreference<AllAdvancementsToggleType> allAdvancementsToggleType;
	public final MultipleChoicePreference<DefaultBoatType> defaultBoatType;
	public final MultipleChoicePreference<AngleAdjustmentType> angleAdjustmentType;

	public NinjabrainBotPreferences(IPreferenceSource source) {
		this.source = source;
		// Integer
		windowX = new IntPreference("window_x", 100, source);
		windowY = new IntPreference("window_y", 100, source);
		theme = new IntPreference("theme", 1, source);
		settingsVersion = new IntPreference("settings_version", 0, source);
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
		hotkeyMod360 = new HotkeyPreference("hotkey_mod_360", source);
		hotkeyToggleAllAdvancementsMode = new HotkeyPreference("hotkey_toggle_aa_mode", source);
		// Float
		sensitivityManual = new FloatPreference("sensitivity_manual", 0.4341732f, 0f, 1f, source);
		sigma = new FloatPreference("sigma", 0.1f, 0.001f, 1f, source);
		sigmaAlt = new FloatPreference("sigma_alt", 0.1f, 0.001f, 1f, source);
		sigmaManual = new FloatPreference("sigma_manual", 0.03f, 0.001f, 1f, source);
		sigmaBoat = new FloatPreference("sigma_boat", 0.001f, 0.0001f, 1f, source);
		resolutionHeight = new FloatPreference("resolution_height", 16384, 1f, 16384f, source);
		boatErrorLimit = new FloatPreference("boat_error", 0.03f, 0f, 0.7f, source);
		overlayHideDelay = new FloatPreference("overlay_hide_delay", 30f, 1f, 3600f, source);
		// Double
		sensitivityAutomatic = new DoublePreference("sensitivity", 0.012727597f, 0f, 1f, source);
		customAdjustment = new DoublePreference("custom_adjustment", 0.01, 0f, 1f, source);
		crosshairCorrection = new DoublePreference("crosshair_correction", 0, -1f, 1f, source);
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
		colorCodeNegativeCoords = new BooleanPreference("color_negative_coords", false, source);
		usePreciseAngle = new BooleanPreference("use_precise_angle", false, source);
		useOverlay = new BooleanPreference("use_obs_overlay", false, source);
		enableHttpServer = new BooleanPreference("enable_http_server", false, source);
		overlayAutoHide = new BooleanPreference("overlay_auto_hide", false, source);
		overlayHideWhenLocked = new BooleanPreference("overlay_lock_hide", false, source);
		allAdvancements = new BooleanPreference("all_advancements", false, source);
		informationMismeasureEnabled = new BooleanPreference("mismeasure_warning_enabled", false, source);
		informationDirectionHelpEnabled = new BooleanPreference("direction_help_enabled", false, source);
		informationCombinedCertaintyEnabled = new BooleanPreference("combined_offset_information_enabled", true, source);
		informationPortalLinkingEnabled = new BooleanPreference("portal_linking_warning_enabled", true, source);
		// String
		customThemesString = new StringPreference("custom_themes", "", source);
		customThemesNames = new StringPreference("custom_themes_names", "", source);
		language = new StringPreference("language_v2", "", source);
		// Multiple choice
		size = new MultipleChoicePreference<>("size", SizeSetting.SMALL, new int[] { 0, 1, 2 }, new SizeSetting[] { SizeSetting.SMALL, SizeSetting.MEDIUM, SizeSetting.LARGE }, source);
		strongholdDisplayType = new MultipleChoicePreference<>("stronghold_display_type", StrongholdDisplayType.FOURFOUR, new int[] { 0, 1, 2 },
				new StrongholdDisplayType[] { StrongholdDisplayType.FOURFOUR, StrongholdDisplayType.EIGHTEIGHT, StrongholdDisplayType.CHUNK }, source);
		view = new MultipleChoicePreference<>("view", MainViewType.BASIC, new int[] { 0, 1 }, new MainViewType[] { MainViewType.BASIC, MainViewType.DETAILED }, source);
		mcVersion = new MultipleChoicePreference<>("mc_version", McVersion.PRE_119, new int[] { 0, 1 }, new McVersion[] { McVersion.PRE_119, McVersion.POST_119 }, source);
		allAdvancementsToggleType = new MultipleChoicePreference<>("aa_toggle_type", AllAdvancementsToggleType.Automatic, new int[] { 0, 1 }, new AllAdvancementsToggleType[] { AllAdvancementsToggleType.Automatic, AllAdvancementsToggleType.Hotkey }, source);
		defaultBoatType = new MultipleChoicePreference<>("default_boat_type", DefaultBoatType.GRAY, new int[] { 0, 1, 2 },
				new DefaultBoatType[] { DefaultBoatType.GRAY, DefaultBoatType.BLUE, DefaultBoatType.GREEN }, source);
		angleAdjustmentType = new MultipleChoicePreference<>("angle_adjustment_type", AngleAdjustmentType.SUBPIXEL, new int[] { 0, 1, 2 },
				new AngleAdjustmentType[] { AngleAdjustmentType.SUBPIXEL, AngleAdjustmentType.TALL, AngleAdjustmentType.CUSTOM }, source);

		// Upgrade if necessary
		if (settingsVersion.get() == 0)
			upgradeSettings_From_0_To_1();
		if (settingsVersion.get() == 1)
			upgradeSettings_From_1_To_2();

		Assert.isTrue(settingsVersion.get() >= 2); // Do >= to allow users to downgrade to an earlier version, in case newer version has issues
	}

	private void upgradeSettings_From_0_To_1(){
		for (HotkeyPreference hotkeyPreference : HotkeyPreference.hotkeys){
			if (hotkeyPreference.getCode() == -1)
				continue;
			int nativeKeyCode = KeyConverter.convertKeyCodeToNativeKeyCode(hotkeyPreference.getCode());
			hotkeyPreference.setCode(nativeKeyCode);
		}
		settingsVersion.set(1);
	}

	private void upgradeSettings_From_1_To_2(){
		if (!Platform.isLinux()){
			KeyConverter keyConverter = new KeyConverter();
			for (HotkeyPreference hotkeyPreference : HotkeyPreference.hotkeys){
				if (hotkeyPreference.getCode() == -1)
					continue;
				int keyCode = keyConverter.convertNativeKeyCodeToKeyCode(hotkeyPreference.getCode());
				hotkeyPreference.setCode(keyCode);
			}
		}
		settingsVersion.set(2);
	}

}
