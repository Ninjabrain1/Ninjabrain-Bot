package ninjabrainbot.data;

import ninjabrainbot.data.alladvancements.IAllAdvancementsDataState;
import ninjabrainbot.data.blind.BlindResult;
import ninjabrainbot.data.calculator.ICalculatorResult;
import ninjabrainbot.data.divine.DivineResult;
import ninjabrainbot.data.divine.IDivineContext;
import ninjabrainbot.data.endereye.IThrow;
import ninjabrainbot.data.endereye.IThrowSet;
import ninjabrainbot.data.highprecision.IBoatDataState;
import ninjabrainbot.data.stronghold.ChunkPrediction;
import ninjabrainbot.event.IObservable;

public interface IDataState {

	IAllAdvancementsDataState allAdvancementDataState();

	IBoatDataState boatDataState();

	IDivineContext getDivineContext();

	IThrowSet getThrowSet();

	IObservable<IThrow> playerPosition();

	IObservable<ICalculatorResult> calculatorResult();

	IObservable<ChunkPrediction> topPrediction();

	IObservable<BlindResult> blindResult();

	IObservable<DivineResult> divineResult();

	IObservable<Boolean> locked();

	IObservable<ResultType> resultType();

	void toggleLocked();

	void reset();

}
