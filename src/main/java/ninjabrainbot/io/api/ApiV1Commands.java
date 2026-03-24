package ninjabrainbot.io.api;

import java.util.Arrays;
import java.util.List;

import ninjabrainbot.io.api.commands.InputClipboardCommand;
import ninjabrainbot.io.api.commands.RedoCommand;
import ninjabrainbot.io.api.commands.ResetCommand;
import ninjabrainbot.io.api.commands.UndoCommand;
import ninjabrainbot.io.api.interfaces.ICommand;

public class ApiV1Commands {

	public static List<ICommand> createAllCommands() {
		return Arrays.asList(
				new UndoCommand(),
				new RedoCommand(),
				new ResetCommand(),
				new InputClipboardCommand()
		);
	}

}
