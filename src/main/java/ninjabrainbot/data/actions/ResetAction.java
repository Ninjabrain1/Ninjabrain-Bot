package ninjabrainbot.data.actions;

import ninjabrainbot.data.temp.IDataComponent;
import ninjabrainbot.data.temp.IDomainModel;

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
