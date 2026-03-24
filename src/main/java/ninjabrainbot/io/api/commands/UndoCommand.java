package ninjabrainbot.io.api.commands;

import ninjabrainbot.io.api.interfaces.IDomainModelCommand;
import ninjabrainbot.model.domainmodel.IDomainModel;

public class UndoCommand implements IDomainModelCommand {

	@Override
	public String name() {
		return "undo";
	}

	@Override
	public String description() {
		return "Undoes the last action, as if the 'Undo' button is pressed.";
	}

	@Override
	public void execute(IDomainModel domainModel) {
		domainModel.undoUnderWriteLock();
	}

}
