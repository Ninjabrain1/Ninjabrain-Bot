package ninjabrainbot.model.datastate.alladvancements;

import ninjabrainbot.event.DisposeHandler;
import ninjabrainbot.event.IDisposable;
import ninjabrainbot.model.datastate.common.StructurePosition;
import ninjabrainbot.model.datastate.stronghold.ChunkPrediction;
import ninjabrainbot.model.domainmodel.DataComponent;
import ninjabrainbot.model.domainmodel.IDataComponent;
import ninjabrainbot.model.domainmodel.IDomainModel;
import ninjabrainbot.model.domainmodel.IDomainModelComponent;
import ninjabrainbot.model.domainmodel.InferredComponent;
import ninjabrainbot.model.environmentstate.IEnvironmentState;

public class AllAdvancementsDataState implements IAllAdvancementsDataState, IDisposable {

	private final IEnvironmentState environmentState;

	private final InferredComponent<Boolean> allAdvancementsModeEnabled;
	private final InferredComponent<StructurePosition> strongholdPosition;
	private final DataComponent<StructurePosition> spawnPosition;
	private final DataComponent<StructurePosition> outpostPosition;
	private final DataComponent<StructurePosition> monumentPosition;
	private final DataComponent<StructurePosition> deepDarkPosition;
	private final DataComponent<StructurePosition> cityQueryPosition;
	private final DataComponent<StructurePosition> shulkerTransportPosition;
	private final DataComponent<StructurePosition> generalLocationPosition;


	private final DisposeHandler disposeHandler = new DisposeHandler();

	public AllAdvancementsDataState(IDomainModelComponent<ChunkPrediction> currentStrongholdPrediction, IDomainModel domainModel, IEnvironmentState environmentState) {
		this.environmentState = environmentState;
		allAdvancementsModeEnabled = new InferredComponent<>(domainModel, false);
		strongholdPosition = new InferredComponent<>(domainModel);
		spawnPosition = new DataComponent<>(domainModel);
		outpostPosition = new DataComponent<>(domainModel);
		monumentPosition = new DataComponent<>(domainModel);
		deepDarkPosition = new DataComponent<>(domainModel);
		cityQueryPosition = new DataComponent<>(domainModel);
		shulkerTransportPosition = new DataComponent<>(domainModel);
		generalLocationPosition = new DataComponent<>(domainModel);
		disposeHandler.add(environmentState.allAdvancementsModeEnabled().subscribeInternal(this::updateAllAdvancementsMode));
		disposeHandler.add(environmentState.hasEnteredEnd().subscribeInternal(this::updateAllAdvancementsMode));
		disposeHandler.add(currentStrongholdPrediction.subscribeInternal(strongholdPosition::set));
	}

	private void updateAllAdvancementsMode() {
		allAdvancementsModeEnabled.set(environmentState.allAdvancementsModeEnabled().get() && environmentState.hasEnteredEnd().get());
	}

	@Override
	public IDomainModelComponent<Boolean> allAdvancementsModeEnabled() {
		return allAdvancementsModeEnabled;
	}

	@Override
	public IDomainModelComponent<StructurePosition> strongholdPosition() {
		return strongholdPosition;
	}

	@Override
	public IDataComponent<StructurePosition> spawnPosition() {
		return spawnPosition;
	}

	@Override
	public IDataComponent<StructurePosition> outpostPosition() {
		return outpostPosition;
	}

	@Override
	public IDataComponent<StructurePosition> monumentPosition() {
		return monumentPosition;
	}

	@Override
	public IDataComponent<StructurePosition> deepDarkPosition() {
		return deepDarkPosition;
	}

	@Override
	public IDataComponent<StructurePosition> cityQueryPosition() {
		return cityQueryPosition;
	}

	@Override
	public IDataComponent<StructurePosition> shulkerTransportPosition() {
		return shulkerTransportPosition;
	}

	@Override
	public IDataComponent<StructurePosition> generalLocationPosition() {
		return generalLocationPosition;
	}

	@Override
	public void dispose() {
		disposeHandler.dispose();
	}
}
