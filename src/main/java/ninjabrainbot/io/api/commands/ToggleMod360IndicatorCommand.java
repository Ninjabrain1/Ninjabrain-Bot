package ninjabrainbot.io.api.commands;

import ninjabrainbot.io.api.interfaces.IParameterlessCommand;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.model.actions.IAction;
import ninjabrainbot.model.actions.boat.ToggleEnteringBoatAction;
import ninjabrainbot.model.actions.boat.ToggleMod360IndicatorAction;
import ninjabrainbot.model.datastate.IDataState;
import ninjabrainbot.model.datastate.highprecision.BoatState;
import ninjabrainbot.model.domainmodel.IDomainModel;

public class ToggleMod360IndicatorCommand implements IParameterlessCommand {

	private final NinjabrainBotPreferences ninjabrainBotPreferences;

	public ToggleMod360IndicatorCommand(NinjabrainBotPreferences ninjabrainBotPreferences) {
		this.ninjabrainBotPreferences = ninjabrainBotPreferences;
	}

	@Override
	public String name() {
		return "toggle_mod360";
	}

	@Override
	public String summary() {
		return "Toggles the mod360 state.";
	}

	@Override
	public String description() {
		return "Toggles the mod 360 state similar to the 'Indicate angle reduction mod 360 on next F3+C' hotkey. " +
			   "Will do nothing if boat measurements are disabled in the settings, the calculator is locked, or if the calculator is currently in all advancements mode.";
	}

	@Override
	public IAction[] mapToActions(IDomainModel domainModel, IDataState dataState) {
		if (ninjabrainBotPreferences.usePreciseAngle.get() && dataState.boatDataState().boatState().get() == BoatState.VALID && !dataState.locked().get() && !dataState.allAdvancementsDataState().allAdvancementsModeEnabled().get()) {
			return new IAction[] { new ToggleMod360IndicatorAction(dataState) };
		}
		return new IAction[0];
	}

}