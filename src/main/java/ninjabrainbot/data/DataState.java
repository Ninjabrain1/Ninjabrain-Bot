package ninjabrainbot.data;

import ninjabrainbot.data.blind.BlindPosition;
import ninjabrainbot.data.blind.BlindResult;
import ninjabrainbot.data.calculator.ICalculator;
import ninjabrainbot.data.calculator.ICalculatorResult;
import ninjabrainbot.data.calculator.ResultType;
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
import ninjabrainbot.event.IDisposable;
import ninjabrainbot.event.IObservable;
import ninjabrainbot.event.ObservableField;
import ninjabrainbot.event.SubscriptionHandler;

public class DataState implements IDataState, IDisposable {

	final BoatDataState boatDataState;

	private final ICalculator calculator;

	private final ObservableField<Boolean> locked;
	private final ObservableField<Boolean> allAdvancementsMode;

	private final DivineContext divineContext;
	private final ThrowSet throwSet;
	private final ObservableField<IThrow> playerPos;

	private final ObservableField<ResultType> resultType;
	private final ObservableField<ICalculatorResult> calculatorResult;
	private final ObservableField<ChunkPrediction> topPrediction;
	private final ObservableField<BlindResult> blindResult;
	private final ObservableField<DivineResult> divineResult;

	private SubscriptionHandler sh = new SubscriptionHandler();

	public DataState(ICalculator calculator, IModificationLock modificationLock) {
		divineContext = new DivineContext(modificationLock);
		throwSet = new ThrowSet(modificationLock);

		playerPos = new LockableField<IThrow>(modificationLock);
		locked = new LockableField<Boolean>(false, modificationLock);
		allAdvancementsMode = new LockableField<Boolean>(false, modificationLock);
		boatDataState = new BoatDataState(modificationLock);

		resultType = new LockableField<ResultType>(ResultType.NONE, modificationLock);
		calculatorResult = new LockableField<ICalculatorResult>(modificationLock);
		topPrediction = new LockableField<ChunkPrediction>(modificationLock);
		blindResult = new LockableField<BlindResult>(modificationLock);
		divineResult = new LockableField<DivineResult>(modificationLock);

		calculator.setDivineContext(divineContext);
		this.calculator = calculator;

		// Subscriptions
		sh.add(throwSet.whenModified().subscribe(__ -> recalculateStronghold()));
		sh.add(divineContext.whenFossilChanged().subscribe(__ -> onFossilChanged()));
	}

	@Override
	public void reset() {
		allAdvancementsMode.set(false);
		boatDataState.reset();
		throwSet.clear();
		playerPos.set(null);
		blindResult.set(null);
		divineResult.set(null);
		divineContext.clear();
		updateResultType();
	}

	@Override
	public void toggleLocked() {
		locked.set(!locked.get());
	}

	@Override
	public void dispose() {
		sh.dispose();
		if (calculatorResult.get() != null)
			calculatorResult.get().dispose();
		throwSet.dispose();
	}

	public void recalculateStronghold() {
		if (calculatorResult.get() != null)
			calculatorResult.get().dispose();
		calculatorResult.set(calculator.triangulate(throwSet, playerPos));
		updateTopPrediction(calculatorResult.get());
		updateResultType();
	}

	public DataStateUndoData getUndoData() {
		return new DataStateUndoData(throwSet, playerPos.get(), divineContext);
	}

	public void setFromUndoData(DataStateUndoData undoData) {
		divineContext.setFossil(undoData.fossil);
		throwSet.setFromList(undoData.eyeThrows);
		playerPos.set(undoData.playerPos);
		updateResultType();
	}

	public void setAllAdvancementsMode(boolean enabled) {
		allAdvancementsMode.set(enabled);
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
		updateResultType();
	}

	void setFossil(Fossil f) {
		divineContext.setFossil(f);
	}

	void setPlayerPos(IThrow t) {
		playerPos.set(t);
	}

	void setBlindPosition(BlindPosition t) {
		blindResult.set(calculator.blind(t));
		updateResultType();
	}

	private void updateResultType() {
		resultType.set(getExpectedResultType());
	}

	private ResultType getExpectedResultType() {
		if (allAdvancementsMode.get())
			return ResultType.ALL_ADVANCEMENTS;

		if (calculatorResult.get() != null && calculatorResult.get().success())
			return ResultType.TRIANGULATION;

		if (calculatorResult.get() != null)
			return ResultType.FAILED;

		if (playerPos.get() != null)
			return ResultType.BLIND;

		if (divineContext.getFossil() != null)
			return ResultType.DIVINE;

		return ResultType.NONE;
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
		return resultType;
	}

	@Override
	public IBoatDataState boatDataState() {
		return boatDataState();
	}

}
