package ninjabrainbot.io.api.commands;

import java.util.Collections;

import ninjabrainbot.model.actions.IAction;
import ninjabrainbot.model.actions.common.ResetAction;
import ninjabrainbot.model.domainmodel.IDomainModel;

public class ResetCommand implements ICommand {

	private final IDomainModel domainModel;

	public ResetCommand(IDomainModel domainModel) {
		this.domainModel = domainModel;
	}

	public String name() {
		return "reset";
	}

	public Iterable<IAction> mapToActions() {
		return Collections.singleton(new ResetAction(domainModel));
	}

}
