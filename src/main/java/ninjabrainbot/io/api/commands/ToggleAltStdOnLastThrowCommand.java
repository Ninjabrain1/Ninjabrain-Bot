package ninjabrainbot.io.api.commands;

import ninjabrainbot.io.api.interfaces.IParameterlessCommand;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.model.actions.IAction;
import ninjabrainbot.model.actions.boat.ToggleEnteringBoatAction;
import ninjabrainbot.model.actions.boat.ToggleMod360IndicatorAction;
import ninjabrainbot.model.actions.endereye.ChangeLastAngleAction;
import ninjabrainbot.model.actions.endereye.ToggleAltStdOnLastThrowAction;
import ninjabrainbot.model.datastate.IDataState;
import ninjabrainbot.model.domainmodel.IDomainModel;

public class ToggleAltStdOnLastThrowCommand implements IParameterlessCommand {

	private final NinjabrainBotPreferences ninjabrainBotPreferences;

	public ToggleAltStdOnLastThrowCommand(NinjabrainBotPreferences ninjabrainBotPreferences) {
		this.ninjabrainBotPreferences = ninjabrainBotPreferences;
	}

	@Override
	public String name() {
		return "toggle_alt_std";
	}

	@Override
	public String summary() {
		return "Toggles standard deviation on the last eye measurement.";
	}

	@Override
	public String description() {
		return "Toggles standard deviation on the last eye measurement similar to the 'Alt. std on last angle' hotkey. " +
			   "Will do nothing if the calculator is locked or currently in all advancements mode.";
	}

	@Override
	public IAction[] mapToActions(IDomainModel domainModel, IDataState dataState) {
		if (!dataState.locked().get() && !dataState.allAdvancementsDataState().allAdvancementsModeEnabled().get())
			return new IAction[] { new ToggleAltStdOnLastThrowAction(dataState, ninjabrainBotPreferences) };

		return new IAction[0];
	}

}