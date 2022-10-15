package ninjabrainbot.data;

import ninjabrainbot.data.blind.BlindResult;
import ninjabrainbot.data.calculator.ICalculatorResult;
import ninjabrainbot.data.divine.DivineResult;
import ninjabrainbot.data.divine.IDivineContext;
import ninjabrainbot.data.endereye.IThrowSet;
import ninjabrainbot.event.IObservable;

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
