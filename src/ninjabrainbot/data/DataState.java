package ninjabrainbot.data;

import ninjabrainbot.Main;
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
import ninjabrainbot.event.IDisposable;
import ninjabrainbot.event.IObservable;
import ninjabrainbot.event.ObservableField;
import ninjabrainbot.event.SubscriptionHandler;

public class DataState implements IDataState, IDisposable {

	private final ICalculator calculator;

	private final DivineContext divineContext;
	private final ThrowSet throwSet;
	private final ObservableField<IThrow> playerPos;
	private final ObservableField<Boolean> locked;

	private final ObservableField<ResultType> resultType;
	private final ObservableField<ICalculatorResult> calculatorResult;
	private final ObservableField<BlindResult> blindResult;
	private final ObservableField<DivineResult> divineResult;

	private SubscriptionHandler sh = new SubscriptionHandler();

	public DataState(ICalculator calculator, IModificationLock modificationLock) {
		divineContext = new DivineContext(modificationLock);
		throwSet = new ThrowSet(modificationLock);
		playerPos = new LockableField<IThrow>(modificationLock);
		locked = new LockableField<Boolean>(false, modificationLock);
		resultType = new LockableField<ResultType>(ResultType.NONE, modificationLock);
		calculatorResult = new LockableField<ICalculatorResult>(modificationLock);
		blindResult = new LockableField<BlindResult>(modificationLock);
		divineResult = new LockableField<DivineResult>(modificationLock);

		calculator.setDivineContext(divineContext);
		this.calculator = calculator;

		// Subscriptions
		sh.add(throwSet.whenModified().subscribe(__ -> recalculateStronghold()));
		sh.add(divineContext.whenFossilChanged().subscribe(__ -> onFossilChanged()));
		sh.add(Main.preferences.useAdvStatistics.whenModified().subscribe(__ -> recalculateStronghold()));
		sh.add(Main.preferences.mcVersion.whenModified().subscribe(__ -> recalculateStronghold()));
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
	public void reset() {
		resultType.set(ResultType.NONE);
		throwSet.clear();
		playerPos.set(null);
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
		sh.dispose();
		if (calculatorResult.get() != null)
			calculatorResult.get().dispose();
		throwSet.dispose();
	}

	private void recalculateStronghold() {
		if (calculatorResult.get() != null)
			calculatorResult.get().dispose();
		calculatorResult.set(calculator.triangulate(throwSet, playerPos));
		throwSet.setAngleErrors(calculatorResult.get());
		resultType.set(calculatorResult.get() == null ? ResultType.NONE : calculatorResult().get().success() ? ResultType.TRIANGULATION : ResultType.FAILED);
	}

	private void onFossilChanged() {
		if (throwSet.size() != 0) {
			recalculateStronghold();
		} else {
			divineResult.set(calculator.divine());
			resultType.set(divineResult.get() != null ? ResultType.DIVINE : ResultType.NONE);
		}
	}

	void setFossil(Fossil f) {
		divineContext.setFossil(f);
	}

	void setPlayerPos(IThrow t) {
		playerPos.set(t);
	}

	void setBlindPosition(BlindPosition t) {
		blindResult.set(calculator.blind(t));
		resultType.set(ResultType.BLIND);
	}

}
