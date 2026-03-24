package ninjabrainbot.io.api;

import java.util.Arrays;
import java.util.List;

import ninjabrainbot.io.api.commands.InputClipboardCommand;
import ninjabrainbot.io.api.commands.ResetCommand;
import ninjabrainbot.io.api.interfaces.IApiCommand;

public class ApiV1Commands {

	public static List<IApiCommand> createAllCommands() {
		return Arrays.asList(
				new ResetCommand(),
				new InputClipboardCommand()
		);
	}

}
