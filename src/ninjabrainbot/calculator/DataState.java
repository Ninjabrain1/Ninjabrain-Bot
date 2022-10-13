package ninjabrainbot.calculator;

import ninjabrainbot.Main;
import ninjabrainbot.calculator.blind.BlindPosition;
import ninjabrainbot.calculator.blind.BlindResult;
import ninjabrainbot.calculator.divine.DivineContext;
import ninjabrainbot.calculator.divine.DivineResult;
import ninjabrainbot.calculator.divine.Fossil;
import ninjabrainbot.calculator.divine.IDivineContext;
import ninjabrainbot.util.IDisposable;
import ninjabrainbot.util.IObservable;
import ninjabrainbot.util.ISubscribable;
import ninjabrainbot.util.ObservableField;
import ninjabrainbot.util.SubscriptionHandler;

public class DataState implements IDataState, IDisposable {

	private ICalculator calculator;
	private IStdProfile stdProfile;

	private DivineContext divineContext;
	private ThrowSet throwSet;
	private ObservableField<Boolean> locked = new ObservableField<Boolean>(false);
	private ObservableField<IThrow> playerPos = new ObservableField<IThrow>(null);
	private ObservableField<ICalculatorResult> calculatorResult = new ObservableField<ICalculatorResult>(null);
	private ObservableField<BlindResult> blindResult = new ObservableField<BlindResult>(null);
	private ObservableField<DivineResult> divineResult = new ObservableField<DivineResult>(null);

	private SubscriptionHandler sh = new SubscriptionHandler();

	public DataState(ICalculator calculator, ISubscribable<IThrow> throwStream, ISubscribable<Fossil> fossilStream, IStdProfile stdProfile) {
		divineContext = new DivineContext();
		throwSet = new ThrowSet();

		calculator.setDivineContext(divineContext);
		this.calculator = calculator;
		this.stdProfile = stdProfile;

		// Subscriptions
		throwStream.subscribe(t -> onNewThrow(t));
		fossilStream.subscribe(f -> divineContext.setFossil(f));
		sh.add(throwSet.whenModified().subscribe(__ -> recalculateStronghold()));
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
	public IObservable<ICalculatorResult> whenCalculatorResultChanged() {
		return calculatorResult;
	}

	@Override
	public IObservable<BlindResult> whenBlindResultChanged() {
		return blindResult;
	}

	@Override
	public IObservable<DivineResult> whenDivineResultChanged() {
		return divineResult;
	}

	@Override
	public IObservable<Boolean> whenLockedChanged() {
		return locked;
	}

	@Override
	public void reset() {
		throwSet.clear();
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
	}

	private void onNewThrow(IThrow t) {
		if (locked.get()) {
			setPlayerPos(t);
			return;
		}
		if (t.isNether()) {
			if (throwSet.size() == 0)
				blindResult.set(calculator.blind(new BlindPosition(t)));
			else
				setPlayerPos(t);
			return;
		}
		t.setStdProfile(stdProfile);
		if (t.lookingBelowHorizon() || !throwSet.add(t))
			setPlayerPos(t);
	}

	private void setPlayerPos(IThrow t) {
		if (throwSet.size() > 0) {
			playerPos.set(t);
		}
	}

}