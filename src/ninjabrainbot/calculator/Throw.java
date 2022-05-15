package ninjabrainbot.calculator;

import ninjabrainbot.Main;

/**
 * Represents an eye of ender throw.
 */
public class Throw implements Ray {

	// correction is how much the angle has been corrected, only used for display purposes (the correction has already been added to alpha)
	public final double x, z, alpha, beta, correction;
	public final boolean altStd;

	private final boolean nether;
	
	public Throw(double x, double z, double alpha, double beta, double correction, boolean nether) {
		this(x, z, alpha, beta, correction, false, nether);
	}
	
	public Throw(double x, double z, double alpha, double beta, double correction, boolean altStd, boolean nether) {
		this.x = x;
		this.z = z;
		this.correction = correction;
		alpha = alpha % 360.0;
		if (alpha < -180.0) {
			alpha += 360.0;
		} else if (alpha > 180.0) {
			alpha -= 360.0;
		}
		this.alpha = alpha;
		this.beta = beta;
		this.altStd = altStd;
		this.nether = nether;
	}
	
	@Override
	public String toString() {
		return "x=" + x + ", z=" + z + ", alpha=" + alpha;
	}

	/**
	 * Returns a Throw object if the given string is the result of an F3+C command
	 * in the overworld, null otherwise.
	 */
	public static Throw parseF3C(String string) {
		if (!(string.startsWith("/execute in minecraft:overworld run tp @s") ||
				string.startsWith("/execute in minecraft:the_nether run tp @s"))) {
			return null;
		}
		String[] substrings = string.split(" ");
		if (substrings.length != 11)
			return null;
		try {
			boolean nether = substrings[2].equals("minecraft:the_nether");
			double x = Double.parseDouble(substrings[6]);
			double z = Double.parseDouble(substrings[8]);
			double alpha = Double.parseDouble(substrings[9]);
			double beta = Double.parseDouble(substrings[10]);
			alpha += Main.preferences.crosshairCorrection.get();
			return new Throw(x, z, alpha, beta, 0, nether);
		} catch (NullPointerException | NumberFormatException e) {
			return null;
		}
	}
	
	/**
	 * Returns the squared distance between this throw and the given throw.
	 */
	public double distance2(Throw other) {
		double dx = x - other.x;
		double dz = z - other.z;
		return dx * dx + dz * dz;
	}
	
	public Throw withToggledSTD() {
		return new Throw(x, z, alpha, beta, correction, !this.altStd, this.nether);
	}
	
	public boolean lookingBelowHorizon() {
		return beta > 0;
	}
	
	@Override
	public double x() {
		return x;
	}

	@Override
	public double z() {
		return z;
	}

	@Override
	public double alpha() {
		return alpha;
	}

	public boolean isNether() {
		return nether;
	}

	public BlindPosition toBlind() {
		return new BlindPosition(x, z);
	}
}
