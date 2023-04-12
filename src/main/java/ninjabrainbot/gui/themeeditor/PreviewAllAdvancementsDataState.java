package ninjabrainbot.gui.themeeditor;

import ninjabrainbot.event.IObservable;
import ninjabrainbot.event.ObservableField;
import ninjabrainbot.model.datastate.alladvancements.IAllAdvancementsDataState;
import ninjabrainbot.model.datastate.common.StructurePosition;
import ninjabrainbot.model.domainmodel.DataComponent;
import ninjabrainbot.model.domainmodel.IDataComponent;

public class PreviewAllAdvancementsDataState implements IAllAdvancementsDataState {

	@Override
	public IObservable<Boolean> allAdvancementsModeEnabled() {
		return new ObservableField<>(false);
	}

	@Override
	public IObservable<StructurePosition> strongholdPosition() {
		return new ObservableField<>();
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
