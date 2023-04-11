package ninjabrainbot.model.datastate.alladvancements;

import ninjabrainbot.model.datastate.common.StructurePosition;
import ninjabrainbot.model.domainmodel.IDataComponent;

public interface IAllAdvancementsDataState {

	IDataComponent<Boolean> allAdvancementsModeEnabled();

	IDataComponent<StructurePosition> strongholdPosition();

	IDataComponent<StructurePosition> spawnPosition();

	IDataComponent<StructurePosition> outpostPosition();

	IDataComponent<StructurePosition> monumentPosition();

}
