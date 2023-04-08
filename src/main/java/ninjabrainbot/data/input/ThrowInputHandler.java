package ninjabrainbot.data.input;

import ninjabrainbot.data.IDataState;
import ninjabrainbot.data.actions.AddTriangulationThrowAction;
import ninjabrainbot.data.actions.IAction;
import ninjabrainbot.data.actions.IActionExecutor;
import ninjabrainbot.data.actions.SetAllAdvancementsStructurePositionAction;
import ninjabrainbot.data.actions.SetBoatAngleAction;
import ninjabrainbot.data.actions.SetPlayerPositionAction;
import ninjabrainbot.data.calculator.alladvancements.StructureType;
import ninjabrainbot.data.calculator.common.StructurePosition;
import ninjabrainbot.data.calculator.endereye.IThrow;
import ninjabrainbot.event.DisposeHandler;
import ninjabrainbot.event.IDisposable;
import ninjabrainbot.event.ISubscribable;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;

/**
 * Listens to the stream of throws and decides if/how the throws should be inputted into the data state.
 */
public class ThrowInputHandler implements IDisposable {

	private final IDataState dataState;
	private final IActionExecutor actionExecutor;
	private final NinjabrainBotPreferences preferences;

	DisposeHandler disposeHandler = new DisposeHandler();

	public ThrowInputHandler(ISubscribable<IThrow> throwSource, IDataState dataState, IActionExecutor actionExecutor, NinjabrainBotPreferences preferences) {
		this.dataState = dataState;
		this.actionExecutor = actionExecutor;
		this.preferences = preferences;
		disposeHandler.add(throwSource.subscribe(this::onNewThrow));
	}

	private void onNewThrow(IThrow t) {
		IAction setPlayerPositionAction = new SetPlayerPositionAction(dataState, t);
		IAction actionForNewThrow = getActionForThrow(t);
		if (actionForNewThrow == null) {
			actionExecutor.executeImmediately(setPlayerPositionAction);
			return;
		}
		actionExecutor.executeImmediately(setPlayerPositionAction, actionForNewThrow);
	}

	private IAction getActionForThrow(IThrow t) {
		if (dataState.locked().get())
			return null;

		if (t.isNether())
			return null;

		if (dataState.allAdvancementsDataState().allAdvancementsModeEnabled().get())
			return createSetAllAdvancementsStructurePositionAction(t);

		if (dataState.boatDataState().enteringBoat().get())
			return new SetBoatAngleAction(dataState.boatDataState(), t.rawAlpha(), preferences);

		if (t.lookingBelowHorizon())
			return null;

		return new AddTriangulationThrowAction(dataState, t);
	}

	private IAction createSetAllAdvancementsStructurePositionAction(IThrow t) {
		StructureType structureType = getAllAdvancementStructureTypeFromThrow(t);
		if (structureType == StructureType.Unknown)
			return null;
		StructurePosition structurePosition = structureType == StructureType.Outpost ? getOutpostPosition(t) : new StructurePosition((int) t.xInOverworld(), (int) t.zInOverworld(), dataState.playerPosition());
		return new SetAllAdvancementsStructurePositionAction(dataState.allAdvancementsDataState(), structureType, structurePosition);
	}

	private StructureType getAllAdvancementStructureTypeFromThrow(IThrow t) {
		if (t.isNether())
			return StructureType.Unknown;

		if (Math.abs(t.xInOverworld()) <= 300 && Math.abs(t.zInOverworld()) <= 300)
			return StructureType.Spawn;

		if (t.yInPlayerDimension() < 63)
			return StructureType.Monument;

		return StructureType.Outpost;
	}

	private StructurePosition getOutpostPosition(IThrow t) {
		int averageOutpostY = 80;
		double deltaY = averageOutpostY - t.yInPlayerDimension();
		double horizontalDistance = deltaY / Math.tan(-t.beta() * Math.PI / 180.0);
		double deltaX = horizontalDistance * Math.sin(-t.alpha() * Math.PI / 180.0);
		double deltaZ = horizontalDistance * Math.cos(t.alpha() * Math.PI / 180.0);
		deltaX = Math.max(Math.min(deltaX, 350), -350);
		deltaZ = Math.max(Math.min(deltaZ, 350), -350);
		return new StructurePosition((int) (t.xInOverworld() + deltaX), (int) (t.zInOverworld() + deltaZ), dataState.playerPosition());
	}

	@Override
	public void dispose() {
		disposeHandler.dispose();
	}

}
