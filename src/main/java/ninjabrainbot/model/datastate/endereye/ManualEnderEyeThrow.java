package ninjabrainbot.model.datastate.endereye;

import ninjabrainbot.model.datastate.common.ILimitedPlayerPosition;
import ninjabrainbot.model.environmentstate.StandardDeviationSettings;

public class ManualEnderEyeThrow extends EnderEyeThrow {

	public ManualEnderEyeThrow(ILimitedPlayerPosition playerPosition, double crosshairCorrection) {
		this(playerPosition.xInOverworld(), playerPosition.zInOverworld(), getCorrectedHorizontalAngle(playerPosition.horizontalAngle(), crosshairCorrection), -31.6, 0, 0);
	}

	private ManualEnderEyeThrow(double x, double z, double horizontalAngle, double verticalAngle, double correction, int correctionIncrements) {
		super(x, z, horizontalAngle, verticalAngle, correction, correctionIncrements);
	}

	@Override
	public IEnderEyeThrow withCorrection(double correction, int correctionIncrements) {
		return new ManualEnderEyeThrow(x, z, horizontalAngleWithoutCorrection, verticalAngle, correction, correctionIncrements);
	}

	@Override
	public IEnderEyeThrow withToggledAltStd() {
		return this;
	}

	@Override
	public double getStandardDeviation(StandardDeviationSettings standardDeviationSettings) {
		return standardDeviationSettings.manualStd;
	}

	@Override
	public double getExpectedStandardDeviationForNextEnderEyeThrow(StandardDeviationSettings standardDeviationSettings) {
		return standardDeviationSettings.manualStd;
	}

	@Override
	public EnderEyeThrowType getType() {
		return EnderEyeThrowType.MANUAL;
	}

}
