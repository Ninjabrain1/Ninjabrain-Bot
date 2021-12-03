package ninjabrainbot;

import ninjabrainbot.calculator.ApproximatedDensity;
import ninjabrainbot.gui.GUI;
import ninjabrainbot.io.ClipboardReader;
import ninjabrainbot.io.KeyboardListener;
import ninjabrainbot.io.NinjabrainBotPreferences;
import ninjabrainbot.io.UpdateChecker;
import ninjabrainbot.util.Profiler;

public class Main {

	public static final String VERSION = " Beta 6";
	public static NinjabrainBotPreferences preferences;

	// TO-DO LIST
	// [x] Error message if throws dont intersect
	// [x] Options
	// [x] - std
	// [ ] - mean?
	// [x] - angle errors
	// [x] - show nether coords
	// [x] - show chunk location instead of block
	// [x] - theme
	// [x] - keep focused
	// [ ] - size
	// [x] - check for updates
	// [x] - translucent
	// [ ] - number of predictions
	// [x] - auto reset
	// [x] Pixel correction
	// [x] Number of blocks away
	// [ ] Blind travel (?)
	// [x] Certainty color code
	// [x] Remove throw
	// [ ] Link to guide
	// [ ] Copy result to clipboard
	// [x] Hotkeys
	// [x] Calibration
	// [ ] Show closest possible location

	public static void main(String[] args) {
		Profiler.start("Initialize preferences");
		preferences = new NinjabrainBotPreferences();
		Profiler.stopAndStart("Calculate approximated density");
		ApproximatedDensity.init();
		Profiler.stopAndStart("Register keyboard listener");
		KeyboardListener.preInit();
		Profiler.stopAndStart("Initialize GUI");
		GUI gui = new GUI();
		Profiler.stopAndStart("Start keyboard listener");
		KeyboardListener.init(gui);
		Profiler.stopAndStart("Start clipboard reader");
		ClipboardReader clipboardReader = new ClipboardReader(gui);
		Thread clipboardThread = new Thread(clipboardReader);
		clipboardThread.start();
		Profiler.stop();
		if (preferences.checkForUpdates.get()) {
			UpdateChecker.check(gui);
		}
		Profiler.print();
	}

}
