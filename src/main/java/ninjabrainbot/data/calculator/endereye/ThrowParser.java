package ninjabrainbot.data.calculator.endereye;

import ninjabrainbot.data.calculator.divine.Fossil;
import ninjabrainbot.data.calculator.highprecision.BoatThrow;
import ninjabrainbot.data.datalock.IModificationLock;
import ninjabrainbot.event.IObservable;
import ninjabrainbot.event.ISubscribable;
import ninjabrainbot.event.ObservableField;
import ninjabrainbot.io.IClipboardProvider;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;

public class ThrowParser implements IThrowSource {

	NinjabrainBotPreferences preferences;
	private IObservable<Float> boatAngle;
	private IModificationLock modificationLock;

	private ObservableField<IThrow> whenNewThrowInputed;
	private ObservableField<Fossil> whenNewFossilInputed;

	public ThrowParser(IClipboardProvider clipboardProvider, NinjabrainBotPreferences preferences, IModificationLock modificationLock, IObservable<Float> boatAngle) {
		this.preferences = preferences;
		this.modificationLock = modificationLock;
		this.boatAngle = boatAngle;

		whenNewThrowInputed = new ObservableField<IThrow>(null, true);
		whenNewFossilInputed = new ObservableField<Fossil>(null, true);

		clipboardProvider.clipboardText().subscribe(clipboardString -> parseF3C(clipboardString));
	}

	public void parseF3C(String f3c) {
		if (f3c == null)
			return;

		IThrow t = null;
		if ((preferences.useTallRes.get() && preferences.usePreciseAngle.get() && boatAngle.get() != null)) {
			t = BoatThrow.parseF3C(f3c, preferences, modificationLock, boatAngle.get());
		} else {
			t = Throw.parseF3C(f3c, preferences.crosshairCorrection.get(), modificationLock);
		}
		if (t != null) {
			whenNewThrowInputed.set(t);
			return;
		}
		final IThrow t2 = Throw1_12.parseF3C(f3c, preferences.crosshairCorrection.get(), modificationLock);
		if (t2 != null) {
			whenNewThrowInputed.set(t);
			return;
		}
		final Fossil f = Fossil.parseF3I(f3c);
		if (f != null) {
			whenNewFossilInputed.set(f);
		}
	}

	public ISubscribable<IThrow> whenNewThrowInputed() {
		return whenNewThrowInputed;
	}

	public ISubscribable<Fossil> whenNewFossilInputed() {
		return whenNewFossilInputed;
	}

}
