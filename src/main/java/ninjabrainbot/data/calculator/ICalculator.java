package ninjabrainbot.data.calculator;

import ninjabrainbot.data.calculator.blind.BlindPosition;
import ninjabrainbot.data.calculator.blind.BlindResult;
import ninjabrainbot.data.calculator.common.IPlayerPosition;
import ninjabrainbot.data.calculator.divine.DivineResult;
import ninjabrainbot.data.calculator.divine.IDivineContext;
import ninjabrainbot.data.calculator.endereye.IEnderEyeThrow;
import ninjabrainbot.event.IObservable;
import ninjabrainbot.event.IReadOnlyList;

public interface ICalculator {

	ICalculatorResult triangulate(IReadOnlyList<IEnderEyeThrow> eyeThrows, IObservable<IPlayerPosition> playerPos, IDivineContext divineContext);

	BlindResult blind(BlindPosition b, IDivineContext divineContext);

	DivineResult divine(IDivineContext divineContext);

}
