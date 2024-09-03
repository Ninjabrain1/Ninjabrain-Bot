package ninjabrainbot.model.datastate.endereye;

import ninjabrainbot.event.DisposeHandler;
import ninjabrainbot.event.IDisposable;
import ninjabrainbot.event.ISubscribable;
import ninjabrainbot.event.ObservableField;
import ninjabrainbot.io.IClipboardProvider;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.model.datastate.common.*;
import ninjabrainbot.model.datastate.divine.Fossil;
import ninjabrainbot.model.input.IFossilInputSource;
import ninjabrainbot.model.input.IGeneralLocationInputSource;

/**
 * Listens changes of the clipboard in the ClipboardProvider and parses any compatible clipboard strings
 * into player positions and fossils, exposed through the streams whenNewPlayerPositionInputted(), and whenNewFossilInputted().
 */
public class CoordinateInputSource implements IPlayerPositionInputSource, IFossilInputSource, IGeneralLocationInputSource, IDisposable {

	private final NinjabrainBotPreferences preferences;
	private final ObservableField<IDetailedPlayerPosition> whenNewDetailedPlayerPositionInputted;
	private final ObservableField<ILimitedPlayerPosition> whenNewLimitedPlayerPositionInputted;
	private final ObservableField<Fossil> whenNewFossilInputted;
	private final ObservableField<StructurePosition> whenNewGeneralLocationInputted;

	private final DisposeHandler disposeHandler = new DisposeHandler();

	public CoordinateInputSource(IClipboardProvider clipboardProvider, NinjabrainBotPreferences preferences) {
		whenNewDetailedPlayerPositionInputted = new ObservableField<>(null, true);
		whenNewLimitedPlayerPositionInputted = new ObservableField<>(null, true);
		whenNewFossilInputted = new ObservableField<>(null, true);
		whenNewGeneralLocationInputted = new ObservableField<>(null, true);

		disposeHandler.add(clipboardProvider.clipboardText().subscribe(this::parseF3C));
		this.preferences = preferences;
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

		if (preferences.oneDotTwentyPlusAA.get() && preferences.allAdvancements.get()) {
			StructurePosition generalLocation = StructurePosition.tryParseGeneralLocation(f3c);
			if (generalLocation != null) {
				whenNewGeneralLocationInputted.setAndAlwaysNotifySubscribers(generalLocation);
			}
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

	public ISubscribable<StructurePosition> whenNewGeneralLocationInputted() {
		return whenNewGeneralLocationInputted;
	}

	@Override
	public void dispose() {
		disposeHandler.dispose();
	}
}
