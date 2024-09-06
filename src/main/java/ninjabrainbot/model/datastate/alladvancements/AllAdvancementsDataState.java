package ninjabrainbot.model.datastate.alladvancements;

import ninjabrainbot.event.DisposeHandler;
import ninjabrainbot.event.IDisposable;
import ninjabrainbot.event.IObservable;
import ninjabrainbot.model.datastate.common.IOverworldPosition;
import ninjabrainbot.model.datastate.common.IPlayerPosition;
import ninjabrainbot.model.datastate.common.StructureInformation;
import ninjabrainbot.model.datastate.stronghold.ChunkPrediction;
import ninjabrainbot.model.domainmodel.DataComponent;
import ninjabrainbot.model.domainmodel.IDataComponent;
import ninjabrainbot.model.domainmodel.IDomainModel;
import ninjabrainbot.model.domainmodel.IDomainModelComponent;
import ninjabrainbot.model.domainmodel.InferredComponent;
import ninjabrainbot.model.environmentstate.IEnvironmentState;

public class AllAdvancementsDataState implements IAllAdvancementsDataState, IDisposable {

	private final IObservable<IPlayerPosition> playerPosition;
	private final IEnvironmentState environmentState;

	private final DataComponent<IAllAdvancementsPosition> spawnPosition;
	private final DataComponent<IAllAdvancementsPosition> outpostPosition;
	private final DataComponent<IAllAdvancementsPosition> monumentPosition;
	private final DataComponent<IAllAdvancementsPosition> deepDarkPosition;
	private final DataComponent<IAllAdvancementsPosition> cityQueryPosition;
	private final DataComponent<IAllAdvancementsPosition> shulkerTransportPosition;
	private final DataComponent<IAllAdvancementsPosition> generalLocationPosition;

	private final InferredComponent<Boolean> allAdvancementsModeEnabled;
	private final InferredComponent<StructureInformation> strongholdInformation;
	private final InferredComponent<StructureInformation> spawnInformation;
	private final InferredComponent<StructureInformation> outpostInformation;
	private final InferredComponent<StructureInformation> monumentInformation;
	private final InferredComponent<StructureInformation> deepDarkInformation;
	private final InferredComponent<StructureInformation> cityQueryInformation;
	private final InferredComponent<StructureInformation> shulkerTransportInformation;
	private final InferredComponent<StructureInformation> generalLocationInformation;

	private final DisposeHandler disposeHandler = new DisposeHandler();

	public AllAdvancementsDataState(IDomainModelComponent<ChunkPrediction> currentStrongholdPrediction, IObservable<IPlayerPosition> playerPosition, IDomainModel domainModel, IEnvironmentState environmentState) {
		this.playerPosition = playerPosition;
		this.environmentState = environmentState;

		spawnPosition = new DataComponent<>(domainModel);
		outpostPosition = new DataComponent<>(domainModel);
		monumentPosition = new DataComponent<>(domainModel);
		deepDarkPosition = new DataComponent<>(domainModel);
		cityQueryPosition = new DataComponent<>(domainModel);
		shulkerTransportPosition = new DataComponent<>(domainModel);
		generalLocationPosition = new DataComponent<>(domainModel);

		allAdvancementsModeEnabled = new InferredComponent<>(domainModel, false);
		strongholdInformation = new InferredComponent<>(domainModel);
		spawnInformation = new InferredComponent<>(domainModel);
		outpostInformation = new InferredComponent<>(domainModel);
		monumentInformation = new InferredComponent<>(domainModel);
		deepDarkInformation = new InferredComponent<>(domainModel);
		cityQueryInformation = new InferredComponent<>(domainModel);
		shulkerTransportInformation = new InferredComponent<>(domainModel);
		generalLocationInformation = new InferredComponent<>(domainModel);

		disposeHandler.add(environmentState.allAdvancementsModeEnabled().subscribeInternal(this::updateAllAdvancementsMode));
		disposeHandler.add(environmentState.hasEnteredEnd().subscribeInternal(this::updateAllAdvancementsMode));
		disposeHandler.add(currentStrongholdPrediction.subscribeInternal(strongholdInformation::set));
		disposeHandler.add(spawnPosition.subscribeInternal(overworldPosition -> updateStructureInformationComponent(spawnInformation, overworldPosition)));
		disposeHandler.add(outpostPosition.subscribeInternal(overworldPosition -> updateStructureInformationComponent(outpostInformation, overworldPosition)));
		disposeHandler.add(monumentPosition.subscribeInternal(overworldPosition -> updateStructureInformationComponent(monumentInformation, overworldPosition)));
		disposeHandler.add(deepDarkPosition.subscribeInternal(overworldPosition -> updateStructureInformationComponent(deepDarkInformation, overworldPosition)));
		disposeHandler.add(cityQueryPosition.subscribeInternal(overworldPosition -> updateStructureInformationComponent(cityQueryInformation, overworldPosition)));
		disposeHandler.add(shulkerTransportPosition.subscribeInternal(overworldPosition -> updateStructureInformationComponent(shulkerTransportInformation, overworldPosition)));
		disposeHandler.add(generalLocationPosition.subscribeInternal(overworldPosition -> updateStructureInformationComponent(generalLocationInformation, overworldPosition)));
	}

