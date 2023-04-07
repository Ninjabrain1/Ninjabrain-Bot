package ninjabrainbot.data.calculator.highprecision;

import ninjabrainbot.data.temp.DataComponent;
import ninjabrainbot.data.temp.IDataComponent;
import ninjabrainbot.data.temp.IDomainModel;

public class BoatDataState implements IBoatDataState {

	private final DataComponent<Boolean> enteringBoat;
	private final DataComponent<Float> boatAngle;
	private final DataComponent<BoatState> boatState;

	public BoatDataState(IDomainModel domainModel) {
		enteringBoat = new DataComponent<>(domainModel, false);
		boatAngle = new DataComponent<>(domainModel);
		boatState = new DataComponent<>(domainModel, BoatState.NONE);
	}

	@Override
	public IDataComponent<Boolean> enteringBoat() {
		return enteringBoat;
	}

	@Override
	public IDataComponent<Float> boatAngle() {
		return boatAngle;
	}

	@Override
	public IDataComponent<BoatState> boatState() {
		return boatState;
	}

}
