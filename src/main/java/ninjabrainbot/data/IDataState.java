package ninjabrainbot.data;

import java.util.List;

import ninjabrainbot.data.blind.BlindResult;
import ninjabrainbot.data.calculator.ICalculatorResult;
import ninjabrainbot.data.calculator.ResultType;
import ninjabrainbot.data.divine.DivineResult;
import ninjabrainbot.data.divine.IDivineContext;
import ninjabrainbot.data.endereye.IThrowSet;
import ninjabrainbot.data.information.InformationMessage;
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

	public IObservable<List<InformationMessage>> informationMessages();

	public void toggleLocked();

	public void reset();

}
