package ninjabrainbot.data.calculator.endereye;

import ninjabrainbot.data.calculator.common.IOverworldRay;
import ninjabrainbot.data.calculator.common.IPlayerPosition;
import ninjabrainbot.data.calculator.common.LimitedPlayerPosition;

public interface IEnderEyeThrow extends IOverworldRay {

	double verticalAngle();

	double horizontalAngleWithoutCorrection();

	double correction();

	double getStd();

	IEnderEyeThrow withCorrection(double correction);

	IEnderEyeThrow withToggledAltStd();

	EnderEyeThrowType getType();

	default IPlayerPosition getPlayerPosition(){
		return new LimitedPlayerPosition(xInOverworld(), zInOverworld(), horizontalAngle());
	}

}
