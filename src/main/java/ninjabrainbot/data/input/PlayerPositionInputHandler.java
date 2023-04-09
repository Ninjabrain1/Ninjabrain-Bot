package ninjabrainbot.data.input;

import ninjabrainbot.data.IDataState;
import ninjabrainbot.data.actions.AddEnderEyeThrowAction;
import ninjabrainbot.data.actions.IAction;
import ninjabrainbot.data.actions.IActionExecutor;
import ninjabrainbot.data.actions.SetAllAdvancementsStructurePositionAction;
import ninjabrainbot.data.actions.SetBoatAngleAction;
import ninjabrainbot.data.actions.SetPlayerPositionAction;
import ninjabrainbot.data.calculator.alladvancements.StructureType;
import ninjabrainbot.data.calculator.common.ILimitedPlayerPosition;
import ninjabrainbot.data.calculator.common.IPlayerPosition;
import ninjabrainbot.data.calculator.common.StructurePosition;
import ninjabrainbot.data.calculator.endereye.IPlayerPositionInputSource;
import ninjabrainbot.event.DisposeHandler;
import ninjabrainbot.event.IDisposable;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;

/**
 * Listens to a stream of player position inputs and decides if/how the inputs should affect the data state.
 */
public class PlayerPositionInputHandler implements IDisposable {

	private final IDataState dataState;
	private final IActionExecutor actionExecutor;
	private final NinjabrainBotPreferences preferences;

	DisposeHandler disposeHandler = new DisposeHandler();

	public PlayerPositionInputHandler(IPlayerPositionInputSource playerPositionInputSource, IDataState dataState, IActionExecutor actionExecutor, NinjabrainBotPreferences preferences) {
		this.dataState = dataState;
		this.actionExecutor = actionExecutor;
		this.preferences = preferences;
		disposeHandler.add(playerPositionInputSource.whenNewPlayerPositionInputted().subscribe(this::onNewPlayerPositionInputted));
		disposeHandler.add(playerPositionInputSource.whenNewLimitedPlayerPositionInputted().subscribe(this::onNewLimitedPlayerPositionInputted));
	}

	private void onNewPlayerPositionInputted(IPlayerPosition playerPosition) {
		IAction setPlayerPositionAction = new SetPlayerPositionAction(dataState, playerPosition);
		IAction actionForNewThrow = getActionForInputtedPlayerPosition(playerPosition);
		if (actionForNewThrow == null) {
			actionExecutor.executeImmediately(setPlayerPositionAction);
			return;
		}
		actionExecutor.executeImmediately(setPlayerPositionAction, actionForNewThrow);
	}

	private void onNewLimitedPlayerPositionInputted(ILimitedPlayerPosition limitedPlayerPosition) {
		IAction setPlayerPositionAction = new SetPlayerPositionAction(dataState, limitedPlayerPosition);
		IAction actionForNewThrow = getActionForInputtedPlayerPosition(limitedPlayerPosition);
		if (actionForNewThrow == null) {
			actionExecutor.executeImmediately(setPlayerPositionAction);
			return;
		}
		actionExecutor.executeImmediately(setPlayerPositionAction, actionForNewThrow);
	}

	private IAction getActionForInputtedPlayerPosition(IPlayerPosition playerPosition) {
		if (dataState.locked().get())
			return null;

		if (!playerPosition.isInOverworld())
			return null;

		if (dataState.allAdvancementsDataState().allAdvancementsModeEnabled().get())
			return createSetAllAdvancementsStructurePositionAction(playerPosition);

		if (dataState.boatDataState().enteringBoat().get())
			return new SetBoatAngleAction(dataState.boatDataState(), playerPosition.horizontalAngle(), preferences);

		if (playerPosition.lookingBelowHorizon())
			return null;

		return new AddEnderEyeThrowAction(dataState, playerPosition);
	}

	private IAction createSetAllAdvancementsStructurePositionAction(IPlayerPosition playerPosition) {
		StructureType structureType = getAllAdvancementStructureTypeFromPlayerPosition(playerPosition);
		if (structureType == StructureType.Unknown)
			return null;
		StructurePosition structurePosition =
				structureType == StructureType.Outpost
						? getOutpostPosition(playerPosition)
						: new StructurePosition((int) Math.floor(playerPosition.xInOverworld()), (int) Math.floor(playerPosition.zInOverworld()), dataState.playerPosition());
		return new SetAllAdvancementsStructurePositionAction(dataState.allAdvancementsDataState(), structureType, structurePosition);
	}

	private StructureType getAllAdvancementStructureTypeFromPlayerPosition(IPlayerPosition t) {
		if (t.isNether())
			return StructureType.Unknown;

		if (Math.abs(t.xInOverworld()) <= 300 && Math.abs(t.zInOverworld()) <= 300)
			return StructureType.Spawn;

		if (t.yInPlayerDimension() < 63)
			return StructureType.Monument;

		return StructureType.Outpost;
	}

	private StructurePosition getOutpostPosition(IPlayerPosition t) {
		int averageOutpostY = 80;
		double deltaY = averageOutpostY - t.yInPlayerDimension();
		double horizontalDistance = deltaY / Math.tan(-t.beta() * Math.PI / 180.0);
		double deltaX = horizontalDistance * Math.sin(-t.horizontalAngle() * Math.PI / 180.0);
		double deltaZ = horizontalDistance * Math.cos(t.horizontalAngle() * Math.PI / 180.0);
		deltaX = Math.max(Math.min(deltaX, 350), -350);
		deltaZ = Math.max(Math.min(deltaZ, 350), -350);
		return new StructurePosition((int) (t.xInOverworld() + deltaX), (int) (t.zInOverworld() + deltaZ), dataState.playerPosition());
	}

	@Override
	public void dispose() {
		disposeHandler.dispose();
	}

}
