package ninjabrainbot.model.datastate.calculator;

import ninjabrainbot.model.datastate.blind.BlindResult;
import ninjabrainbot.model.datastate.divine.DivineResult;
import ninjabrainbot.model.datastate.stronghold.ChunkPrediction;
import ninjabrainbot.model.domainmodel.IDomainModelComponent;

public interface ICalculatorManager {

	IDomainModelComponent<ICalculatorResult> calculatorResult();

	IDomainModelComponent<ChunkPrediction> topPrediction();

	IDomainModelComponent<BlindResult> blindResult();

	IDomainModelComponent<DivineResult> divineResult();

}
