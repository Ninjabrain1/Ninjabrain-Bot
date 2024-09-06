package ninjabrainbot.model.datastate.highprecision;

import ninjabrainbot.io.preferences.enums.DefaultBoatType;
import ninjabrainbot.model.domainmodel.DataComponent;
import ninjabrainbot.model.domainmodel.IDataComponent;
import ninjabrainbot.model.domainmodel.IDomainModel;

public class BoatDataState implements IBoatDataState {

	private final DataComponent<Boolean> enteringBoat;
	private final DataComponent<Boolean> reducingModulo360;
	private final DataComponent<Float> boatAngle;
	private final DataComponent<BoatState> boatState;

	public BoatDataState(IDomainModel domainModel) {
		this(domainModel, DefaultBoatType.GRAY);
	}

	public BoatDataState(IDomainModel domainModel, DefaultBoatType defaultBoatType) {
		reducingModulo360 = new DataComponent<>("boat_reducing_mod_360", domainModel, false);

		switch (defaultBoatType) {
			case GREEN:
				enteringBoat = new DataComponent<>("boat_entering", domainModel, false);
				boatAngle = new DataComponent<>("boat_angle", domainModel, 0f);
				boatState = new DataComponent<>("boat_state", domainModel, BoatState.VALID);
				break;
			case BLUE:
				enteringBoat = new DataComponent<>("boat_entering", domainModel, true);
				boatAngle = new DataComponent<>("boat_angle", domainModel);
				boatState = new DataComponent<>("boat_state", domainModel, BoatState.MEASURING);
				break;
			default:
				enteringBoat = new DataComponent<>("boat_entering", domainModel, false);
				boatAngle = new DataComponent<>("boat_angle", domainModel);
				boatState = new DataComponent<>("boat_state", domainModel, BoatState.NONE);
		}
	}

	@Override
	public IDataComponent<Boolean> enteringBoat() {
		return enteringBoat;
	}

	@Override
	public IDataComponent<Boolean> reducingModulo360() {
		return reducingModulo360;
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
