package ninjabrainbot.model.datastate.endereye;

import ninjabrainbot.model.datastate.common.IDetailedPlayerPosition;
import ninjabrainbot.model.datastate.common.ILimitedPlayerPosition;

public interface IEnderEyeThrowFactory {

	IEnderEyeThrow createEnderEyeThrowFromDetailedPlayerPosition(IDetailedPlayerPosition detailedPlayerPosition);

	IEnderEyeThrow createEnderEyeThrowFromLimitedPlayerPosition(ILimitedPlayerPosition playerPosition);

}
