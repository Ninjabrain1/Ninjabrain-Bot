package ninjabrainbot.model.input;

import java.util.Optional;

import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.model.actions.IAction;
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
import ninjabrainbot.model.datastate.endereye.IEnderEyeThrow;
import ninjabrainbot.model.datastate.endereye.IEnderEyeThrowFactory;

public class InputtedPlayerPositionToActionMapper implements IInputtedPlayerPositionToActionMapper {

	private final IDataState dataState;
	private final NinjabrainBotPreferences preferences;
	private final IEnderEyeThrowFactory enderEyeThrowFactory;

	public InputtedPlayerPositionToActionMapper(IDataState dataState, NinjabrainBotPreferences preferences, IEnderEyeThrowFactory enderEyeThrowFactory) {
		this.dataState = dataState;
		this.preferences = preferences;
		this.enderEyeThrowFactory = enderEyeThrowFactory;
	}

	public IAction[] getActionsForInputtedPlayerPosition(IDetailedPlayerPosition detailedPlayerPosition) {
		IAction setPlayerPositionAction = new SetPlayerPositionAction(dataState, detailedPlayerPosition);
		Optional<IAction> action = getActionForInputtedPlayerPosition(detailedPlayerPosition);
		return action.isPresent()
				? new IAction[] { setPlayerPositionAction, action.get() }
				: new IAction[] { setPlayerPositionAction };
	}

	public IAction[] getActionsForInputtedLimitedPlayerPosition(ILimitedPlayerPosition limitedPlayerPosition) {
		IAction setPlayerPositionAction = new SetPlayerPositionAction(dataState, limitedPlayerPosition);
		Optional<IAction> action = getActionForInputtedLimitedPlayerPosition(limitedPlayerPosition);
		return action.isPresent()
				? new IAction[] { setPlayerPositionAction, action.get() }
				: new IAction[] { setPlayerPositionAction };
	}

	private Optional<IAction> getActionForInputtedPlayerPosition(IDetailedPlayerPosition playerPosition) {
		if (dataState.locked().get())
			return Optional.empty();

		if (dataState.allAdvancementsDataState().allAdvancementsModeEnabled().get())
			return Optional.of(new TryAddAllAdvancementsStructureAction(dataState, playerPosition, preferences));

		if (preferences.usePreciseAngle.get() && dataState.boatDataState().enteringBoat().get())
			return Optional.of(new SetBoatAngleAction(dataState.boatDataState(), playerPosition.horizontalAngle(), preferences));

		if (preferences.usePreciseAngle.get() && dataState.boatDataState().reducingModulo360().get())
			return Optional.of(new ReduceBoatAngleMod360Action(dataState.boatDataState(), playerPosition.horizontalAngle(), preferences.sensitivityAutomatic.get()));

		if (!playerPosition.isInOverworld())
			return Optional.empty();

		if (playerPosition.lookingBelowHorizon())
			return Optional.empty();

		IEnderEyeThrow enderEyeThrowToAdd = enderEyeThrowFactory.createEnderEyeThrowFromDetailedPlayerPosition(playerPosition);
		if (shouldSkipAddingThrow(enderEyeThrowToAdd))
			return Optional.empty();

		return Optional.of(new AddEnderEyeThrowAction(dataState, enderEyeThrowToAdd));
	}

	private Optional<IAction> getActionForInputtedLimitedPlayerPosition(ILimitedPlayerPosition playerPosition) {
		if (dataState.locked().get())
			return Optional.empty();

		if (!playerPosition.isInOverworld())
			return Optional.empty();

		IEnderEyeThrow enderEyeThrowToAdd = enderEyeThrowFactory.createEnderEyeThrowFromLimitedPlayerPosition(playerPosition);
		if (shouldSkipAddingThrow(enderEyeThrowToAdd))
			return Optional.empty();

		IAction action = new AddEnderEyeThrowAction(dataState, enderEyeThrowToAdd);

		if (playerPosition.correctionIncrements() != 0)
			action = new JointAction(action, new ChangeLastAngleAction(dataState, preferences, playerPosition.correctionIncrements()));

		return Optional.of(action);
	}

	private boolean shouldSkipAddingThrow(IEnderEyeThrow enderEyeThrow) {
		if (dataState.getThrowList().size() == 0)
			return false;

		IEnderEyeThrow lastThrow = dataState.getThrowList().getLast();
		return lastThrow.xInOverworld() == enderEyeThrow.xInOverworld() &&
			   lastThrow.zInOverworld() == enderEyeThrow.zInOverworld() &&
			   lastThrow.horizontalAngleWithoutCorrection() == enderEyeThrow.horizontalAngleWithoutCorrection();
	}

}
