package ninjabrainbot.model.actions;

import ninjabrainbot.model.domainmodel.IDomainModel;

public class ResetAction implements IAction {

	private final IDomainModel domainModel;

	public ResetAction(IDomainModel domainModel) {
		this.domainModel = domainModel;
	}

	@Override
	public void execute() {
		domainModel.reset();
	}
}
