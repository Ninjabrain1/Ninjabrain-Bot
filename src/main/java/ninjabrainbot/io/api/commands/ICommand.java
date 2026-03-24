package ninjabrainbot.io.api.commands;

import ninjabrainbot.model.actions.IAction;

public interface ICommand extends IApiCommand {

	Iterable<IAction> mapToActions();

}
