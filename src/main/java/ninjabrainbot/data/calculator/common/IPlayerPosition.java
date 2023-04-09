package ninjabrainbot.data.calculator.common;

public interface IPlayerPosition extends IOverworldRay {

	double xInPlayerDimension();

	double zInPlayerDimension();

	double yInPlayerDimension();

	double beta();

	boolean lookingBelowHorizon();

	boolean isInOverworld();

	boolean isNether();

}
