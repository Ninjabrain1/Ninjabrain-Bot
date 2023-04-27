package ninjabrainbot.model.datastate.endereye;

/**
 * Represents an eye of ender throw.
 */
public abstract class EnderEyeThrow implements IEnderEyeThrow {

	protected final double x, z, horizontalAngleWithoutCorrection, verticalAngle;
	protected final double correction;

	protected EnderEyeThrow(double x, double z, double horizontalAngle, double verticalAngle, double correction) {
		this.x = x;
		this.z = z;
		this.horizontalAngleWithoutCorrection = clampToPlusMinus180Degrees(horizontalAngle);
		this.verticalAngle = verticalAngle;
		this.correction = correction;
	}

	@Override
	public final double verticalAngle() {
		return 0;
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

}
