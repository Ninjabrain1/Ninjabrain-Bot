package ninjabrainbot.model.datastate.alladvancements;

import ninjabrainbot.model.datastate.common.StructurePosition;
import ninjabrainbot.model.domainmodel.IDataComponent;
import ninjabrainbot.model.domainmodel.IDomainModelComponent;

public interface IAllAdvancementsDataState {

	IDomainModelComponent<Boolean> allAdvancementsModeEnabled();

	IDomainModelComponent<StructurePosition> strongholdPosition();

	IDataComponent<StructurePosition> spawnPosition();

	IDataComponent<StructurePosition> outpostPosition();

	IDataComponent<StructurePosition> monumentPosition();

	IDataComponent<StructurePosition> deepDarkPosition();

	IDataComponent<StructurePosition> cityQueryPosition();

	IDataComponent<StructurePosition> shulkerTransportPosition();

	IDataComponent<StructurePosition> generalLocationPosition();

}
