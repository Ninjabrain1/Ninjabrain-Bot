package ninjabrainbot.gui.themeeditor;

import ninjabrainbot.model.datastate.alladvancements.IAllAdvancementsDataState;
import ninjabrainbot.model.datastate.common.IOverworldPosition;
import ninjabrainbot.model.datastate.common.StructureInformation;
import ninjabrainbot.model.domainmodel.DataComponent;
import ninjabrainbot.model.domainmodel.IDataComponent;
import ninjabrainbot.model.domainmodel.IDomainModelComponent;

public class PreviewAllAdvancementsDataState implements IAllAdvancementsDataState {

	@Override
	public IDomainModelComponent<Boolean> allAdvancementsModeEnabled() {
		return new DataComponent<>(null, false);
	}

	@Override
	public IDataComponent<IOverworldPosition> spawnPosition() {
		return new DataComponent<>(null);
	}

	@Override
	public IDataComponent<IOverworldPosition> outpostPosition() {
		return new DataComponent<>(null);
	}

	@Override
	public IDataComponent<IOverworldPosition> monumentPosition() {
		return new DataComponent<>(null);
	}

	@Override
	public IDataComponent<IOverworldPosition> deepDarkPosition() {
		return new DataComponent<>(null);
	}

	@Override
	public IDataComponent<IOverworldPosition> cityQueryPosition() {
		return new DataComponent<>(null);
	}

	@Override
	public IDataComponent<IOverworldPosition> shulkerTransportPosition() {
		return new DataComponent<>(null);
	}

	@Override
	public IDataComponent<IOverworldPosition> generalLocationPosition() {
		return new DataComponent<>(null);
	}

	@Override
	public IDomainModelComponent<StructureInformation> strongholdInformation() {
		return new DataComponent<>(null);
	}

	@Override
	public IDomainModelComponent<StructureInformation> spawnInformation() {
		return new DataComponent<>(null);
	}

	@Override
	public IDomainModelComponent<StructureInformation> outpostInformation() {
		return new DataComponent<>(null);
	}

	@Override
	public IDomainModelComponent<StructureInformation> monumentInformation() {
		return new DataComponent<>(null);
	}

	@Override
	public IDomainModelComponent<StructureInformation> deepDarkInformation() {
		return new DataComponent<>(null);
	}

	@Override
	public IDomainModelComponent<StructureInformation> cityQueryInformation() {
		return new DataComponent<>(null);
	}

	@Override
	public IDomainModelComponent<StructureInformation> shulkerTransportInformation() {
		return new DataComponent<>(null);
	}

	@Override
	public IDomainModelComponent<StructureInformation> generalLocationInformation() {
		return new DataComponent<>(null);
	}
}
