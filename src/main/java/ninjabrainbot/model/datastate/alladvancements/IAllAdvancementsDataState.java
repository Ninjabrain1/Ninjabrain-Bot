package ninjabrainbot.model.datastate.alladvancements;

import ninjabrainbot.event.IObservable;
import ninjabrainbot.model.datastate.common.StructurePosition;
import ninjabrainbot.model.domainmodel.IDataComponent;
import ninjabrainbot.model.domainmodel.IDomainModelComponent;

public interface IAllAdvancementsDataState {

	IDomainModelComponent<Boolean> allAdvancementsModeEnabled();

	IDomainModelComponent<StructurePosition> strongholdPosition();

	IDataComponent<StructurePosition> spawnPosition();

	IDataComponent<StructurePosition> outpostPosition();

	IDataComponent<StructurePosition> monumentPosition();

}
