package ninjabrainbot.model.datastate.endereye;

import ninjabrainbot.model.datastate.common.IDetailedPlayerPosition;
import ninjabrainbot.model.datastate.common.IPlayerPosition;

public interface IEnderEyeThrowFactory {

	IEnderEyeThrow createEnderEyeThrowFromDetailedPlayerPosition(IDetailedPlayerPosition detailedPlayerPosition);

	IEnderEyeThrow createEnderEyeThrowFromLimitedPlayerPosition(IPlayerPosition playerPosition);

}
