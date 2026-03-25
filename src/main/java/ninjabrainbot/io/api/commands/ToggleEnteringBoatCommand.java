package ninjabrainbot.io.api.commands;

import ninjabrainbot.io.api.interfaces.IParameterlessCommand;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.model.actions.IAction;
import ninjabrainbot.model.actions.boat.ToggleEnteringBoatAction;
import ninjabrainbot.model.actions.boat.ToggleMod360IndicatorAction;
import ninjabrainbot.model.datastate.IDataState;
import ninjabrainbot.model.domainmodel.IDomainModel;

public class ToggleEnteringBoatCommand implements IParameterlessCommand {

	private final NinjabrainBotPreferences ninjabrainBotPreferences;

	public ToggleEnteringBoatCommand(NinjabrainBotPreferences ninjabrainBotPreferences) {
		this.ninjabrainBotPreferences = ninjabrainBotPreferences;
	}

	@Override
	public String name() {
		return "toggle_boat";
	}

	@Override
	public String summary() {
		return "Toggles the boat state.";
	}

	@Override
	public String description() {
		return "Toggles the boat state similar to the 'Indicate boat angle reset on next F3+C' hotkey. " +
			   "Will do nothing if boat measurements are disabled in the settings, the calculator is locked, or if the calculator is currently in all advancements mode.";
	}

	@Override
	public IAction[] mapToActions(IDomainModel domainModel, IDataState dataState) {
		if (ninjabrainBotPreferences.usePreciseAngle.get() && !dataState.locked().get() && !dataState.allAdvancementsDataState().allAdvancementsModeEnabled().get()) {
			if (dataState.boatDataState().reducingModulo360().get())
				return new IAction[] { new ToggleMod360IndicatorAction(dataState), new ToggleEnteringBoatAction(dataState) };
			else
				return new IAction[] { new ToggleEnteringBoatAction(dataState) };
		}
		return new IAction[0];
	}

}