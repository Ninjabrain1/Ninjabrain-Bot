package ninjabrainbot.io;

import java.util.prefs.Preferences;

import javax.swing.SwingUtilities;

import ninjabrainbot.Main;
import ninjabrainbot.gui.GUI;
import ninjabrainbot.gui.SizePreference;
import ninjabrainbot.gui.Theme;
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
	public FloatPreference crosshairCorrection;
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
	public MultipleChoicePreference strongholdDisplayType;
	public MultipleChoicePreference theme;
	public MultipleChoicePreference size;
	public MultipleChoicePreference stdToggleMode;
	public MultipleChoicePreference view;

	public static final String FOURFOUR = "(4, 4)";
	public static final String EIGHTEIGHT = "(8, 8)";
	public static final String CHUNK = I18n.get("chunk");
	public static final String BASIC = I18n.get("basic");
	public static final String DETAILED = I18n.get("detailed");

	public NinjabrainBotPreferences() {
		pref = Preferences.userNodeForPackage(Main.class);
		windowX = new IntPreference("window_x", 100, pref);
		windowY = new IntPreference("window_y", 100, pref);
		hotkeyIncrement = new HotkeyPreference("hotkey_increment", pref) {
			@Override
			public void execute(GUI gui) {
				SwingUtilities.invokeLater(() -> {
					if (!gui.isTargetLocked()) {
						gui.changeLastAngle(0.01f);
					}
				});
			}
		};
		hotkeyDecrement = new HotkeyPreference("hotkey_decrement", pref) {
			@Override
			public void execute(GUI gui) {
				SwingUtilities.invokeLater(() -> {
					if (!gui.isTargetLocked()) {
						gui.changeLastAngle(-0.01f);
					}
				});
			}
		};
		hotkeyReset = new HotkeyPreference("hotkey_reset", pref) {
			@Override
			public void execute(GUI gui) {
				SwingUtilities.invokeLater(() -> {
					if (!gui.isTargetLocked()) {
						gui.resetThrows();
					}
				});
			}
		};
		hotkeyUndo = new HotkeyPreference("hotkey_undo", pref) {
			@Override
			public void execute(GUI gui) {
				SwingUtilities.invokeLater(() -> {
					if (!gui.isTargetLocked()) {
						gui.undo();
					}
				});
			}
		};
		hotkeyMinimize = new HotkeyPreference("hotkey_minimize", pref) {
			@Override
			public void execute(GUI gui) {
				SwingUtilities.invokeLater(() -> gui.toggleMinimized());
			}
		};
		hotkeyAltStd = new HotkeyPreference("hotkey_alt_std", pref) {
			@Override
			public void execute(GUI gui) {
				if (Main.preferences.useAltStd.get()) {
					SwingUtilities.invokeLater(() -> {
						if (!gui.isTargetLocked()) {
							gui.toggleLastSTD();
						}
					});
				}
			}
		};
		hotkeyLock = new HotkeyPreference("hotkey_lock", pref) {
			@Override
			public void execute(GUI gui) {
				SwingUtilities.invokeLater(() -> gui.toggleTargetLocked());
			}
		};
		sigma = new FloatPreference("sigma", 0.1f, 0.001f, 1f, pref) {
			@Override
			public void onChangedByUser(GUI gui) {
				gui.getTriangulator().setSigma(get());
				gui.recalculateStronghold();
			}
		};
		sigmaAlt = new FloatPreference("sigma_alt", 0.1f, 0.001f, 1f, pref) {
			@Override
			public void onChangedByUser(GUI gui) {
				gui.getTriangulator().setSigmaAlt(get());
				gui.recalculateStronghold();
			}
		};
		crosshairCorrection = new FloatPreference("crosshair_correction", 0, -1f, 1f, pref) {
			@Override
			public void onChangedByUser(GUI gui) {
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
		showNetherCoords = new BooleanPreference("show_nether_coords", true, pref) {
			@Override
			public void onChangedByUser(GUI gui) {
				gui.setNetherCoordsEnabled(get());
			}
		};
		showAngleUpdates = new BooleanPreference("show_angle_updates", false, pref) {
			@Override
			public void onChangedByUser(GUI gui) {
				gui.setAngleUpdatesEnabled(get());
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
		useAdvStatistics = new BooleanPreference("use_adv_statistics", true, pref) {
			@Override
			public void onChangedByUser(GUI gui) {
				SwingUtilities.invokeLater(() -> gui.recalculateStronghold());
			}
		};
		altClipboardReader = new BooleanPreference("alt_clipboard_reader", false, pref) {
			@Override
			public void onChangedByUser(GUI gui) {
			}
		};
		useAltStd = new BooleanPreference("use_alt_std", false, pref) {
			@Override
			public void onChangedByUser(GUI gui) {
				SwingUtilities.invokeLater(() -> gui.optionsFrame.setAltSigmaEnabled(get()));
			}
		};
		useOverlay = new BooleanPreference("use_obs_overlay", false, pref) {
			@Override
			public void onChangedByUser(GUI gui) {
				SwingUtilities.invokeLater(() -> gui.setOverlayEnabled(get()));
			}
		};
		strongholdDisplayType = new MultipleChoicePreference("stronghold_display_type", FOURFOUR, new int[] { 0, 1, 2 },
				new String[] { FOURFOUR, EIGHTEIGHT, CHUNK }, pref) {
			@Override
			public void onChangedByUser(GUI gui) {
				SwingUtilities.invokeLater(() -> gui.recalculateStronghold());
			}
		};
		theme = new MultipleChoicePreference("theme", Theme.DARK.name, new int[] { 0, 1, 2 },
				new String[] { Theme.LIGHT.name, Theme.DARK.name, Theme.BLUE.name }, pref) {
			@Override
			public void onChangedByUser(GUI gui) {
				SwingUtilities.invokeLater(() -> gui.updateTheme());
			}
		};
		size = new MultipleChoicePreference("size", SizePreference.REGULAR.name, new int[] { 0, 1, 2 },
				new String[] { SizePreference.REGULAR.name, SizePreference.LARGE.name, SizePreference.EXTRALARGE.name },
				pref) {
			@Override
			public void onChangedByUser(GUI gui) {
				SwingUtilities.invokeLater(() -> gui.updateSizePreference());
			}
		};
		view = new MultipleChoicePreference("view", BASIC, new int[] { 0, 1 }, new String[] { BASIC, DETAILED }, pref) {
			@Override
			public void onChangedByUser(GUI gui) {
				SwingUtilities.invokeLater(() -> gui.recalculateStronghold());
			}
		};
	}

}
