package ninjabrainbot.data.calculator;

import ninjabrainbot.data.calculator.blind.BlindResult;
import ninjabrainbot.data.calculator.divine.DivineResult;
import ninjabrainbot.data.calculator.stronghold.ChunkPrediction;
import ninjabrainbot.event.IObservable;

public interface ICalculatorManager {

	IObservable<ICalculatorResult> calculatorResult();

	IObservable<ChunkPrediction> topPrediction();

	IObservable<BlindResult> blindResult();

	IObservable<DivineResult> divineResult();

}
