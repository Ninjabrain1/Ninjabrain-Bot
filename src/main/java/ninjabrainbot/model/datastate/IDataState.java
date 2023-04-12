package ninjabrainbot.model.datastate;

import ninjabrainbot.model.datastate.calculator.ICalculatorResult;
import ninjabrainbot.model.datastate.alladvancements.IAllAdvancementsDataState;
import ninjabrainbot.model.datastate.blind.BlindResult;
import ninjabrainbot.model.datastate.common.IPlayerPosition;
import ninjabrainbot.model.datastate.divine.DivineResult;
import ninjabrainbot.model.datastate.divine.IDivineContext;
import ninjabrainbot.model.datastate.endereye.IEnderEyeThrow;
import ninjabrainbot.model.datastate.highprecision.IBoatDataState;
import ninjabrainbot.model.datastate.stronghold.ChunkPrediction;
import ninjabrainbot.model.domainmodel.IDataComponent;
import ninjabrainbot.model.domainmodel.IListComponent;
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
