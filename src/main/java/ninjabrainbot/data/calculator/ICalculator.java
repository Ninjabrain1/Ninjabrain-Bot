package ninjabrainbot.data.calculator;

import ninjabrainbot.data.calculator.blind.BlindPosition;
import ninjabrainbot.data.calculator.blind.BlindResult;
import ninjabrainbot.data.calculator.divine.DivineResult;
import ninjabrainbot.data.calculator.divine.IDivineContext;
import ninjabrainbot.data.calculator.endereye.IThrow;
import ninjabrainbot.event.IObservable;
import ninjabrainbot.util.ISet;

public interface ICalculator {

	public ICalculatorResult triangulate(ISet<IThrow> eyeThrows, IObservable<IThrow> playerPos, IDivineContext divineContext);

	public BlindResult blind(BlindPosition b, IDivineContext divineContext);

	public DivineResult divine(IDivineContext divineContext);

}
