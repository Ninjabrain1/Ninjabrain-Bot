package ninjabrainbot;

import ninjabrainbot.calculator.ApproximatedDensity;
import ninjabrainbot.gui.GUI;
import ninjabrainbot.io.ClipboardReader;
import ninjabrainbot.io.KeyboardListener;
import ninjabrainbot.io.NinjabrainBotPreferences;
import ninjabrainbot.io.UpdateChecker;
import ninjabrainbot.util.I18n;
import ninjabrainbot.util.Profiler;

public class Main {

	public static final String VERSION = "1.3.0";
	public static NinjabrainBotPreferences preferences;

	public static void main(String[] args) {
		System.out.println("lang info: " + I18n.get("lang"));
		Profiler.start("Initialize preferences");
		preferences = new NinjabrainBotPreferences();
		Profiler.stopAndStart("Calculate approximated density");
		ApproximatedDensity.init();
		Profiler.stopAndStart("Register keyboard listener");
		KeyboardListener.preInit();
		Profiler.stopAndStart("Initialize GUI");
		GUI gui = new GUI();
		Profiler.stopAndStart("Start clipboard reader");
		ClipboardReader clipboardReader = new ClipboardReader(gui);
		Thread clipboardThread = new Thread(clipboardReader);
		Profiler.stopAndStart("Start keyboard listener");
		KeyboardListener.init(gui, clipboardReader);
		clipboardThread.start();
		Profiler.stop();
		if (preferences.checkForUpdates.get()) {
			UpdateChecker.check(gui);
		}
		Profiler.print();
	}

}
