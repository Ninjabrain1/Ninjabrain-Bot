package ninjabrainbot;

import ninjabrainbot.calculator.statistics.ApproximatedDensity;
import ninjabrainbot.calculator.stronghold.StrongholdConstants;
import ninjabrainbot.gui.GUI;
import ninjabrainbot.io.KeyboardListener;
import ninjabrainbot.io.NinjabrainBotPreferences;
import ninjabrainbot.util.I18n;
import ninjabrainbot.util.Profiler;

public class Main {

	public static final String VERSION = "1.3.2";
	public static NinjabrainBotPreferences preferences;

	public static void main(String[] args) {
		System.out.println("lang info: " + I18n.get("lang"));
		Profiler.start("Initialize preferences");
		preferences = new NinjabrainBotPreferences();
		StrongholdConstants.updateStrongholdChunkCoord();
		Profiler.stopAndStart("Calculate approximated density");
		ApproximatedDensity.init();
		Profiler.stopAndStart("Register keyboard listener");
		KeyboardListener.preInit();
		Profiler.stopAndStart("Initialize GUI");
		new GUI();
		Profiler.stop();
		Profiler.print();
	}

}
