package ninjabrainbot.io.api.commands;

import java.util.Arrays;
import java.util.List;

public class ApiV1Commands {

	public static List<IApiCommand> createAllCommands() {
		return Arrays.asList(
				new InputClipboardCommand()
		);
	}

}