	private void updateAllAdvancementsMode() {
		allAdvancementsModeEnabled.set(environmentState.allAdvancementsModeEnabled().get() && environmentState.hasEnteredEnd().get());
	}

	private void updateStructureInformationComponent(InferredComponent<StructureInformation> structureInformationComponent, IOverworldPosition overworldPosition) {
		structureInformationComponent.set(overworldPosition == null
				? null
				: new StructureInformation(overworldPosition, playerPosition));
	}

	@Override
	public IDomainModelComponent<Boolean> allAdvancementsModeEnabled() {
		return allAdvancementsModeEnabled;
	}

	@Override
	public IDataComponent<IAllAdvancementsPosition> spawnPosition() {
		return spawnPosition;
	}

	@Override
	public IDataComponent<IAllAdvancementsPosition> outpostPosition() {
		return outpostPosition;
	}

	@Override
	public IDataComponent<IAllAdvancementsPosition> monumentPosition() {
		return monumentPosition;
	}

	@Override
	public IDataComponent<IAllAdvancementsPosition> deepDarkPosition() {
		return deepDarkPosition;
	}

	@Override
	public IDataComponent<IAllAdvancementsPosition> cityQueryPosition() {
		return cityQueryPosition;
	}

	@Override
	public IDataComponent<IAllAdvancementsPosition> shulkerTransportPosition() {
		return shulkerTransportPosition;
	}

	@Override
	public IDataComponent<IAllAdvancementsPosition> generalLocationPosition() {
		return generalLocationPosition;
	}

	@Override
	public IDomainModelComponent<StructureInformation> strongholdInformation() {
		return strongholdInformation;
	}

	@Override
	public IDomainModelComponent<StructureInformation> spawnInformation() {
		return spawnInformation;
	}

	@Override
	public IDomainModelComponent<StructureInformation> outpostInformation() {
		return outpostInformation;
	}

	@Override
	public IDomainModelComponent<StructureInformation> monumentInformation() {
		return monumentInformation;
	}

	@Override
	public IDomainModelComponent<StructureInformation> deepDarkInformation() {
		return deepDarkInformation;
	}

	@Override
	public IDomainModelComponent<StructureInformation> cityQueryInformation() {
		return cityQueryInformation;
	}

	@Override
	public IDomainModelComponent<StructureInformation> shulkerTransportInformation() {
		return shulkerTransportInformation;
	}

	@Override
	public IDomainModelComponent<StructureInformation> generalLocationInformation() {
		return generalLocationInformation;
	}

	@Override
	public void dispose() {
		disposeHandler.dispose();
	}
}
