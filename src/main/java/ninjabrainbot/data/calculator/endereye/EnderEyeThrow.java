package ninjabrainbot.data.calculator.endereye;

import ninjabrainbot.data.calculator.common.IPlayerPosition;
import ninjabrainbot.data.calculator.common.LimitedPlayerPosition;

/**
 * Represents an eye of ender throw.
 */
public class EnderEyeThrow implements IEnderEyeThrow {

	private final double x, z, horizontalAngleWithoutCorrection, verticalAngle;
	private final ThrowType throwType;

	private final IStdProfile stdProfile;
	private final int stdProfileNumber;
	private final double std;
	private final double correction;

	public EnderEyeThrow(double x, double z, double horizontalAngle, double verticalAngle, ThrowType throwType, IStdProfile stdProfile) {
		this(x, z, horizontalAngle, verticalAngle, 0, throwType, stdProfile);
	}

	private EnderEyeThrow(double x, double z, double horizontalAngle, double verticalAngle, double correction, ThrowType throwType, IStdProfile stdProfile) {
		this.x = x;
		this.z = z;
		this.throwType = throwType;
		horizontalAngle %= 360.0;
		if (horizontalAngle < -180.0) {
			horizontalAngle += 360.0;
		} else if (horizontalAngle > 180.0) {
			horizontalAngle -= 360.0;
		}
		this.horizontalAngleWithoutCorrection = horizontalAngle;
		this.verticalAngle = verticalAngle;
		this.correction = correction;

		this.stdProfile = stdProfile;
		stdProfileNumber = stdProfile.getInitialProfileNumber(this);
		std = stdProfile.getStd(stdProfileNumber);
	}

	@Override
	public double verticalAngle() {
		return 0;
	}

	@Override
	public IEnderEyeThrow withCorrection(double correction) {
		return new EnderEyeThrow(x, z, horizontalAngleWithoutCorrection, verticalAngle, correction, throwType, stdProfile);
	}

	@Override
	public String toString() {
		return "x=" + x + ", z=" + z + ", alpha=" + horizontalAngleWithoutCorrection;
	}

	@Override
	public boolean isBoatThrow() {
		return throwType == ThrowType.Boat;
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

	@Override
	public int getStdProfileNumber() {
		return stdProfileNumber;
	}

	@Override
	public boolean isMcVersion1_12() {
		return throwType == ThrowType.McVersion1_12;
	}

	@Override
	public double getStd() {
		return std;
	}

	@Override
	public IPlayerPosition getPlayerPosition() {
		return new LimitedPlayerPosition(x, z, horizontalAngle());
	}

}
