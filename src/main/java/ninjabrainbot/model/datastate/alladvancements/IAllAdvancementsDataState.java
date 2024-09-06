package ninjabrainbot.model.datastate.alladvancements;

import ninjabrainbot.model.datastate.common.StructureInformation;
import ninjabrainbot.model.domainmodel.IDataComponent;
import ninjabrainbot.model.domainmodel.IDomainModelComponent;

public interface IAllAdvancementsDataState {

	IDomainModelComponent<Boolean> allAdvancementsModeEnabled();

	IDataComponent<IAllAdvancementsPosition> spawnPosition();

	IDataComponent<IAllAdvancementsPosition> outpostPosition();

	IDataComponent<IAllAdvancementsPosition> monumentPosition();

	IDataComponent<IAllAdvancementsPosition> deepDarkPosition();

	IDataComponent<IAllAdvancementsPosition> cityQueryPosition();

	IDataComponent<IAllAdvancementsPosition> shulkerTransportPosition();

	IDataComponent<IAllAdvancementsPosition> generalLocationPosition();

	IDomainModelComponent<StructureInformation> strongholdInformation();

	IDomainModelComponent<StructureInformation> spawnInformation();

	IDomainModelComponent<StructureInformation> outpostInformation();

	IDomainModelComponent<StructureInformation> monumentInformation();

	IDomainModelComponent<StructureInformation> deepDarkInformation();

	IDomainModelComponent<StructureInformation> cityQueryInformation();

	IDomainModelComponent<StructureInformation> shulkerTransportInformation();

	IDomainModelComponent<StructureInformation> generalLocationInformation();

}
