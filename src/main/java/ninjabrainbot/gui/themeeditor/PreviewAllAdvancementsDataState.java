package ninjabrainbot.gui.themeeditor;

import ninjabrainbot.model.datastate.alladvancements.IAllAdvancementsDataState;
import ninjabrainbot.model.datastate.alladvancements.IAllAdvancementsPosition;
import ninjabrainbot.model.datastate.common.DetachedDomainModel;
import ninjabrainbot.model.datastate.common.StructureInformation;
import ninjabrainbot.model.domainmodel.DataComponent;
import ninjabrainbot.model.domainmodel.IDataComponent;
import ninjabrainbot.model.domainmodel.IDomainModel;
import ninjabrainbot.model.domainmodel.IDomainModelComponent;
import ninjabrainbot.model.domainmodel.InferredComponent;

public class PreviewAllAdvancementsDataState implements IAllAdvancementsDataState {

	IDomainModel domainModel = new DetachedDomainModel();

	@Override
	public IDomainModelComponent<Boolean> allAdvancementsModeEnabled() {
		return new DataComponent<>("", domainModel, false);
	}

	@Override
	public IDataComponent<IAllAdvancementsPosition> spawnPosition() {
		return new DataComponent<>("", domainModel);
	}

	@Override
	public IDataComponent<IAllAdvancementsPosition> outpostPosition() {
		return new DataComponent<>("", domainModel);
	}

	@Override
	public IDataComponent<IAllAdvancementsPosition> monumentPosition() {
		return new DataComponent<>("", domainModel);
	}

	@Override
	public IDataComponent<IAllAdvancementsPosition> deepDarkPosition() {
		return new DataComponent<>("", domainModel);
	}

	@Override
	public IDataComponent<IAllAdvancementsPosition> cityQueryPosition() {
		return new DataComponent<>("", domainModel);
	}

	@Override
	public IDataComponent<IAllAdvancementsPosition> shulkerTransportPosition() {
		return new DataComponent<>("", domainModel);
	}

	@Override
	public IDataComponent<IAllAdvancementsPosition> generalLocationPosition() {
		return new DataComponent<>("", domainModel);
	}

	@Override
	public IDomainModelComponent<StructureInformation> strongholdInformation() {
		return new InferredComponent<>(null);
	}

	@Override
	public IDomainModelComponent<StructureInformation> spawnInformation() {
		return new InferredComponent<>(null);
	}

	@Override
	public IDomainModelComponent<StructureInformation> outpostInformation() {
		return new InferredComponent<>(null);
	}

	@Override
	public IDomainModelComponent<StructureInformation> monumentInformation() {
		return new InferredComponent<>(null);
	}

	@Override
	public IDomainModelComponent<StructureInformation> deepDarkInformation() {
		return new InferredComponent<>(null);
	}

	@Override
	public IDomainModelComponent<StructureInformation> cityQueryInformation() {
		return new InferredComponent<>(null);
	}

	@Override
	public IDomainModelComponent<StructureInformation> shulkerTransportInformation() {
		return new InferredComponent<>(null);
	}

	@Override
	public IDomainModelComponent<StructureInformation> generalLocationInformation() {
		return new InferredComponent<>(null);
	}
}
