package ninjabrainbot.model.datastate.highprecision;

import ninjabrainbot.model.domainmodel.IDataComponent;

public interface IBoatDataState {

	IDataComponent<Boolean> enteringBoat();

	IDataComponent<Boolean> reducingModulo360();

	IDataComponent<Float> boatAngle();

	IDataComponent<BoatState> boatState();

}
