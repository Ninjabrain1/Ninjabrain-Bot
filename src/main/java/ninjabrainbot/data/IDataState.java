package ninjabrainbot.data;

import ninjabrainbot.data.calculator.ICalculatorResult;
import ninjabrainbot.data.calculator.alladvancements.IAllAdvancementsDataState;
import ninjabrainbot.data.calculator.blind.BlindResult;
import ninjabrainbot.data.calculator.common.IPlayerPosition;
import ninjabrainbot.data.calculator.divine.DivineResult;
import ninjabrainbot.data.calculator.divine.IDivineContext;
import ninjabrainbot.data.calculator.endereye.IEnderEyeThrow;
import ninjabrainbot.data.calculator.highprecision.IBoatDataState;
import ninjabrainbot.data.calculator.stronghold.ChunkPrediction;
import ninjabrainbot.data.domainmodel.IDataComponent;
import ninjabrainbot.data.domainmodel.IListComponent;
import ninjabrainbot.event.IObservable;

public interface IDataState {

	IAllAdvancementsDataState allAdvancementsDataState();

	IBoatDataState boatDataState();

	IDivineContext getDivineContext();

	IListComponent<IEnderEyeThrow> getThrowList();

	IDataComponent<IPlayerPosition> playerPosition();

	IDataComponent<Boolean> locked();

	IObservable<ICalculatorResult> calculatorResult();

	IObservable<ChunkPrediction> topPrediction();

	IObservable<BlindResult> blindResult();

	IObservable<DivineResult> divineResult();

	IObservable<ResultType> resultType();

}
