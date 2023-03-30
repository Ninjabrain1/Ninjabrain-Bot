package ninjabrainbot.data;

import ninjabrainbot.data.alladvancements.AllAdvancementsDataState;
import ninjabrainbot.data.alladvancements.IAllAdvancementsDataState;
import ninjabrainbot.data.blind.BlindPosition;
import ninjabrainbot.data.blind.BlindResult;
import ninjabrainbot.data.calculator.ICalculator;
import ninjabrainbot.data.calculator.ICalculatorResult;
import ninjabrainbot.data.datalock.IModificationLock;
import ninjabrainbot.data.datalock.LockableField;
import ninjabrainbot.data.divine.DivineContext;
import ninjabrainbot.data.divine.DivineResult;
import ninjabrainbot.data.divine.Fossil;
import ninjabrainbot.data.divine.IDivineContext;
import ninjabrainbot.data.endereye.IThrow;
import ninjabrainbot.data.endereye.IThrowSet;
import ninjabrainbot.data.endereye.ThrowSet;
import ninjabrainbot.data.highprecision.BoatDataState;
import ninjabrainbot.data.highprecision.IBoatDataState;
import ninjabrainbot.data.stronghold.ChunkPrediction;
import ninjabrainbot.event.DisposeHandler;
import ninjabrainbot.event.IDisposable;
import ninjabrainbot.event.IObservable;
import ninjabrainbot.event.ObservableField;

public class DataState implements IDataState, IDisposable {

	final AllAdvancementsDataState allAdvancementsDataState;
	final BoatDataState boatDataState;

	private final ICalculator calculator;

	private final ObservableField<Boolean> locked;

	private final DivineContext divineContext;
	private final ThrowSet throwSet;
	private final ObservableField<IThrow> playerPosition;

	private final ObservableField<ICalculatorResult> calculatorResult;
	private final ObservableField<ChunkPrediction> topPrediction;
	private final ObservableField<BlindResult> blindResult;
	private final ObservableField<DivineResult> divineResult;

	private final ResultTypeProvider resultTypeProvider;

	private final DisposeHandler disposeHandler = new DisposeHandler();

	public DataState(ICalculator calculator, IModificationLock modificationLock) {
		divineContext = new DivineContext(modificationLock);
		throwSet = new ThrowSet(modificationLock);

		playerPosition = new LockableField<>(modificationLock);
		locked = new LockableField<>(false, modificationLock);

		calculatorResult = new LockableField<>(modificationLock);
		topPrediction = new LockableField<>(modificationLock);
		blindResult = new LockableField<>(modificationLock);
		divineResult = new LockableField<>(modificationLock);

		allAdvancementsDataState = new AllAdvancementsDataState(topPrediction, modificationLock);
		boatDataState = new BoatDataState(modificationLock);

		resultTypeProvider = new ResultTypeProvider(this, modificationLock);

		calculator.setDivineContext(divineContext);
		this.calculator = calculator;

		// Subscriptions
		disposeHandler.add(throwSet.whenModified().subscribe(__ -> recalculateStronghold()));
		disposeHandler.add(divineContext.fossil().subscribe(__ -> onFossilChanged()));
	}

	@Override
	public void reset() {
		allAdvancementsDataState.reset();
		boatDataState.reset();
		throwSet.clear();
		playerPosition.set(null);
		blindResult.set(null);
		divineResult.set(null);
		divineContext.clear();
	}

	@Override
	public void toggleLocked() {
		locked.set(!locked.get());
	}

	@Override
	public void dispose() {
		disposeHandler.dispose();
		if (calculatorResult.get() != null)
			calculatorResult.get().dispose();
		throwSet.dispose();
	}

	public void recalculateStronghold() {
		if (calculatorResult.get() != null)
			calculatorResult.get().dispose();
		calculatorResult.set(calculator.triangulate(throwSet, playerPosition));
		updateTopPrediction(calculatorResult.get());
	}

	public DataStateUndoData getUndoData() {
		return new DataStateUndoData(throwSet, playerPosition.get(), divineContext);
	}

	public void setFromUndoData(DataStateUndoData undoData) {
		divineContext.setFossil(undoData.fossil);
		throwSet.setFromList(undoData.eyeThrows);
		playerPosition.set(undoData.playerPos);
	}

	private void updateTopPrediction(ICalculatorResult calculatorResult) {
		if (calculatorResult == null || !calculatorResult.success()) {
			topPrediction.set(null);
			return;
		}
		topPrediction.set(calculatorResult.getBestPrediction());
	}

	private void onFossilChanged() {
		if (throwSet.size() != 0) {
			recalculateStronghold();
		} else {
			divineResult.set(calculator.divine());
		}
	}

	void setFossil(Fossil f) {
		divineContext.setFossil(f);
	}

	void setPlayerPosition(IThrow t) {
		playerPosition.set(t);
	}

	void setBlindPosition(BlindPosition t) {
		blindResult.set(calculator.blind(t));
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
	public IObservable<ICalculatorResult> calculatorResult() {
		return calculatorResult;
	}

	@Override
	public IObservable<IThrow> playerPosition() {
		return playerPosition;
	}

	@Override
	public IObservable<ChunkPrediction> topPrediction() {
		return topPrediction;
	}

	@Override
	public IObservable<BlindResult> blindResult() {
		return blindResult;
	}

	@Override
	public IObservable<DivineResult> divineResult() {
		return divineResult;
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

}
