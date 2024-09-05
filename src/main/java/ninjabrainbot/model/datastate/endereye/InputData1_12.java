package ninjabrainbot.model.datastate.endereye;

public class InputData1_12 {

	public final double x, z, horizontalAngle;
	public final int correctionIncrements;

	private InputData1_12(double x, double z, double horizontalAngle, int correctionIncrements) {
		this.x = x;
		this.z = z;
		this.horizontalAngle = horizontalAngle;
		this.correctionIncrements = correctionIncrements;
	}

	public static InputData1_12 parseInputString(String string) {
		String[] substrings = string.split(" ");
		if (substrings.length < 3 || substrings.length > 4)
			return null;
		try {
			double x = Double.parseDouble(substrings[0]);
			if (!substrings[0].contains("."))
				x += 0.5; // Add 0.5 if block coords are being used

			double z = Double.parseDouble(substrings[1]);
			if (!substrings[1].contains("."))
				z += 0.5; // Add 0.5 if block coords are being used

			double rawAlpha = Double.parseDouble(substrings[2]);

			int correction = 0;
			if (substrings.length == 4)
				correction = Integer.parseInt(substrings[3]);

			return new InputData1_12(x, z, rawAlpha, correction);
		} catch (NullPointerException | NumberFormatException e) {
			return null;
		}
	}

}
