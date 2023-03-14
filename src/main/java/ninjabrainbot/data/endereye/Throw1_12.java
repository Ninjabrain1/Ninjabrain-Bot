package ninjabrainbot.data.endereye;

import ninjabrainbot.data.datalock.IModificationLock;

public class Throw1_12 extends Throw {

	public Throw1_12(double x, double z, double alpha, double beta, boolean nether, IModificationLock modificationLock) {
		this(x, z, alpha, alpha, beta, nether, modificationLock);
	}

	public Throw1_12(double x, double z, double rawAlpha, double alpha, double beta, boolean nether, IModificationLock modificationLock) {
		super(x, z, rawAlpha, alpha, beta, nether, modificationLock);
	}

	@Override
	public boolean isMcVersion1_12() {
		return true;
	}

	/**
	 * Returns a Throw object if the given string is the result of a manually
	 * written x/z/angle string, null otherwise.
	 */
	public static IThrow parseF3C(String string, double crosshairCorrection, IModificationLock modificationLock) {
		String[] substrings = string.split(" ");
		if (substrings.length != 3)
			return null;
		try {
			double x = Double.parseDouble(substrings[0]) + 0.5; // Add 0.5 because block coords should be used
			double z = Double.parseDouble(substrings[1]) + 0.5; // Add 0.5 because block coords should be used
			double rawAlpha = Double.parseDouble(substrings[2]);
			double alpha = rawAlpha + crosshairCorrection;
			return new Throw(x, z, rawAlpha, alpha, -31, false, modificationLock);
		} catch (NullPointerException | NumberFormatException e) {
			return null;
		}
	}

}
