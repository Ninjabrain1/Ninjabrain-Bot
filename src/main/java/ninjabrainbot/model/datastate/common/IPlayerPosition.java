package ninjabrainbot.model.datastate.common;

import java.io.Serializable;

public interface IPlayerPosition extends IOverworldRay, Serializable {

	double xInPlayerDimension();

	double zInPlayerDimension();

	boolean isInOverworld();

	boolean isInNether();

	boolean isInEnd();

}
