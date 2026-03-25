package ninjabrainbot.io.api;

import java.util.Arrays;
import java.util.List;

import ninjabrainbot.io.api.commands.InputClipboardCommand;
import ninjabrainbot.io.api.commands.RedoCommand;
import ninjabrainbot.io.api.commands.ResetCommand;
import ninjabrainbot.io.api.commands.UndoCommand;
import ninjabrainbot.io.api.interfaces.ICommand;
import ninjabrainbot.model.input.IInputtedF3IToActionMapper;
import ninjabrainbot.model.input.IInputtedPlayerPositionToActionMapper;

public class ApiV1Commands {

	public static List<ICommand> createAllCommands(IInputtedPlayerPositionToActionMapper inputtedPlayerPositionToActionMapper, IInputtedF3IToActionMapper inputtedF3IToActionMapper) {
		return Arrays.asList(
				new UndoCommand(),
				new RedoCommand(),
				new ResetCommand(),
				new InputClipboardCommand(inputtedPlayerPositionToActionMapper, inputtedF3IToActionMapper)
		);
	}

}
