package ninjabrainbot.model.datastate;

import ninjabrainbot.model.datastate.blind.BlindResult;
import ninjabrainbot.model.datastate.divine.DivineResult;
import ninjabrainbot.model.datastate.stronghold.ChunkPrediction;
import ninjabrainbot.event.IObservable;

public interface ICalculatorManager {

	IObservable<ICalculatorResult> calculatorResult();

	IObservable<ChunkPrediction> topPrediction();

	IObservable<BlindResult> blindResult();

	IObservable<DivineResult> divineResult();

}
