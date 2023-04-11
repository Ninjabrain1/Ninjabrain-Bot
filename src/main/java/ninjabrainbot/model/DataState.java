package ninjabrainbot.model;

import ninjabrainbot.model.datastate.CalculatorManager;
import ninjabrainbot.model.datastate.ICalculator;
import ninjabrainbot.model.datastate.ICalculatorResult;
import ninjabrainbot.model.datastate.alladvancements.AllAdvancementsDataState;
import ninjabrainbot.model.datastate.alladvancements.IAllAdvancementsDataState;
import ninjabrainbot.model.datastate.blind.BlindResult;
import ninjabrainbot.model.datastate.common.IPlayerPosition;
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
import ninjabrainbot.model.domainmodel.IListComponent;
import ninjabrainbot.model.domainmodel.ListComponent;
import ninjabrainbot.event.DisposeHandler;
import ninjabrainbot.event.IDisposable;
import ninjabrainbot.event.IObservable;

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

	public DataState(ICalculator calculator, IDomainModel domainModel) {
		divineContext = new DivineContext(domainModel);
		throwSet = new ListComponent<>(domainModel, 10);
		playerPosition = new DataComponent<>(domainModel);
		locked = new DataComponent<>(domainModel, false);

		calculatorManager = disposeHandler.add(new CalculatorManager(calculator, throwSet, playerPosition, divineContext));
		allAdvancementsDataState = new AllAdvancementsDataState(calculatorManager.topPrediction(), domainModel);
		boatDataState = new BoatDataState(domainModel);

		resultTypeProvider = disposeHandler.add(new ResultTypeProvider(this));
	}

	void setCalculator(ICalculator calculator) {
		calculatorManager.setCalculator(calculator);
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
	public IObservable<ICalculatorResult> calculatorResult() {
		return calculatorManager.calculatorResult();
	}

	@Override
	public IObservable<ChunkPrediction> topPrediction() {
		return calculatorManager.topPrediction();
	}

	@Override
	public IObservable<BlindResult> blindResult() {
		return calculatorManager.blindResult();
	}

	@Override
	public IObservable<DivineResult> divineResult() {
		return calculatorManager.divineResult();
	}

	@Override
	public IObservable<ResultType> resultType() {
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
