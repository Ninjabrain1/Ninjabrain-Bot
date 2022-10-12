package ninjabrainbot.calculator;

import ninjabrainbot.calculator.blind.BlindPosition;
import ninjabrainbot.calculator.blind.BlindResult;
import ninjabrainbot.calculator.divine.DivineResult;
import ninjabrainbot.calculator.divine.IDivineContext;
import ninjabrainbot.util.IObservable;
import ninjabrainbot.util.ISet;

public interface ICalculator {

	public ICalculatorResult triangulate(ISet<IThrow> eyeThrows, IObservable<IThrow> playerPos);

	public BlindResult blind(BlindPosition b);

	public DivineResult divine();

	public void setDivineContext(IDivineContext divineContext);
	
}
