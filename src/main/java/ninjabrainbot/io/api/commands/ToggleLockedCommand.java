package ninjabrainbot.io.api.commands;

import ninjabrainbot.io.api.interfaces.IParameterlessCommand;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.model.actions.IAction;
import ninjabrainbot.model.actions.common.ToggleLockedAction;
import ninjabrainbot.model.datastate.IDataState;
import ninjabrainbot.model.domainmodel.IDomainModel;

public class ToggleLockedCommand implements IParameterlessCommand {

	@Override
	public String name() {
		return "toggle_lock";
	}

	@Override
	public String summary() {
		return "Toggles the calculator lock.";
	}

	@Override
	public String description() {
		return "Toggles the calculator lock similar to the 'Lock calculator' hotkey. ";
	}

	@Override
	public IAction[] mapToActions(IDomainModel domainModel, IDataState dataState) {
		return new IAction[] { new ToggleLockedAction(dataState) };
	}

}