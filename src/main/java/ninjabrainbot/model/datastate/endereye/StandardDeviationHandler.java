package ninjabrainbot.model.datastate.endereye;

import ninjabrainbot.event.DisposeHandler;
import ninjabrainbot.event.IDisposable;
import ninjabrainbot.event.ISubscribable;
import ninjabrainbot.event.ObservableProperty;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;

public class StandardDeviationHandler implements IStandardDeviationHandler, IDisposable {

	private final ObservableProperty<IStandardDeviationHandler> whenModified = new ObservableProperty<>();

	private double normalStandardDeviation;
	private double alternativeStandardDeviation;
	private double manualStandardDeviation;
	private double boatStandardDeviation;

	private final DisposeHandler disposeHandler = new DisposeHandler();

	public StandardDeviationHandler(NinjabrainBotPreferences preferences) {
		normalStandardDeviation = preferences.sigma.get();
		alternativeStandardDeviation = preferences.sigmaAlt.get();
		manualStandardDeviation = preferences.sigmaManual.get();
		boatStandardDeviation = preferences.sigmaBoat.get();
		disposeHandler.add(preferences.sigma.whenModified().subscribe(newStd -> {
			normalStandardDeviation = newStd;
			whenModified.notifySubscribers(this);
		}));
		disposeHandler.add(preferences.sigmaAlt.whenModified().subscribe(newStd -> {
			alternativeStandardDeviation = newStd;
			whenModified.notifySubscribers(this);
		}));
		disposeHandler.add(preferences.sigmaManual.whenModified().subscribe(newStd -> {
			manualStandardDeviation = newStd;
			whenModified.notifySubscribers(this);
		}));
		disposeHandler.add(preferences.sigmaBoat.whenModified().subscribe(newStd -> {
			boatStandardDeviation = newStd;
			whenModified.notifySubscribers(this);
		}));
	}

	@Override
	public ISubscribable<IStandardDeviationHandler> whenModified() {
		return whenModified;
	}

	@Override
	public double getNormalStandardDeviation() {
		return normalStandardDeviation;
	}

	@Override
	public double getAlternativeStandardDeviation() {
		return alternativeStandardDeviation;
	}

	@Override
	public double getManualStandardDeviation() {
		return manualStandardDeviation;
	}

	@Override
	public double getBoatStandardDeviation() {
		return boatStandardDeviation;
	}

	@Override
	public void dispose() {
		disposeHandler.dispose();
	}
}
