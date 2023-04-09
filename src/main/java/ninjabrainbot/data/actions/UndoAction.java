package ninjabrainbot.data.actions;

import ninjabrainbot.data.domainmodel.IDomainModel;

public class UndoAction implements IAction {

	private final IDomainModel domainModel;

	public UndoAction(IDomainModel domainModel) {
		this.domainModel = domainModel;
	}

	@Override
	public void execute() {

	}
}
