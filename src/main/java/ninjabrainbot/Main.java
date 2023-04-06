package ninjabrainbot;

import java.util.Locale;

import ninjabrainbot.data.calculator.statistics.ApproximatedDensity;
import ninjabrainbot.gui.GUI;
import ninjabrainbot.gui.splash.Progress;
import ninjabrainbot.gui.splash.Splash;
import ninjabrainbot.io.KeyboardListener;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.io.preferences.SavedPreferences;
import ninjabrainbot.util.I18n;
import ninjabrainbot.util.Logger;
import ninjabrainbot.util.Profiler;

public class Main {

	public static final String VERSION = "1.4.0-pre";

	public static void main(String[] args) {
		Progress.init(new Splash());
		Progress.setTask("Loading language", 0.02f);
		Profiler.start("Initialize language");
		Logger.log("Language: " + I18n.get("lang"));

		Progress.setTask("Loading preferences", 0.03f);
		Profiler.stopAndStart("Initialize preferences");
		NinjabrainBotPreferences preferences = new NinjabrainBotPreferences(new SavedPreferences());

		Progress.setTask("Calculating approximated stronghold density", 0.04f);
		Profiler.stopAndStart("Calculate approximated density");
		ApproximatedDensity.init();

		Progress.setTask("Starting keyboard listener", 0.07f);
		Profiler.stopAndStart("Register keyboard listener");
		KeyboardListener.preInit();

		Progress.startCompoundTask("", 1f);
		Profiler.stopAndStart("Initialize GUI");
		Locale.setDefault(Locale.US);
		new GUI(preferences);
		Progress.endCompoundTask();

		Profiler.stop();
		Profiler.print();
	}

}
