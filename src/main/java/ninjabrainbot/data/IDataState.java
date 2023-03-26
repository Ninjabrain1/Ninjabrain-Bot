package ninjabrainbot.data;

import ninjabrainbot.data.blind.BlindResult;
import ninjabrainbot.data.calculator.ICalculatorResult;
import ninjabrainbot.data.calculator.ResultType;
import ninjabrainbot.data.divine.DivineResult;
import ninjabrainbot.data.divine.IDivineContext;
import ninjabrainbot.data.endereye.IThrow;
import ninjabrainbot.data.endereye.IThrowSet;
import ninjabrainbot.data.highprecision.IBoatDataState;
import ninjabrainbot.data.stronghold.ChunkPrediction;
import ninjabrainbot.event.IObservable;

public interface IDataState {

	public IDivineContext getDivineContext();

	public IThrowSet getThrowSet();

	public IObservable<ICalculatorResult> calculatorResult();

	public IObservable<ChunkPrediction> topPrediction();

	public IObservable<IThrow> playerPosition();

	public IObservable<BlindResult> blindResult();

	public IObservable<DivineResult> divineResult();

	public IObservable<Boolean> locked();

	public IObservable<ResultType> resultType();

	public IBoatDataState boatDataState();

	public void toggleLocked();

	public void reset();

}
