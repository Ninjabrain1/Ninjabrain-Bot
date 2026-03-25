package ninjabrainbot.io.api.commands;

import ninjabrainbot.io.api.interfaces.IDomainModelCommand;
import ninjabrainbot.model.datastate.IDataState;
import ninjabrainbot.model.domainmodel.IDomainModel;

public class RedoCommand implements IDomainModelCommand {

	@Override
	public String name() {
		return "redo";
	}

	@Override
	public String summary() {
		return "Redoes the last undone action.";
	}

	@Override
	public String description() {
		return "Redoes the last undone action, does the same as pressing the 'Redo' button in the UI. " +
			   "Will do nothing if the calculator is locked.";
	}

	@Override
	public void execute(IDomainModel domainModel, IDataState dataState) {
		if (!dataState.locked().get())
			domainModel.redoUnderWriteLock();
	}

}
