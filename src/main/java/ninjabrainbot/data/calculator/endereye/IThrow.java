package ninjabrainbot.data.calculator.endereye;

import ninjabrainbot.data.calculator.statistics.IOverworldRay;

public interface IThrow extends IOverworldRay {

	IThrow withCorrection(double correction);

	double xInPlayerDimension();

	double zInPlayerDimension();

	double yInPlayerDimension();

	double getStd();

	double rawAlpha();

	double alpha_0();

	double beta();

	double correction();

	boolean lookingBelowHorizon();

	boolean isNether();

	int getStdProfileNumber();

	boolean isMcVersion1_12();

	boolean isBoatThrow();

}
