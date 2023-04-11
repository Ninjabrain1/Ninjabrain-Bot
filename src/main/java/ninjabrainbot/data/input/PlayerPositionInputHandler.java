package ninjabrainbot.data.input;

import ninjabrainbot.data.IDataState;
import ninjabrainbot.data.actions.AddEnderEyeThrowAction;
import ninjabrainbot.data.actions.IAction;
import ninjabrainbot.data.actions.IActionExecutor;
import ninjabrainbot.data.actions.SetBoatAngleAction;
import ninjabrainbot.data.actions.SetPlayerPositionAction;
import ninjabrainbot.data.actions.TryAddAllAdvancementsStructureAction;
import ninjabrainbot.data.calculator.common.IDetailedPlayerPosition;
import ninjabrainbot.data.calculator.common.IPlayerPosition;
import ninjabrainbot.data.calculator.common.IPlayerPositionInputSource;
import ninjabrainbot.data.calculator.endereye.IEnderEyeThrowFactory;
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
	private final IEnderEyeThrowFactory enderEyeThrowFactory;

	final DisposeHandler disposeHandler = new DisposeHandler();

	public PlayerPositionInputHandler(IPlayerPositionInputSource playerPositionInputSource, IDataState dataState, IActionExecutor actionExecutor, NinjabrainBotPreferences preferences, IEnderEyeThrowFactory enderEyeThrowFactory) {
		this.dataState = dataState;
		this.actionExecutor = actionExecutor;
		this.preferences = preferences;
		this.enderEyeThrowFactory = enderEyeThrowFactory;
		disposeHandler.add(playerPositionInputSource.whenNewDetailedPlayerPositionInputted().subscribe(this::onNewDetailedPlayerPositionInputted));
		disposeHandler.add(playerPositionInputSource.whenNewLimitedPlayerPositionInputted().subscribe(this::onNewLimitedPlayerPositionInputted));
	}

	private void onNewDetailedPlayerPositionInputted(IDetailedPlayerPosition detailedPlayerPosition) {
		IAction setPlayerPositionAction = new SetPlayerPositionAction(dataState, detailedPlayerPosition);
		IAction actionForNewThrow = getActionForInputtedPlayerPosition(detailedPlayerPosition);
		if (actionForNewThrow == null) {
			actionExecutor.executeImmediately(setPlayerPositionAction);
			return;
		}
		actionExecutor.executeImmediately(setPlayerPositionAction, actionForNewThrow);
	}

	private IAction getActionForInputtedPlayerPosition(IDetailedPlayerPosition playerPosition) {
		if (dataState.locked().get())
			return null;

		if (!playerPosition.isInOverworld())
			return null;

		if (dataState.allAdvancementsDataState().allAdvancementsModeEnabled().get())
			return new TryAddAllAdvancementsStructureAction(dataState, playerPosition);

		if (dataState.boatDataState().enteringBoat().get())
			return new SetBoatAngleAction(dataState.boatDataState(), playerPosition.horizontalAngle(), preferences);

		if (playerPosition.lookingBelowHorizon())
			return null;

		return new AddEnderEyeThrowAction(dataState, enderEyeThrowFactory.createEnderEyeThrowFromDetailedPlayerPosition(playerPosition));
	}

	private void onNewLimitedPlayerPositionInputted(IPlayerPosition playerPosition) {
		IAction setPlayerPositionAction = new SetPlayerPositionAction(dataState, playerPosition);
		IAction actionForNewThrow = getActionForInputtedLimitedPlayerPosition(playerPosition);
		if (actionForNewThrow == null) {
			actionExecutor.executeImmediately(setPlayerPositionAction);
			return;
		}
		actionExecutor.executeImmediately(setPlayerPositionAction, actionForNewThrow);
	}

	private IAction getActionForInputtedLimitedPlayerPosition(IPlayerPosition playerPosition) {
		if (dataState.locked().get())
			return null;

		if (!playerPosition.isInOverworld())
			return null;

		return new AddEnderEyeThrowAction(dataState, enderEyeThrowFactory.createEnderEyeThrowFromLimitedPlayerPosition(playerPosition));
	}

	@Override
	public void dispose() {
		disposeHandler.dispose();
	}

}
