package ninjabrainbot.gui.themeeditor;

import ninjabrainbot.model.datastate.alladvancements.IAllAdvancementsDataState;
import ninjabrainbot.model.datastate.common.StructurePosition;
import ninjabrainbot.model.domainmodel.DataComponent;
import ninjabrainbot.model.domainmodel.IDataComponent;
import ninjabrainbot.model.domainmodel.IDomainModelComponent;

public class PreviewAllAdvancementsDataState implements IAllAdvancementsDataState {

	@Override
	public IDomainModelComponent<Boolean> allAdvancementsModeEnabled() {
		return new DataComponent<>(null, false);
	}

	@Override
	public IDomainModelComponent<StructurePosition> strongholdPosition() {
		return new DataComponent<>(null);
	}

	@Override
	public IDataComponent<StructurePosition> spawnPosition() {
		return new DataComponent<>(null);
	}

	@Override
	public IDataComponent<StructurePosition> outpostPosition() {
		return new DataComponent<>(null);
	}

	@Override
	public IDataComponent<StructurePosition> monumentPosition() {
		return new DataComponent<>(null);
	}
}
