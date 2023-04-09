package ninjabrainbot.data.calculator.endereye;

import ninjabrainbot.data.calculator.common.DetailedPlayerPosition;
import ninjabrainbot.data.calculator.common.IDetailedPlayerPosition;
import ninjabrainbot.data.calculator.common.IPlayerPosition;
import ninjabrainbot.data.calculator.common.LimitedPlayerPosition;
import ninjabrainbot.data.calculator.divine.Fossil;
import ninjabrainbot.data.input.IFossilInputSource;
import ninjabrainbot.event.ISubscribable;
import ninjabrainbot.event.ObservableField;
import ninjabrainbot.io.IClipboardProvider;

/**
 * Listens changes of the clipboard in the ClipboardProvider and parses any compatible clipboard strings
 * into player positions and fossils, exposed through the streams whenNewPlayerPositionInputted(), and whenNewFossilInputted().
 */
public class CoordinateInputSource implements IPlayerPositionInputSource, IFossilInputSource {

	private final ObservableField<IDetailedPlayerPosition> whenNewDetailedPlayerPositionInputted;
	private final ObservableField<IPlayerPosition> whenNewLimitedPlayerPositionInputted;
	private final ObservableField<Fossil> whenNewFossilInputted;

	public CoordinateInputSource(IClipboardProvider clipboardProvider) {
		whenNewDetailedPlayerPositionInputted = new ObservableField<>(null, true);
		whenNewLimitedPlayerPositionInputted = new ObservableField<>(null, true);
		whenNewFossilInputted = new ObservableField<>(null, true);

		clipboardProvider.clipboardText().subscribe(this::parseF3C);
	}

	private void parseF3C(String f3c) {
		if (f3c == null)
			return;

		F3CData f3cData = F3CData.tryParseF3CString(f3c);
		if (f3cData != null) {
			whenNewDetailedPlayerPositionInputted.set(new DetailedPlayerPosition(f3cData.x, f3cData.y, f3cData.z, f3cData.horizontalAngle, f3cData.verticalAngle, f3cData.nether));
			return;
		}

		InputData1_12 data1_12 = InputData1_12.parseInputString(f3c);
		if (data1_12 != null) {
			whenNewLimitedPlayerPositionInputted.set(new LimitedPlayerPosition(data1_12.x, data1_12.z, data1_12.horizontalAngle));
			return;
		}

		final Fossil f = Fossil.parseF3I(f3c);
		if (f != null) {
			whenNewFossilInputted.set(f);
		}
	}

	public ISubscribable<IDetailedPlayerPosition> whenNewDetailedPlayerPositionInputted() {
		return whenNewDetailedPlayerPositionInputted;
	}

	public ISubscribable<IPlayerPosition> whenNewLimitedPlayerPositionInputted() {
		return whenNewLimitedPlayerPositionInputted;
	}

	public ISubscribable<Fossil> whenNewFossilInputted() {
		return whenNewFossilInputted;
	}

}
