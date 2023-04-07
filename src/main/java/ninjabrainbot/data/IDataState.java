package ninjabrainbot.data;

import ninjabrainbot.data.calculator.ICalculatorResult;
import ninjabrainbot.data.calculator.alladvancements.IAllAdvancementsDataState;
import ninjabrainbot.data.calculator.blind.BlindResult;
import ninjabrainbot.data.calculator.divine.DivineResult;
import ninjabrainbot.data.calculator.divine.IDivineContext;
import ninjabrainbot.data.calculator.endereye.IThrow;
import ninjabrainbot.data.calculator.highprecision.IBoatDataState;
import ninjabrainbot.data.calculator.stronghold.ChunkPrediction;
import ninjabrainbot.data.temp.IDataComponent;
import ninjabrainbot.data.temp.IListComponent;
import ninjabrainbot.event.IObservable;

public interface IDataState {

	IAllAdvancementsDataState allAdvancementDataState();

	IBoatDataState boatDataState();

	IDivineContext getDivineContext();

	IListComponent<IThrow> getThrowSet();

	IDataComponent<IThrow> playerPosition();

	IDataComponent<Boolean> locked();

	IObservable<ICalculatorResult> calculatorResult();

	IObservable<ChunkPrediction> topPrediction();

	IObservable<BlindResult> blindResult();

	IObservable<DivineResult> divineResult();

	IObservable<ResultType> resultType();

}
