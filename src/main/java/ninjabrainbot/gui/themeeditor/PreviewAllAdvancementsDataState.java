package ninjabrainbot.gui.themeeditor;

import ninjabrainbot.model.datastate.alladvancements.AllAdvancementsStructureType;
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
	public IDataComponent<Boolean> hasEnteredEnd() {
		return new DataComponent<>("", domainModel);
	}

	@Override
	public IDataComponent<IAllAdvancementsPosition> getAllAdvancementsPosition(AllAdvancementsStructureType allAdvancementsStructureType) {
		return new DataComponent<>("", domainModel);
	}

	@Override
	public IDomainModelComponent<StructureInformation> getStructureInformation(AllAdvancementsStructureType allAdvancementsStructureType) {
		return new InferredComponent<>(null);
	}
}
