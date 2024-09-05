package ninjabrainbot.model.datastate.alladvancements;

import ninjabrainbot.model.datastate.common.IOverworldPosition;
import ninjabrainbot.model.datastate.common.StructureInformation;
import ninjabrainbot.model.domainmodel.IDataComponent;
import ninjabrainbot.model.domainmodel.IDomainModelComponent;

public interface IAllAdvancementsDataState {

	IDomainModelComponent<Boolean> allAdvancementsModeEnabled();

	IDataComponent<IOverworldPosition> spawnPosition();

	IDataComponent<IOverworldPosition> outpostPosition();

	IDataComponent<IOverworldPosition> monumentPosition();

	IDataComponent<IOverworldPosition> deepDarkPosition();

	IDataComponent<IOverworldPosition> cityQueryPosition();

	IDataComponent<IOverworldPosition> shulkerTransportPosition();

	IDataComponent<IOverworldPosition> generalLocationPosition();

	IDomainModelComponent<StructureInformation> strongholdInformation();

	IDomainModelComponent<StructureInformation> spawnInformation();

	IDomainModelComponent<StructureInformation> outpostInformation();

	IDomainModelComponent<StructureInformation> monumentInformation();

	IDomainModelComponent<StructureInformation> deepDarkInformation();

	IDomainModelComponent<StructureInformation> cityQueryInformation();

	IDomainModelComponent<StructureInformation> shulkerTransportInformation();

	IDomainModelComponent<StructureInformation> generalLocationInformation();

}
