package ninjabrainbot.data.calculator.endereye;

import ninjabrainbot.data.calculator.common.IOverworldRay;

public interface IThrow extends IOverworldRay {

	IThrow withCorrection(double correction);

	double alphaWithoutCorrection();

	double correction();

	double getStd();

	int getStdProfileNumber();

	boolean isMcVersion1_12();

	boolean isBoatThrow();

}
