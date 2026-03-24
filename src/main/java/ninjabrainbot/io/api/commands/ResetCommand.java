package ninjabrainbot.io.api.commands;

import java.util.Collections;

import ninjabrainbot.io.api.interfaces.ICommand;
import ninjabrainbot.model.actions.IAction;
import ninjabrainbot.model.actions.common.ResetAction;
import ninjabrainbot.model.datastate.IDataState;
import ninjabrainbot.model.domainmodel.IDomainModel;

public class ResetCommand implements ICommand {

	@Override
	public String name() {
		return "reset";
	}

	@Override
	public String description() {
		return "Resets the application state.";
	}

	@Override
	public Iterable<IAction> mapToActions(IDomainModel domainModel, IDataState dataState) {
		return Collections.singleton(new ResetAction(domainModel));
	}

}
