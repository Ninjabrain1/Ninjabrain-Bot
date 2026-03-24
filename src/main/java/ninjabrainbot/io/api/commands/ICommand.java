package ninjabrainbot.io.api.commands;

import ninjabrainbot.model.actions.IAction;

public interface ICommand {

	String name();

	Iterable<IAction> mapToActions();

}
