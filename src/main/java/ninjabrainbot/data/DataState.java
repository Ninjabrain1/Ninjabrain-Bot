package ninjabrainbot.data;

import ninjabrainbot.data.calculator.alladvancements.AllAdvancementsDataState;
import ninjabrainbot.data.calculator.alladvancements.IAllAdvancementsDataState;
import ninjabrainbot.data.calculator.blind.BlindResult;
import ninjabrainbot.data.calculator.CalculatorManager;
import ninjabrainbot.data.calculator.ICalculator;
import ninjabrainbot.data.calculator.ICalculatorResult;
import ninjabrainbot.data.datalock.IModificationLock;
import ninjabrainbot.data.datalock.LockableField;
import ninjabrainbot.data.calculator.divine.DivineContext;
import ninjabrainbot.data.calculator.divine.DivineResult;
import ninjabrainbot.data.calculator.divine.Fossil;
import ninjabrainbot.data.calculator.divine.IDivineContext;
import ninjabrainbot.data.calculator.endereye.IThrow;
import ninjabrainbot.data.calculator.endereye.IThrowSet;
import ninjabrainbot.data.calculator.endereye.ThrowSet;
import ninjabrainbot.data.calculator.highprecision.BoatDataState;
import ninjabrainbot.data.calculator.highprecision.IBoatDataState;
import ninjabrainbot.data.calculator.stronghold.ChunkPrediction;
import ninjabrainbot.event.DisposeHandler;
import ninjabrainbot.event.IDisposable;
import ninjabrainbot.event.IObservable;
import ninjabrainbot.event.ObservableField;

public class DataState implements IDataState, IDisposable {

	final AllAdvancementsDataState allAdvancementsDataState;
	final BoatDataState boatDataState;

	private final ObservableField<Boolean> locked;

	private final DivineContext divineContext;
	private final ThrowSet throwSet;
	private final ObservableField<IThrow> playerPosition;

	private final CalculatorManager calculatorManager;
	private final ResultTypeProvider resultTypeProvider;

	private final DisposeHandler disposeHandler = new DisposeHandler();

	public DataState(ICalculator calculator, IModificationLock modificationLock) {
		divineContext = new DivineContext(modificationLock);
		throwSet = new ThrowSet(modificationLock);
		playerPosition = new LockableField<>(modificationLock);
		locked = new LockableField<>(false, modificationLock);

		calculatorManager = disposeHandler.add(new CalculatorManager(calculator, throwSet, playerPosition, divineContext, modificationLock));
		allAdvancementsDataState = new AllAdvancementsDataState(calculatorManager.topPrediction(), modificationLock);
		boatDataState = new BoatDataState(modificationLock);

		resultTypeProvider = disposeHandler.add(new ResultTypeProvider(this, modificationLock));
	}

	@Override
	public void reset() {
		allAdvancementsDataState.reset();
		boatDataState.reset();
		throwSet.clear();
		playerPosition.set(null);
		divineContext.clear();
	}

	@Override
	public void toggleLocked() {
		locked.set(!locked.get());
	}

	public DataStateUndoData getUndoData() {
		return new DataStateUndoData(throwSet, playerPosition.get(), divineContext);
	}

	public void setFromUndoData(DataStateUndoData undoData) {
		divineContext.setFossil(undoData.fossil);
		throwSet.setFromList(undoData.eyeThrows);
		playerPosition.set(undoData.playerPos);
	}

	void setFossil(Fossil f) {
		divineContext.setFossil(f);
	}

	void setPlayerPosition(IThrow t) {
		playerPosition.set(t);
	}

	void setCalculator(ICalculator calculator){
		calculatorManager.setCalculator(calculator);
	}

	@Override
	public IAllAdvancementsDataState allAdvancementDataState() {
		return allAdvancementsDataState;
	}

	@Override
	public IDivineContext getDivineContext() {
		return divineContext;
	}

	@Override
	public IThrowSet getThrowSet() {
		return throwSet;
	}

	@Override
	public IObservable<IThrow> playerPosition() {
		return playerPosition;
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
	public IObservable<Boolean> locked() {
		return locked;
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
		throwSet.dispose();
	}

}
