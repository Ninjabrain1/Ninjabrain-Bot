package ninjabrainbot.util;

import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import org.junit.jupiter.api.Assertions;

/**
 * Represents a F3+C input, with subpixel, for testing purposes.
 */
public class TestF3C {

	public final String F3C;
	public final int subpixelCorrection;

	/**
	 * Data should be on the form "x z angle", or "x z angle(+/-)i" if subpixel corrected.
	 */
	public TestF3C(String data){
		String[] substrings = data.split(" ");
		Assertions.assertEquals(3, substrings.length);
		String x = substrings[0];
		String z = substrings[1];
		String angle = substrings[2];
		int subpixelCorrection = 0;
		if (angle.contains("-")){
			String[] angleSubstrings = angle.split("-");
			angle = angleSubstrings[0];
			subpixelCorrection = -Integer.parseInt(angleSubstrings[1]);
		}
		if (angle.contains("+")){
			String[] angleSubstrings = angle.split("\\+");
			angle = angleSubstrings[0];
			subpixelCorrection = Integer.parseInt(angleSubstrings[1]);
		}
		F3C = String.format("/execute in minecraft:overworld run tp @s %s 69.00 %s %s -31.43", x, z, angle);
		this.subpixelCorrection = subpixelCorrection;
	}

	public void executeSubpixelCorrectionHotkeys(NinjabrainBotPreferences preferences){
		for (int i = 0; i < subpixelCorrection; i++){
			preferences.hotkeyIncrement.execute();
		}
		for (int i = 0; i < -subpixelCorrection; i++){
			preferences.hotkeyDecrement.execute();
		}
	}

}
