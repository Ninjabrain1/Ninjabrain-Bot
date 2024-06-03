package ninjabrainbot.model.datastate.endereye;

/**
 * Represents an eye of ender throw.
 */
public abstract class EnderEyeThrow implements IEnderEyeThrow {

	protected final double x, z, horizontalAngleWithoutCorrection, verticalAngle;
	protected final double correction;
	protected final int correctionIncrements;

	protected EnderEyeThrow(double x, double z, double horizontalAngle, double verticalAngle, double correction, int correctionIncrements) {
		this.x = x;
		this.z = z;
		this.horizontalAngleWithoutCorrection = clampToPlusMinus180Degrees(horizontalAngle);
		this.verticalAngle = verticalAngle;
		this.correction = correction;
		this.correctionIncrements = correctionIncrements;
	}

	@Override
	public final double verticalAngle() {
		return verticalAngle;
	}


	@Override
	public String toString() {
		return "x=" + x + ", z=" + z + ", alpha=" + horizontalAngleWithoutCorrection;
	}

	@Override
	public double xInOverworld() {
		return x;
	}

	@Override
	public double zInOverworld() {
		return z;
	}

	@Override
	public double horizontalAngle() {
		return horizontalAngleWithoutCorrection + correction;
	}

	@Override
	public double correction() {
		return correction;
	}

	@Override
	public int correctionIncrements() {
		return correctionIncrements;
	}

	@Override
	public double horizontalAngleWithoutCorrection() {
		return horizontalAngleWithoutCorrection;
	}

	private static double clampToPlusMinus180Degrees(double angleInDegrees) {
		angleInDegrees %= 360.0;
		if (angleInDegrees < -180.0) {
			angleInDegrees += 360.0;
		} else if (angleInDegrees > 180.0) {
			angleInDegrees -= 360.0;
		}
		return angleInDegrees;
	}

	protected static double getCorrectedHorizontalAngle(double alpha, double crosshairCorrection) {
		alpha += crosshairCorrection;

		// Caused by rounding in client-bound move entity packets
		alpha -= 0.000824 * Math.sin((alpha + 45) * Math.PI / 180.0);

		return alpha;
	}

}
