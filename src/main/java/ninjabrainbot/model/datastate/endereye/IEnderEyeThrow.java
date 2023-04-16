package ninjabrainbot.model.datastate.endereye;

import ninjabrainbot.model.datastate.common.IOverworldRay;
import ninjabrainbot.model.datastate.common.IPlayerPosition;
import ninjabrainbot.model.datastate.common.LimitedPlayerPosition;
import ninjabrainbot.model.environmentstate.StandardDeviationSettings;

public interface IEnderEyeThrow extends IOverworldRay {

	double verticalAngle();

	double horizontalAngleWithoutCorrection();

	double correction();

	double getStandardDeviation(StandardDeviationSettings standardDeviationHandler);

	double getExpectedStandardDeviationForNextEnderEyeThrow(StandardDeviationSettings standardDeviationHandler);

	IEnderEyeThrow withCorrection(double correction);

	IEnderEyeThrow withToggledAltStd();

	EnderEyeThrowType getType();

	default IPlayerPosition getPlayerPosition() {
		return new LimitedPlayerPosition(xInOverworld(), zInOverworld(), horizontalAngle());
	}

}
