package ninjabrainbot.data.calculator;

import ninjabrainbot.data.blind.BlindPosition;
import ninjabrainbot.data.blind.BlindResult;
import ninjabrainbot.data.divine.DivineResult;
import ninjabrainbot.data.divine.IDivineContext;
import ninjabrainbot.data.endereye.IThrow;
import ninjabrainbot.event.IObservable;
import ninjabrainbot.util.ISet;

public interface ICalculator {

	public ICalculatorResult triangulate(ISet<IThrow> eyeThrows, IObservable<IThrow> playerPos);

	public BlindResult blind(BlindPosition b);

	public DivineResult divine();

	public void setDivineContext(IDivineContext divineContext);

}
