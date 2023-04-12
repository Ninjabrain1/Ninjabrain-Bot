package ninjabrainbot.model.datastate.alladvancements;

import ninjabrainbot.event.IObservable;
import ninjabrainbot.model.datastate.common.StructurePosition;
import ninjabrainbot.model.domainmodel.IDataComponent;

public interface IAllAdvancementsDataState {

	IObservable<Boolean> allAdvancementsModeEnabled();

	IObservable<StructurePosition> strongholdPosition();

	IDataComponent<StructurePosition> spawnPosition();

	IDataComponent<StructurePosition> outpostPosition();

	IDataComponent<StructurePosition> monumentPosition();

}
