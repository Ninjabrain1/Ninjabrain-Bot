package ninjabrainbot.calculator;

/**
 * Represents an eye of ender throw.
 */
public class Throw implements Ray {

	public final double x, z, alpha;

	public Throw(double x, double z, double alpha) {
		this.x = x;
		this.z = z;
		alpha = alpha % 360.0;
		if (alpha < -180.0) {
			alpha += 360.0;
		} else if (alpha > 180.0) {
			alpha -= 360.0;
		}
		this.alpha = alpha;
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
		if (!string.startsWith("/execute in minecraft:overworld run tp @s"))
			return null;
		String[] substrings = string.split(" ");
		if (substrings.length != 11)
			return null;
		try {
			double x = Double.parseDouble(substrings[6]);
			double z = Double.parseDouble(substrings[8]);
			double alpha = Double.parseDouble(substrings[9]);
			return new Throw(x, z, alpha);
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

}
