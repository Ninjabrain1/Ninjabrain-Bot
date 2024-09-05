package ninjabrainbot.util;

import ninjabrainbot.event.ISubscribable;
import ninjabrainbot.event.ObservableProperty;
import ninjabrainbot.model.datastate.common.IDetailedPlayerPosition;
import ninjabrainbot.model.datastate.common.ILimitedPlayerPosition;
import ninjabrainbot.model.datastate.common.IPlayerPositionInputSource;
import ninjabrainbot.model.datastate.endereye.F3IData;
import ninjabrainbot.model.input.IF3ILocationInputSource;

public class FakeCoordinateInputSource implements IPlayerPositionInputSource, IF3ILocationInputSource {

	public final ObservableProperty<IDetailedPlayerPosition> whenNewDetailedPlayerPositionInputted;
	public final ObservableProperty<ILimitedPlayerPosition> whenNewLimitedPlayerPositionInputted;
	public final ObservableProperty<F3IData> whenNewF3IInputted;

	public FakeCoordinateInputSource() {
		whenNewDetailedPlayerPositionInputted = new ObservableProperty<>();
		whenNewLimitedPlayerPositionInputted = new ObservableProperty<>();
		whenNewF3IInputted = new ObservableProperty<>();
	}

	public ISubscribable<IDetailedPlayerPosition> whenNewDetailedPlayerPositionInputted() {
		return whenNewDetailedPlayerPositionInputted;
	}

	public ISubscribable<ILimitedPlayerPosition> whenNewLimitedPlayerPositionInputted() {
		return whenNewLimitedPlayerPositionInputted;
	}

	public ISubscribable<F3IData> whenNewF3ILocationInputted() {
		return whenNewF3IInputted;
	}
}
