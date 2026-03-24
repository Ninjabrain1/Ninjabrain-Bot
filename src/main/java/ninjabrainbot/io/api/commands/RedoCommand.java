package ninjabrainbot.io.api.commands;

import ninjabrainbot.io.api.interfaces.IDomainModelCommand;
import ninjabrainbot.model.domainmodel.IDomainModel;

public class RedoCommand implements IDomainModelCommand {

	@Override
	public String name() {
		return "redo";
	}

	@Override
	public String description() {
		return "Redoes the last undone action, as if the 'Redo' button is pressed.";
	}

	@Override
	public void execute(IDomainModel domainModel) {
		domainModel.redoUnderWriteLock();
	}

}
