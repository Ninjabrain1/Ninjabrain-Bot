package ninjabrainbot.model.datastate.endereye;

import ninjabrainbot.event.DisposeHandler;
import ninjabrainbot.event.IDisposable;
import ninjabrainbot.event.ISubscribable;
import ninjabrainbot.event.ObservableField;
import ninjabrainbot.io.IClipboardProvider;
import ninjabrainbot.model.datastate.common.*;
import ninjabrainbot.model.datastate.divine.Fossil;
import ninjabrainbot.model.input.IFossilInputSource;
import ninjabrainbot.model.input.IF3ILocationInputSource;

/**
 * Listens changes of the clipboard in the ClipboardProvider and parses any compatible clipboard strings
 * into player positions and fossils, exposed through the streams whenNewPlayerPositionInputted(), and whenNewFossilInputted().
 */
public class CoordinateInputSource implements IPlayerPositionInputSource, IFossilInputSource, IF3ILocationInputSource, IDisposable {

	private final ObservableField<IDetailedPlayerPosition> whenNewDetailedPlayerPositionInputted;
	private final ObservableField<ILimitedPlayerPosition> whenNewLimitedPlayerPositionInputted;
	private final ObservableField<Fossil> whenNewFossilInputted;
	private final ObservableField<F3IData> whenNewF3ILocationInputted;

	private final DisposeHandler disposeHandler = new DisposeHandler();

	public CoordinateInputSource(IClipboardProvider clipboardProvider) {
		whenNewDetailedPlayerPositionInputted = new ObservableField<>(null, true);
		whenNewLimitedPlayerPositionInputted = new ObservableField<>(null, true);
		whenNewFossilInputted = new ObservableField<>(null, true);
		whenNewF3ILocationInputted = new ObservableField<>(null, true);

		disposeHandler.add(clipboardProvider.clipboardText().subscribe(this::parseF3C));
	}

	private void parseF3C(String f3c) {
		if (f3c == null)
			return;

		F3CData f3cData = F3CData.tryParseF3CString(f3c);
		if (f3cData != null) {
			whenNewDetailedPlayerPositionInputted.set(new DetailedPlayerPosition(f3cData.x, f3cData.y, f3cData.z, f3cData.horizontalAngle, f3cData.verticalAngle, f3cData.dimension));
			return;
		}

		InputData1_12 data1_12 = InputData1_12.parseInputString(f3c);
		if (data1_12 != null) {
			whenNewLimitedPlayerPositionInputted.set(new LimitedPlayerPosition(data1_12.x, data1_12.z, data1_12.horizontalAngle, data1_12.correctionIncrements));
			return;
		}

		final Fossil f = Fossil.parseF3I(f3c);
		if (f != null) {
			whenNewFossilInputted.setAndAlwaysNotifySubscribers(f);
		}

		F3IData f3iData = F3IData.tryParseF3IString(f3c);
		if (f3iData != null) {
			whenNewF3ILocationInputted.setAndAlwaysNotifySubscribers(f3iData);
		}
	}

	public ISubscribable<IDetailedPlayerPosition> whenNewDetailedPlayerPositionInputted() {
		return whenNewDetailedPlayerPositionInputted;
	}

	public ISubscribable<ILimitedPlayerPosition> whenNewLimitedPlayerPositionInputted() {
		return whenNewLimitedPlayerPositionInputted;
	}

	public ISubscribable<Fossil> whenNewFossilInputted() {
		return whenNewFossilInputted;
	}

	public ISubscribable<F3IData> whenNewF3ILocationInputted() {
		return whenNewF3ILocationInputted;
	}

	@Override
	public void dispose() {
		disposeHandler.dispose();
	}
}
