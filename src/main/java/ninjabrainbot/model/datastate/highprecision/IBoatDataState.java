package ninjabrainbot.model.datastate.highprecision;

import ninjabrainbot.model.domainmodel.IDataComponent;

public interface IBoatDataState {

	IDataComponent<Boolean> enteringBoat();

	IDataComponent<Float> boatAngle();

	IDataComponent<BoatState> boatState();

}
