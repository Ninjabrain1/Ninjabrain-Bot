package ninjabrainbot.model.datastate.endereye;

public class InputData1_12 {

	public final double x, z, horizontalAngle;

	private InputData1_12(double x, double z, double horizontalAngle) {
		this.x = x;
		this.z = z;
		this.horizontalAngle = horizontalAngle;
	}

	public static InputData1_12 parseInputString(String string) {
		String[] substrings = string.split(" ");
		if (substrings.length != 3)
			return null;
		try {
			double x = Double.parseDouble(substrings[0]);
			if ((int) x == x)
				x += 0.5; // Add 0.5 if block coords are being used

			double z = Double.parseDouble(substrings[1]);
			if ((int) z == z)
				z += 0.5; // Add 0.5 if block coords are being used

			double rawAlpha = Double.parseDouble(substrings[2]);
			return new InputData1_12(x, z, rawAlpha);
		} catch (NullPointerException | NumberFormatException e) {
			return null;
		}
	}

}
