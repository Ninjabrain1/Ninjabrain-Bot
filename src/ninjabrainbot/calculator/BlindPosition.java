package ninjabrainbot.calculator;

public class BlindPosition {

	public final double x, z;

	public BlindPosition(double x, double z) {
		this.x = x;
		this.z = z;
	}

	/**
	 * Returns a BlindPosition object if the given string is the result of an F3+C
	 * command in the nether, null otherwise.
	 */
	public static BlindPosition parseF3C(String string) {
		if (!string.startsWith("/execute in minecraft:the_nether run tp @s"))
			return null;
		String[] substrings = string.split(" ");
		if (substrings.length != 11)
			return null;
		try {
			double x = Double.parseDouble(substrings[6]);
			double z = Double.parseDouble(substrings[8]);
			return new BlindPosition(x, z);
		} catch (NullPointerException | NumberFormatException e) {
			return null;
		}
	}

}
