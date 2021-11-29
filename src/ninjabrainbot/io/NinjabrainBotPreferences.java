package ninjabrainbot.io;

import java.util.prefs.Preferences;

import ninjabrainbot.Main;
import ninjabrainbot.gui.GUI;

public class NinjabrainBotPreferences {
	
	Preferences pref;
	
	public IntPreference windowX;
	public IntPreference windowY;
	public FloatPreference sigma;
	public BooleanPreference checkForUpdates;
	public BooleanPreference translucent;
	public BooleanPreference alwaysOnTop;
	public BooleanPreference darkTheme;
	public BooleanPreference showNetherCoords;
	public BooleanPreference showAdvancedOptions;
	public BooleanPreference showAngleErrors;
	public BooleanPreference autoReset;
	public MultipleChoicePreference strongholdDisplayType;
	
	public static final String FOURFOUR = "(4, 4)";
	public static final String EIGHTEIGHT = "(8, 8)";
	public static final String CHUNK = "Chunk";
	
	public NinjabrainBotPreferences() {
		pref = Preferences.userNodeForPackage(Main.class);
		windowX = new IntPreference("window_x", 100, pref);
		windowY = new IntPreference("window_y", 100, pref);
		sigma = new FloatPreference("sigma", 0.05f, 0.001f, 1f, pref) {
			@Override
			public void onChangedByUser(GUI gui) {
				gui.getTriangulator().setSigma(get());
				gui.recalculateStronghold();
			}
		};
		checkForUpdates = new BooleanPreference("check_for_updates", true, pref) {
			@Override
			public void onChangedByUser(GUI gui) {
				if (get())
					UpdateChecker.check(gui);
				gui.setNotificationsEnabled(get());
			}
		};
		translucent = new BooleanPreference("translucent", false, pref) {
			@Override
			public void onChangedByUser(GUI gui) {
				gui.setTranslucent(get());
			}
		};
		alwaysOnTop = new BooleanPreference("always_on_top", true, pref) {
			@Override
			public void onChangedByUser(GUI gui) {
				gui.setAlwaysOnTop(get());
			}
		};
		darkTheme = new BooleanPreference("dark_theme", true, pref) {
			@Override
			public void onChangedByUser(GUI gui) {
				gui.updateTheme();
			}
		};
		showNetherCoords = new BooleanPreference("show_nether_coords", true, pref) {
			@Override
			public void onChangedByUser(GUI gui) {
				gui.setNetherCoordsEnabled(get());
			}
		};
		showAdvancedOptions = new BooleanPreference("show_advanced_options", false, pref) {
			@Override
			public void onChangedByUser(GUI gui) {
				gui.setAdvancedOptionsEnabled(get());
			}
		};
		showAngleErrors = new BooleanPreference("show_angle_errors", false, pref) {
			@Override
			public void onChangedByUser(GUI gui) {
				gui.setAngleErrorsEnabled(get());
			}
		};
		autoReset = new BooleanPreference("auto_reset", false, pref) {
			@Override
			public void onChangedByUser(GUI gui) {
				if (get()) {
					gui.autoResetTimer.start();
				} else {
					gui.autoResetTimer.stop();
				}
			}
		};
		strongholdDisplayType = new MultipleChoicePreference("stronghold_display_type", FOURFOUR, new int[] {0, 1, 2}, new String[] {FOURFOUR, EIGHTEIGHT, CHUNK}, pref) {
			@Override
			public void onChangedByUser(GUI gui) {
				gui.recalculateStronghold();
			}
		};
	}
	
}
