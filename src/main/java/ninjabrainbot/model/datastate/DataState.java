package ninjabrainbot.model.datastate;

import ninjabrainbot.event.DisposeHandler;
import ninjabrainbot.event.IDisposable;
import ninjabrainbot.event.IObservable;
import ninjabrainbot.model.datastate.alladvancements.AllAdvancementsDataState;
import ninjabrainbot.model.datastate.alladvancements.IAllAdvancementsDataState;
import ninjabrainbot.model.datastate.blind.BlindResult;
import ninjabrainbot.model.datastate.calculator.CalculatorManager;
import ninjabrainbot.model.datastate.calculator.ICalculatorResult;
import ninjabrainbot.model.datastate.common.IPlayerPosition;
import ninjabrainbot.model.datastate.common.ResultType;
import ninjabrainbot.model.datastate.common.ResultTypeProvider;
import ninjabrainbot.model.datastate.divine.DivineContext;
import ninjabrainbot.model.datastate.divine.DivineResult;
import ninjabrainbot.model.datastate.divine.IDivineContext;
import ninjabrainbot.model.datastate.endereye.IEnderEyeThrow;
import ninjabrainbot.model.datastate.highprecision.BoatDataState;
import ninjabrainbot.model.datastate.highprecision.IBoatDataState;
import ninjabrainbot.model.datastate.stronghold.ChunkPrediction;
import ninjabrainbot.model.domainmodel.DataComponent;
import ninjabrainbot.model.domainmodel.IDataComponent;
import ninjabrainbot.model.domainmodel.IDomainModel;
import ninjabrainbot.model.domainmodel.IDomainModelComponent;
import ninjabrainbot.model.domainmodel.IListComponent;
import ninjabrainbot.model.domainmodel.ListComponent;
import ninjabrainbot.model.environmentstate.IEnvironmentState;

public class DataState implements IDataState, IDisposable {

	final AllAdvancementsDataState allAdvancementsDataState;
	final BoatDataState boatDataState;

	private final DataComponent<Boolean> locked;

	private final DivineContext divineContext;
	private final ListComponent<IEnderEyeThrow> throwSet;
	private final DataComponent<IPlayerPosition> playerPosition;

	private final CalculatorManager calculatorManager;
	private final ResultTypeProvider resultTypeProvider;

	private final DisposeHandler disposeHandler = new DisposeHandler();

	public DataState(IDomainModel domainModel, IEnvironmentState environmentState) {
		divineContext = disposeHandler.add(new DivineContext(domainModel));
		throwSet = new ListComponent<>(domainModel, 10);
		playerPosition = new DataComponent<>(domainModel);
		locked = new DataComponent<>(domainModel, false);

		calculatorManager = disposeHandler.add(new CalculatorManager(domainModel, environmentState, throwSet, playerPosition, divineContext));
		allAdvancementsDataState = disposeHandler.add(new AllAdvancementsDataState(calculatorManager.topPrediction(), domainModel, environmentState));
		boatDataState = new BoatDataState(domainModel);

		resultTypeProvider = disposeHandler.add(new ResultTypeProvider(this, domainModel));
	}

	@Override
	public IAllAdvancementsDataState allAdvancementsDataState() {
		return allAdvancementsDataState;
	}

	@Override
	public IDivineContext getDivineContext() {
		return divineContext;
	}

	@Override
	public IListComponent<IEnderEyeThrow> getThrowList() {
		return throwSet;
	}

	@Override
	public IDataComponent<IPlayerPosition> playerPosition() {
		return playerPosition;
	}

	@Override
	public IDataComponent<Boolean> locked() {
		return locked;
	}

	@Override
	public IDomainModelComponent<ICalculatorResult> calculatorResult() {
		return calculatorManager.calculatorResult();
	}

	@Override
	public IDomainModelComponent<ChunkPrediction> topPrediction() {
		return calculatorManager.topPrediction();
	}

	@Override
	public IDomainModelComponent<BlindResult> blindResult() {
		return calculatorManager.blindResult();
	}

	@Override
	public IDomainModelComponent<DivineResult> divineResult() {
		return calculatorManager.divineResult();
	}

	@Override
	public IDomainModelComponent<ResultType> resultType() {
		return resultTypeProvider.resultType();
	}

	@Override
	public IBoatDataState boatDataState() {
		return boatDataState;
	}

	@Override
	public void dispose() {
		disposeHandler.dispose();
	}

}
