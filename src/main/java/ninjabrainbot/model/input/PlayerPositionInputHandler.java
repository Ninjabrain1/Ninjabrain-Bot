package ninjabrainbot.model.input;

import ninjabrainbot.event.DisposeHandler;
import ninjabrainbot.event.IDisposable;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.model.actions.IAction;
import ninjabrainbot.model.actions.IActionExecutor;
import ninjabrainbot.model.actions.alladvancements.TryAddAllAdvancementsStructureAction;
import ninjabrainbot.model.actions.boat.ReduceBoatAngleMod360Action;
import ninjabrainbot.model.actions.boat.SetBoatAngleAction;
import ninjabrainbot.model.actions.common.SetPlayerPositionAction;
import ninjabrainbot.model.actions.endereye.AddEnderEyeThrowAction;
import ninjabrainbot.model.actions.endereye.ChangeLastAngleAction;
import ninjabrainbot.model.actions.util.JointAction;
import ninjabrainbot.model.datastate.IDataState;
import ninjabrainbot.model.datastate.common.IDetailedPlayerPosition;
import ninjabrainbot.model.datastate.common.ILimitedPlayerPosition;
import ninjabrainbot.model.datastate.common.IPlayerPositionInputSource;
import ninjabrainbot.model.datastate.endereye.IEnderEyeThrowFactory;

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

		if (preferences.usePreciseAngle.get() && dataState.boatDataState().enteringBoat().get())
			return new SetBoatAngleAction(dataState.boatDataState(), playerPosition.horizontalAngle(), preferences);

		if (preferences.usePreciseAngle.get() && dataState.boatDataState().reducingModulo360().get())
			return new ReduceBoatAngleMod360Action(dataState.boatDataState(), playerPosition.horizontalAngle(), preferences.sensitivityAutomatic.get());

		if (!playerPosition.isInOverworld())
			return null;

		if (dataState.allAdvancementsDataState().allAdvancementsModeEnabled().get())
			return new TryAddAllAdvancementsStructureAction(dataState, playerPosition);

		if (playerPosition.lookingBelowHorizon())
			return null;

		return new AddEnderEyeThrowAction(dataState, enderEyeThrowFactory.createEnderEyeThrowFromDetailedPlayerPosition(playerPosition));
	}

	private void onNewLimitedPlayerPositionInputted(ILimitedPlayerPosition playerPosition) {
		IAction setPlayerPositionAction = new SetPlayerPositionAction(dataState, playerPosition);
		IAction actionForNewThrow = getActionForInputtedLimitedPlayerPosition(playerPosition);
		if (actionForNewThrow == null) {
			actionExecutor.executeImmediately(setPlayerPositionAction);
			return;
		}
		actionExecutor.executeImmediately(setPlayerPositionAction, actionForNewThrow);
	}

	private IAction getActionForInputtedLimitedPlayerPosition(ILimitedPlayerPosition playerPosition) {
		if (dataState.locked().get())
			return null;

		if (!playerPosition.isInOverworld())
			return null;

		IAction action = new AddEnderEyeThrowAction(dataState, enderEyeThrowFactory.createEnderEyeThrowFromLimitedPlayerPosition(playerPosition));

		if (playerPosition.correctionIncrements() != 0)
			action = new JointAction(action, new ChangeLastAngleAction(dataState, preferences, playerPosition.correctionIncrements()));

		return action;
	}

	@Override
	public void dispose() {
		disposeHandler.dispose();
	}

}
