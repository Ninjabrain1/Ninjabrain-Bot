package ninjabrainbot.io.api;

import java.util.Arrays;
import java.util.List;

import ninjabrainbot.io.api.commands.ChangeLastAngleCommand;
import ninjabrainbot.io.api.commands.InputClipboardCommand;
import ninjabrainbot.io.api.commands.RedoCommand;
import ninjabrainbot.io.api.commands.ResetCommand;
import ninjabrainbot.io.api.commands.SetAdvancementsModeCommand;
import ninjabrainbot.io.api.commands.ToggleAltStdOnLastThrowCommand;
import ninjabrainbot.io.api.commands.ToggleEnteringBoatCommand;
import ninjabrainbot.io.api.commands.ToggleLockedCommand;
import ninjabrainbot.io.api.commands.ToggleMod360IndicatorCommand;
import ninjabrainbot.io.api.commands.UndoCommand;
import ninjabrainbot.io.api.interfaces.ICommand;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.model.input.IInputtedF3IToActionMapper;
import ninjabrainbot.model.input.IInputtedPlayerPositionToActionMapper;

public class ApiV1Commands {

	public static List<ICommand> createAllCommands(IInputtedPlayerPositionToActionMapper inputtedPlayerPositionToActionMapper, IInputtedF3IToActionMapper inputtedF3IToActionMapper, NinjabrainBotPreferences ninjabrainBotPreferences) {
		return Arrays.asList(
				new UndoCommand(),
				new RedoCommand(),
				new ResetCommand(),
				new InputClipboardCommand(inputtedPlayerPositionToActionMapper, inputtedF3IToActionMapper),
				new ChangeLastAngleCommand(ninjabrainBotPreferences),
				new ToggleEnteringBoatCommand(ninjabrainBotPreferences),
				new ToggleMod360IndicatorCommand(ninjabrainBotPreferences),
				new ToggleAltStdOnLastThrowCommand(ninjabrainBotPreferences),
				new ToggleLockedCommand(),
				new SetAdvancementsModeCommand()
		);
	}

}
