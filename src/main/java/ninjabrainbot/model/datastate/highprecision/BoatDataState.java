package ninjabrainbot.model.datastate.highprecision;

import ninjabrainbot.io.preferences.enums.DefaultBoatType;
import ninjabrainbot.model.domainmodel.DataComponent;
import ninjabrainbot.model.domainmodel.IDataComponent;
import ninjabrainbot.model.domainmodel.IDomainModel;

public class BoatDataState implements IBoatDataState {

	private final DataComponent<Boolean> enteringBoat;
	private final DataComponent<Float> boatAngle;
	private final DataComponent<BoatState> boatState;

	public BoatDataState(IDomainModel domainModel) {
		this(domainModel, DefaultBoatType.GRAY);
	}

	public BoatDataState(IDomainModel domainModel, DefaultBoatType defaultBoatType) {
		switch (defaultBoatType) {
			case GREEN:
				enteringBoat = new DataComponent<>(domainModel, false);
				boatAngle = new DataComponent<>(domainModel, 0f);
				boatState = new DataComponent<>(domainModel, BoatState.VALID);
				break;
			case BLUE:
				enteringBoat = new DataComponent<>(domainModel, true);
				boatAngle = new DataComponent<>(domainModel);
				boatState = new DataComponent<>(domainModel, BoatState.MEASURING);
				break;
			default:
				enteringBoat = new DataComponent<>(domainModel, false);
				boatAngle = new DataComponent<>(domainModel);
				boatState = new DataComponent<>(domainModel, BoatState.NONE);
		}
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
