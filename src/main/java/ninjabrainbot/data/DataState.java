package ninjabrainbot.data;

import ninjabrainbot.data.calculator.CalculatorManager;
import ninjabrainbot.data.calculator.ICalculator;
import ninjabrainbot.data.calculator.ICalculatorResult;
import ninjabrainbot.data.calculator.alladvancements.AllAdvancementsDataState;
import ninjabrainbot.data.calculator.alladvancements.IAllAdvancementsDataState;
import ninjabrainbot.data.calculator.blind.BlindResult;
import ninjabrainbot.data.calculator.common.IPlayerPosition;
import ninjabrainbot.data.calculator.divine.DivineContext;
import ninjabrainbot.data.calculator.divine.DivineResult;
import ninjabrainbot.data.calculator.divine.IDivineContext;
import ninjabrainbot.data.calculator.endereye.IThrow;
import ninjabrainbot.data.calculator.highprecision.BoatDataState;
import ninjabrainbot.data.calculator.highprecision.IBoatDataState;
import ninjabrainbot.data.calculator.stronghold.ChunkPrediction;
import ninjabrainbot.data.temp.DataComponent;
import ninjabrainbot.data.temp.IDataComponent;
import ninjabrainbot.data.temp.IDomainModel;
import ninjabrainbot.data.temp.IListComponent;
import ninjabrainbot.data.temp.ListComponent;
import ninjabrainbot.event.DisposeHandler;
import ninjabrainbot.event.IDisposable;
import ninjabrainbot.event.IObservable;

public class DataState implements IDataState, IDisposable {

	final AllAdvancementsDataState allAdvancementsDataState;
	final BoatDataState boatDataState;

	private final DataComponent<Boolean> locked;

	private final DivineContext divineContext;
	private final ListComponent<IThrow> throwSet;
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

//	public DataStateUndoData getUndoData() {
//		return new DataStateUndoData(throwSet, playerPosition.get(), divineContext);
//	}
//
//	public void setFromUndoData(DataStateUndoData undoData) {
//		divineContext.setFossil(undoData.fossil);
//		throwSet.setFromList(undoData.eyeThrows);
//		playerPosition.set(undoData.playerPos);
//	}

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
	public IListComponent<IThrow> getThrowSet() {
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
