package ninjabrainbot.data.endereye;

import ninjabrainbot.Main;

public class Throw1_12 extends Throw {

	public Throw1_12(double x, double z, double alpha, double beta, boolean nether) {
		super(x, z, alpha, beta, nether);
	}

	@Override
	public boolean isMcVersion1_12() {
		return true;
	}

	/**
	 * Returns a Throw object if the given string is the result of a manually
	 * written x/z/angle string, null otherwise.
	 */
	public static IThrow parseF3C(String string) {
		String[] substrings = string.split(" ");
		if (substrings.length != 3)
			return null;
		try {
			double x = Double.parseDouble(substrings[0]) + 0.5; // Add 0.5 because block coords should be used
			double z = Double.parseDouble(substrings[1]) + 0.5; // Add 0.5 because block coords should be used
			double alpha = Double.parseDouble(substrings[2]);
			alpha += Main.preferences.crosshairCorrection.get();
			return new Throw(x, z, alpha, -31, false);
		} catch (NullPointerException | NumberFormatException e) {
			return null;
		}
	}

}
