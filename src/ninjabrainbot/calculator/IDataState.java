package ninjabrainbot.calculator;

import ninjabrainbot.calculator.blind.BlindResult;
import ninjabrainbot.calculator.divine.IDivineContext;
import ninjabrainbot.util.IObservable;

public interface IDataState {

	public IDivineContext getDivineContext();

	public IThrowSet getThrowSet();

	public IObservable<ICalculatorResult> whenCalculatorResultChanged();

	public IObservable<BlindResult> whenBlindResultChanged();

	public IObservable<Boolean> whenLockedChanged();

	public void toggleLocked();

	public void reset();

}
