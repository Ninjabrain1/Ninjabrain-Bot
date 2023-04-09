package ninjabrainbot.data.actions;

import ninjabrainbot.data.domainmodel.IDataComponent;
import ninjabrainbot.data.domainmodel.IDomainModel;

public class ResetAction implements IAction {

	private final IDomainModel domainModel;

	public ResetAction(IDomainModel domainModel) {
		this.domainModel = domainModel;
	}

	@Override
	public void execute() {
		for (IDataComponent<?> dataComponent : domainModel.getAllDataComponents()) {
			dataComponent.reset();
		}
	}
}
