package ninjabrainbot.data.calculator.highprecision;

import ninjabrainbot.data.datalock.IModificationLock;
import ninjabrainbot.data.calculator.endereye.IThrow;
import ninjabrainbot.data.calculator.endereye.Throw;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;

public class BoatThrow extends Throw {

	public BoatThrow(double x, double z, double rawAlpha, double alpha, double beta, boolean nether, IModificationLock modificationLock) {
		super(x, 80, z, rawAlpha, alpha, beta, nether, modificationLock);
	}

	/**
	 * Returns a BoatThrow object if the given string is the result of an F3+C
	 * command, null otherwise.
	 */
	public static IThrow parseF3C(String string, NinjabrainBotPreferences preferences, IModificationLock modificationLock, float currentBoatAngle) {
		if (!(string.startsWith("/execute in minecraft:overworld run tp @s") || string.startsWith("/execute in minecraft:the_nether run tp @s"))) {
			return null;
		}
		String[] substrings = string.split(" ");
		if (substrings.length != 11)
			return null;
		try {
			boolean nether = substrings[2].equals("minecraft:the_nether");
			double x = Double.parseDouble(substrings[6]);
			double z = Double.parseDouble(substrings[8]);
			double rawAlpha = Double.parseDouble(substrings[9]);
			double beta = Double.parseDouble(substrings[10]);
			double alpha = getPreciseAlpha(rawAlpha, preferences, currentBoatAngle);
			return new BoatThrow(x, z, rawAlpha, alpha, beta, nether, modificationLock);
		} catch (NullPointerException | NumberFormatException e) {
			return null;
		}
	}

	private static double getPreciseAlpha(double alpha, NinjabrainBotPreferences preferences, float boatAngle) {
		double sens = preferences.sensitivity.get();
		double preMult = sens * 0.6f + 0.2f;
		preMult = preMult * preMult * preMult * 8.0f;
		double minInc = preMult * 0.15D;
		alpha = boatAngle + Math.round((alpha - boatAngle) / minInc) * minInc;

		return Throw.getPreciseAlpha(alpha, preferences.crosshairCorrection.get());
	}

	@Override
	public boolean isBoatThrow() {
		return true;
	}

}
