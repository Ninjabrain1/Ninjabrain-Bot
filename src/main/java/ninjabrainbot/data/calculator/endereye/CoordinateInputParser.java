package ninjabrainbot.data.calculator.endereye;

import ninjabrainbot.data.calculator.common.ILimitedPlayerPosition;
import ninjabrainbot.data.calculator.common.IOverworldPosition;
import ninjabrainbot.data.calculator.common.IPlayerPosition;
import ninjabrainbot.data.calculator.common.PlayerPosition;
import ninjabrainbot.data.calculator.divine.Fossil;
import ninjabrainbot.data.input.IFossilInputSource;
import ninjabrainbot.event.IObservable;
import ninjabrainbot.event.ISubscribable;
import ninjabrainbot.event.ObservableField;
import ninjabrainbot.io.IClipboardProvider;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;

/**
 * Listens changes of the clipboard in the ClipboardProvider and parses any compatible clipboard strings
 * into player positions and fossils, exposed through the streams whenNewPlayerPositionInputted(), and whenNewFossilInputted().
 */
public class CoordinateInputParser implements IPlayerPositionInputSource, IFossilInputSource {

	private final NinjabrainBotPreferences preferences;
	private final IStdProfile stdProfile;
	private final IObservable<Float> boatAngle;

	private final ObservableField<IPlayerPosition> whenNewPlayerPositionInputted;
	private final ObservableField<ILimitedPlayerPosition> whenNewLimitedPlayerPositionInputted;
	private final ObservableField<Fossil> whenNewFossilInputted;

	public CoordinateInputParser(IClipboardProvider clipboardProvider, NinjabrainBotPreferences preferences, IStdProfile stdProfile, IObservable<Float> boatAngle) {
		this.preferences = preferences;
		this.stdProfile = stdProfile;
		this.boatAngle = boatAngle;

		whenNewPlayerPositionInputted = new ObservableField<>(null, true);
		whenNewLimitedPlayerPositionInputted = new ObservableField<>(null, true);
		whenNewFossilInputted = new ObservableField<>(null, true);

		clipboardProvider.clipboardText().subscribe(this::parseF3C);
	}

	private void parseF3C(String f3c) {
		if (f3c == null)
			return;

		F3CData f3cData = F3CData.tryParseF3CString(f3c);
		if (f3cData != null) {
			whenNewPlayerPositionInputted.set(new PlayerPosition(f3cData.x, f3cData.y, f3cData.z, f3cData.horizontalAngle, f3cData.verticalAngle, f3cData.nether));
			return;
		}

		InputData1_12 data1_12 = InputData1_12.parseInputString(f3c);
		if (data1_12 != null) {
			whenNewPlayerPositionInputted.set(new PlayerPosition(data1_12.x, 80, data1_12.z, data1_12.horizontalAngle, -31, false));
			return;
		}

		final Fossil f = Fossil.parseF3I(f3c);
		if (f != null) {
			whenNewFossilInputted.set(f);
		}
	}

	public ISubscribable<IPlayerPosition> whenNewPlayerPositionInputted() {
		return whenNewPlayerPositionInputted;
	}

	public ISubscribable<ILimitedPlayerPosition> whenNewLimitedPlayerPositionInputted() {
		return whenNewLimitedPlayerPositionInputted;
	}

	public ISubscribable<Fossil> whenNewFossilInputted() {
		return whenNewFossilInputted;
	}

}
