package ninjabrainbot.data.endereye;

import ninjabrainbot.data.datalock.IModificationLock;
import ninjabrainbot.data.divine.Fossil;
import ninjabrainbot.data.highprecision.BoatThrow;
import ninjabrainbot.event.IObservable;
import ninjabrainbot.event.ISubscribable;
import ninjabrainbot.event.ObservableProperty;
import ninjabrainbot.io.IClipboardProvider;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;

public class ThrowParser implements IThrowSource {

	NinjabrainBotPreferences preferences;
	private IObservable<Float> boatAngle;
	private IModificationLock modificationLock;

	private ObservableProperty<IThrow> whenNewThrowInputed;
	private ObservableProperty<Fossil> whenNewFossilInputed;

	public ThrowParser(IClipboardProvider clipboardProvider, NinjabrainBotPreferences preferences, IModificationLock modificationLock, IObservable<Float> boatAngle) {
		this.preferences = preferences;
		this.modificationLock = modificationLock;
		this.boatAngle = boatAngle;

		whenNewThrowInputed = new ObservableProperty<IThrow>();
		whenNewFossilInputed = new ObservableProperty<Fossil>();

		clipboardProvider.whenClipboardChanged().subscribe(clipboardString -> parseF3C(clipboardString));
	}

	public void parseF3C(String f3c) {
		IThrow t = null;
		if ((preferences.useTallRes.get() && preferences.usePreciseAngle.get() && boatAngle.get() != null)) {
			t = BoatThrow.parseF3C(f3c, preferences, modificationLock, boatAngle.get());
		} else {
			t = Throw.parseF3C(f3c, preferences.crosshairCorrection.get(), modificationLock);
		}
		if (t != null) {
			whenNewThrowInputed.notifySubscribers(t);
			return;
		}
		final IThrow t2 = Throw1_12.parseF3C(f3c, preferences.crosshairCorrection.get(), modificationLock);
		if (t2 != null) {
			whenNewThrowInputed.notifySubscribers(t);
			return;
		}
		final Fossil f = Fossil.parseF3I(f3c);
		if (f != null) {
			whenNewFossilInputed.notifySubscribers(f);
		}
	}

	public ISubscribable<IThrow> whenNewThrowInputed() {
		return whenNewThrowInputed;
	}

	public ISubscribable<Fossil> whenNewFossilInputed() {
		return whenNewFossilInputed;
	}

}
