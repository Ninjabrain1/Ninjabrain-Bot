package ninjabrainbot.util;

import ninjabrainbot.data.calculator.common.IDetailedPlayerPosition;
import ninjabrainbot.data.calculator.common.IPlayerPosition;
import ninjabrainbot.data.calculator.common.IPlayerPositionInputSource;
import ninjabrainbot.data.calculator.divine.Fossil;
import ninjabrainbot.data.input.IFossilInputSource;
import ninjabrainbot.event.ISubscribable;
import ninjabrainbot.event.ObservableProperty;

public class FakeCoordinateInputSource implements IPlayerPositionInputSource, IFossilInputSource {

	public final ObservableProperty<IDetailedPlayerPosition> whenNewDetailedPlayerPositionInputted;
	public final ObservableProperty<IPlayerPosition> whenNewLimitedPlayerPositionInputted;
	public final ObservableProperty<Fossil> whenNewFossilInputted;

	public FakeCoordinateInputSource() {
		whenNewDetailedPlayerPositionInputted = new ObservableProperty<>();
		whenNewLimitedPlayerPositionInputted = new ObservableProperty<>();
		whenNewFossilInputted = new ObservableProperty<>();
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
