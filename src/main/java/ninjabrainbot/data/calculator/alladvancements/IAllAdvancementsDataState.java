package ninjabrainbot.data.calculator.alladvancements;

import ninjabrainbot.data.calculator.common.StructurePosition;
import ninjabrainbot.event.IObservable;

public interface IAllAdvancementsDataState {

	IObservable<Boolean> allAdvancementsModeEnabled();

	IObservable<StructurePosition> strongholdPosition();

	IObservable<StructurePosition> spawnPosition();

	IObservable<StructurePosition> outpostPosition();

	IObservable<StructurePosition> monumentPosition();

}
