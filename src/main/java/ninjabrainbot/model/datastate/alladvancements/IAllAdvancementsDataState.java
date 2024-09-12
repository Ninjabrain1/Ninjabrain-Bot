package ninjabrainbot.model.datastate.alladvancements;

import java.util.Iterator;

import ninjabrainbot.model.datastate.common.StructureInformation;
import ninjabrainbot.model.domainmodel.IDataComponent;
import ninjabrainbot.model.domainmodel.IDomainModelComponent;

public interface IAllAdvancementsDataState {

	IDomainModelComponent<Boolean> allAdvancementsModeEnabled();

	IDataComponent<Boolean> hasEnteredEnd();

	/**
	 * Returns the IDataComponent corresponding to the given structure type. Throws IllegalArgumentException if
	 * allAdvancementsStructureType equals Stronghold.
	 */
	IDataComponent<IAllAdvancementsPosition> getAllAdvancementsPosition(AllAdvancementsStructureType allAdvancementsStructureType);

	/**
	 * Returns the IDomainModelComponent corresponding to the given structure type.
	 */
	IDomainModelComponent<StructureInformation> getStructureInformation(AllAdvancementsStructureType allAdvancementsStructureType);

}
