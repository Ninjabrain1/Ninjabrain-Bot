package ninjabrainbot.data.calculator.alladvancements;

import ninjabrainbot.data.calculator.common.StructurePosition;
import ninjabrainbot.data.temp.IDataComponent;

public interface IAllAdvancementsDataState {

	IDataComponent<Boolean> allAdvancementsModeEnabled();

	IDataComponent<StructurePosition> strongholdPosition();

	IDataComponent<StructurePosition> spawnPosition();

	IDataComponent<StructurePosition> outpostPosition();

	IDataComponent<StructurePosition> monumentPosition();

}
