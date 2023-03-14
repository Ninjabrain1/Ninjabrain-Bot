package ninjabrainbot.data;

import ninjabrainbot.data.blind.BlindResult;
import ninjabrainbot.data.calculator.ICalculatorResult;
import ninjabrainbot.data.calculator.ResultType;
import ninjabrainbot.data.divine.DivineResult;
import ninjabrainbot.data.divine.IDivineContext;
import ninjabrainbot.data.endereye.IThrowSet;
import ninjabrainbot.data.highprecision.BoatState;
import ninjabrainbot.data.stronghold.ChunkPrediction;
import ninjabrainbot.event.IObservable;

public interface IDataState {

	public IDivineContext getDivineContext();

	public IThrowSet getThrowSet();

	public IObservable<ICalculatorResult> calculatorResult();

	public IObservable<ChunkPrediction> topPrediction();

	public IObservable<BlindResult> blindResult();

	public IObservable<DivineResult> divineResult();

	public IObservable<Boolean> locked();

	public IObservable<ResultType> resultType();

	public IObservable<Boolean> enteringBoat();

	public IObservable<Float> boatAngle();

	public IObservable<BoatState> boatState();

	public void toggleLocked();

	public void reset();

}
