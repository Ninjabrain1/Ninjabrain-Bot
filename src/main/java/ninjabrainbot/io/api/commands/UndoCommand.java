package ninjabrainbot.io.api.commands;

import ninjabrainbot.io.api.interfaces.IDomainModelCommand;
import ninjabrainbot.model.datastate.IDataState;
import ninjabrainbot.model.domainmodel.IDomainModel;

public class UndoCommand implements IDomainModelCommand {

	@Override
	public String name() {
		return "undo";
	}

	@Override
	public String summary() {
		return "Undoes the last action.";
	}

	@Override
	public String description() {
		return "Undoes the last action, does the same as pressing the 'Undo' button in the UI.";
	}

	@Override
	public void execute(IDomainModel domainModel, IDataState dataState) {
		if (!dataState.locked().get())
			domainModel.undoUnderWriteLock();
	}

}
