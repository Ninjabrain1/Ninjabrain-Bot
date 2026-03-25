package ninjabrainbot.io.api.commands;

import ninjabrainbot.io.api.documentation.ApiParam;
import ninjabrainbot.io.api.interfaces.IParametrizedCommand;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.model.actions.IAction;
import ninjabrainbot.model.actions.common.ResetAction;
import ninjabrainbot.model.actions.endereye.ChangeLastAngleAction;
import ninjabrainbot.model.datastate.IDataState;
import ninjabrainbot.model.datastate.common.DetailedPlayerPosition;
import ninjabrainbot.model.datastate.common.LimitedPlayerPosition;
import ninjabrainbot.model.datastate.endereye.F3CData;
import ninjabrainbot.model.datastate.endereye.F3IData;
import ninjabrainbot.model.datastate.endereye.InputData1_12;
import ninjabrainbot.model.domainmodel.IDomainModel;
import ninjabrainbot.model.input.IInputtedF3IToActionMapper;
import ninjabrainbot.model.input.IInputtedPlayerPositionToActionMapper;

public class ChangeLastAngleCommand implements IParametrizedCommand<ChangeLastAngleCommand.Args> {

	private final NinjabrainBotPreferences ninjabrainBotPreferences;

	public ChangeLastAngleCommand(NinjabrainBotPreferences ninjabrainBotPreferences) {
		this.ninjabrainBotPreferences = ninjabrainBotPreferences;
	}

	@Override
	public String name() {
		return "change_last_angle";
	}

	@Override
	public String summary() {
		return "Increments or decrements the last angle.";
	}

	@Override
	public String description() {
		return "Increments or decrements the last angle a number of steps, equivalent to pressing the increment/decrement last angle hotkeys a number of times. " +
			   "Just like for the corresponding hotkeys, the 'Pixel adjustment type' setting affects how much each increment changes the last angle. " +
			   "Will do nothing if the calculator is locked.";
	}

	@Override
	public IAction[] mapToActions(IDomainModel domainModel, IDataState dataState, Args args) {
		return new IAction[] { new ChangeLastAngleAction(dataState.getThrowList(), dataState.locked(), ninjabrainBotPreferences, args.correctionIncrements) };
	}

	public static class Args {

		@ApiParam(description = "A positive value increments the last angle that many times, a negative value decrements it.",
				required = true,
				example = "-5")
		private int correctionIncrements;

	}

}