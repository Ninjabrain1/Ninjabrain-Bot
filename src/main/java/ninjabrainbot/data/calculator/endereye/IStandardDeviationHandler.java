package ninjabrainbot.data.calculator.endereye;

import ninjabrainbot.event.ISubscribable;

public interface IStandardDeviationHandler {

	ISubscribable<IStandardDeviationHandler> whenModified();

	double getNormalStandardDeviation();

	double getAlternativeStandardDeviation();

	double getManualStandardDeviation();

	double getBoatStandardDeviation();

}
