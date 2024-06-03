package ninjabrainbot.model.datastate.endereye;

import ninjabrainbot.model.datastate.common.IOverworldRay;
import ninjabrainbot.model.datastate.common.IPlayerPosition;
import ninjabrainbot.model.datastate.common.LimitedPlayerPosition;
import ninjabrainbot.model.environmentstate.StandardDeviationSettings;

public interface IEnderEyeThrow extends IOverworldRay {

	double verticalAngle();

	double horizontalAngleWithoutCorrection();

	double correction();

	int correctionIncrements();

	double getStandardDeviation(StandardDeviationSettings standardDeviationHandler);

	double getExpectedStandardDeviationForNextEnderEyeThrow(StandardDeviationSettings standardDeviationHandler);

	IEnderEyeThrow withCorrection(double correction, int correctionIncrements);

	IEnderEyeThrow withToggledAltStd();

	EnderEyeThrowType getType();

	default IPlayerPosition getPlayerPosition() {
		return new LimitedPlayerPosition(xInOverworld(), zInOverworld(), horizontalAngle());
	}

}
