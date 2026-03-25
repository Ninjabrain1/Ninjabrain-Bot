package ninjabrainbot.io.api.commands;

import ninjabrainbot.io.api.interfaces.IDomainModelCommand;
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
		return "Redoes the last undone action, does the same as pressing the 'Redo' button in the UI.";
	}

	@Override
	public void execute(IDomainModel domainModel) {
		domainModel.redoUnderWriteLock();
	}

}
