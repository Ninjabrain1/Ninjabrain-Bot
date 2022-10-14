package ninjabrainbot.calculator;

import ninjabrainbot.calculator.blind.BlindResult;
import ninjabrainbot.calculator.divine.DivineResult;
import ninjabrainbot.calculator.divine.IDivineContext;
import ninjabrainbot.util.IObservable;

public interface IDataState {

	public IDivineContext getDivineContext();

	public IThrowSet getThrowSet();

	public IObservable<ICalculatorResult> calculatorResult();

	public IObservable<BlindResult> blindResult();
	
	public IObservable<DivineResult> divineResult();

	public IObservable<Boolean> locked();
	
	public IObservable<ResultType> resultType();

	public void toggleLocked();

	public void reset();

}
