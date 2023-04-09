package ninjabrainbot.data.calculator.common;

public interface IPlayerPosition extends IOverworldRay {

	double xInPlayerDimension();

	double zInPlayerDimension();

	boolean isInOverworld();

	boolean isInNether();

}
