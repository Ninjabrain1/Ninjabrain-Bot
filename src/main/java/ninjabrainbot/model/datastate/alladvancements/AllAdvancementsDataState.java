package ninjabrainbot.model.datastate.alladvancements;

import java.util.HashMap;

import ninjabrainbot.event.DisposeHandler;
import ninjabrainbot.event.IDisposable;
import ninjabrainbot.event.IObservable;
import ninjabrainbot.event.Subscription;
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

	private final DataComponent<Boolean> hasEnteredEnd;
	private final HashMap<AllAdvancementsStructureType, DataComponent<IAllAdvancementsPosition>> allAdvancementsPositionDataComponents;

	private final InferredComponent<Boolean> allAdvancementsModeEnabled;
	private final HashMap<AllAdvancementsStructureType, InferredComponent<StructureInformation>> structureInformationInferredComponents;

	private final DisposeHandler disposeHandler = new DisposeHandler();

	public AllAdvancementsDataState(IDomainModelComponent<ChunkPrediction> currentStrongholdPrediction, IObservable<IPlayerPosition> playerPosition, IDomainModel domainModel, IEnvironmentState environmentState) {
		this.playerPosition = playerPosition;
		this.environmentState = environmentState;

		hasEnteredEnd = new DataComponent<>("aa_toggle", domainModel, false);
		allAdvancementsModeEnabled = new InferredComponent<>(domainModel, false);

		allAdvancementsPositionDataComponents = new HashMap<>();
		structureInformationInferredComponents = new HashMap<>();
		for (AllAdvancementsStructureType allAdvancementsStructureType : AllAdvancementsStructureType.values()){
			if (allAdvancementsStructureType != AllAdvancementsStructureType.Stronghold) {
				IDataComponent<IAllAdvancementsPosition> dataComponent = addAllAdvancementsPositionDataComponent(allAdvancementsStructureType, domainModel);
				InferredComponent<StructureInformation> inferredComponent = addStructureInformationInferredComponent(allAdvancementsStructureType, domainModel);
				disposeHandler.add(createStructureInformationSubscription(dataComponent, inferredComponent));
			} else {
				InferredComponent<StructureInformation> strongholdInformation = addStructureInformationInferredComponent(allAdvancementsStructureType, domainModel);
				disposeHandler.add(currentStrongholdPrediction.subscribeInternal(strongholdInformation::set));
			}
		}

		disposeHandler.add(environmentState.allAdvancementsModeEnabled().subscribeInternal(this::updateAllAdvancementsMode));
		disposeHandler.add(hasEnteredEnd.subscribeInternal(this::updateAllAdvancementsMode));
	}

	private DataComponent<IAllAdvancementsPosition> addAllAdvancementsPositionDataComponent(AllAdvancementsStructureType allAdvancementsStructureType, IDomainModel domainModel) {
		DataComponent<IAllAdvancementsPosition> dataComponent = new DataComponent<>("aa_" + allAdvancementsStructureType.name(), domainModel);
		allAdvancementsPositionDataComponents.put(allAdvancementsStructureType, dataComponent);
		return dataComponent;
	}

	private InferredComponent<StructureInformation> addStructureInformationInferredComponent(AllAdvancementsStructureType allAdvancementsStructureType, IDomainModel domainModel) {
		InferredComponent<StructureInformation> inferredComponent = new InferredComponent<>(domainModel);
		structureInformationInferredComponents.put(allAdvancementsStructureType, inferredComponent);
		return inferredComponent;
	}

	private Subscription createStructureInformationSubscription(IDataComponent<IAllAdvancementsPosition> allAdvancementsPosition, InferredComponent<StructureInformation> structureInformation) {
		return allAdvancementsPosition.subscribeInternal(overworldPosition ->
				structureInformation.set(overworldPosition == null
						? null
						: new StructureInformation(overworldPosition, playerPosition)
				)
		);
	}

	private void updateAllAdvancementsMode() {
		allAdvancementsModeEnabled.set(environmentState.allAdvancementsModeEnabled().get() && hasEnteredEnd.get());
	}

	@Override
	public IDomainModelComponent<Boolean> allAdvancementsModeEnabled() {
		return allAdvancementsModeEnabled;
	}

	@Override
	public IDataComponent<Boolean> hasEnteredEnd() {
		return hasEnteredEnd;
	}

	@Override
	public IDataComponent<IAllAdvancementsPosition> getAllAdvancementsPosition(AllAdvancementsStructureType allAdvancementsStructureType) {
		if (allAdvancementsStructureType == AllAdvancementsStructureType.Stronghold)
			throw new IllegalArgumentException("There is no IDataComponent for AllAdvancementsStructureType.Stronghold");
		return allAdvancementsPositionDataComponents.get(allAdvancementsStructureType);
	}

	@Override
	public IDomainModelComponent<StructureInformation> getStructureInformation(AllAdvancementsStructureType allAdvancementsStructureType) {
		return structureInformationInferredComponents.get(allAdvancementsStructureType);
	}

	@Override
	public void dispose() {
		disposeHandler.dispose();
	}
}
