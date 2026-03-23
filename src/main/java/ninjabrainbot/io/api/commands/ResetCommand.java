package ninjabrainbot.io.api.commands;

import ninjabrainbot.model.actions.IActionExecutor;
import ninjabrainbot.model.actions.common.ResetAction;
import ninjabrainbot.model.domainmodel.IDomainModel;

public class ResetCommand implements ICommand {

	private final IActionExecutor actionExecutor;
	private final IDomainModel domainModel;

	public ResetCommand(IActionExecutor actionExecutor, IDomainModel domainModel) {
		this.actionExecutor = actionExecutor;
		this.domainModel = domainModel;
	}

	public void post(String arguments) {
		actionExecutor.executeImmediately(new ResetAction(domainModel));
	}

}
