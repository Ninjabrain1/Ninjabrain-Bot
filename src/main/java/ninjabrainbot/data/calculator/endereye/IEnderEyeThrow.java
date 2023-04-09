package ninjabrainbot.data.calculator.endereye;

import ninjabrainbot.data.calculator.common.IOverworldRay;
import ninjabrainbot.data.calculator.common.IPlayerPosition;

public interface IEnderEyeThrow extends IOverworldRay {

	double verticalAngle();

	IEnderEyeThrow withCorrection(double correction);

	double horizontalAngleWithoutCorrection();

	double correction();

	double getStd();

	int getStdProfileNumber();

	boolean isMcVersion1_12();

	boolean isBoatThrow();

	IPlayerPosition getPlayerPosition();

}
