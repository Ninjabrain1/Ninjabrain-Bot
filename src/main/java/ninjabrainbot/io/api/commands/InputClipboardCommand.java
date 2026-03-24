package ninjabrainbot.io.api.commands;

import java.util.Collections;

import ninjabrainbot.io.api.documentation.ApiParam;
import ninjabrainbot.io.api.interfaces.IParametrizedCommand;
import ninjabrainbot.model.actions.IAction;
import ninjabrainbot.model.actions.common.ResetAction;
import ninjabrainbot.model.datastate.IDataState;
import ninjabrainbot.model.domainmodel.IDomainModel;

public class InputClipboardCommand implements IParametrizedCommand<InputClipboardCommand.Args> {

	@Override
	public String name() {
		return "input_clipboard";
	}

	@Override
	public String description() {
		return "Parses the given text and inputs it into the application as if the clipboard was set to it.";
	}

	@Override
	public Iterable<IAction> mapToActions(IDomainModel domainModel, IDataState dataState, Args args) {
		return Collections.singleton(new ResetAction(domainModel));
	}

	public static class Args {

		@ApiParam(description = "Text to parse and input into the application as if the clipboard was set to it.", required = true)
		private String clipboardText;

	}

}