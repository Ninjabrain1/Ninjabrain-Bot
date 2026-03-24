package ninjabrainbot.io.api.commands;

import java.util.Collections;

import ninjabrainbot.model.actions.IAction;
import ninjabrainbot.model.actions.common.ResetAction;
import ninjabrainbot.model.datastate.IDataState;
import ninjabrainbot.model.domainmodel.IDomainModel;

public class InputClipboardCommand implements IParametrizedCommand<InputClipboardCommandArgs> {

	public String name() {
		return "input_clipboard";
	}

	public Iterable<IAction> mapToActions(IDomainModel domainModel, IDataState dataState, InputClipboardCommandArgs args) {
		return Collections.singleton(new ResetAction(domainModel));
	}

}
class InputClipboardCommandArgs {

	@ApiParam(description = "Text to parse and input into the application as if the clipboard was set to it.", required = true)
	private int clipboardText;

}