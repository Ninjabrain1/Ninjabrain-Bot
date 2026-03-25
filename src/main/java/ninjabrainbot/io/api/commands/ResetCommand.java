package ninjabrainbot.io.api.commands;

import java.util.Collections;

import ninjabrainbot.io.api.interfaces.IParameterlessCommand;
import ninjabrainbot.model.actions.IAction;
import ninjabrainbot.model.actions.common.ResetAction;
import ninjabrainbot.model.datastate.IDataState;
import ninjabrainbot.model.domainmodel.IDomainModel;

public class ResetCommand implements IParameterlessCommand {

	@Override
	public String name() {
		return "reset";
	}

	@Override
	public String description() {
		return "Resets the application state, as if the 'Reset' button is pressed.";
	}

	@Override
	public IAction[] mapToActions(IDomainModel domainModel, IDataState dataState) {
		return new IAction[] { new ResetAction(domainModel) };
	}

}
