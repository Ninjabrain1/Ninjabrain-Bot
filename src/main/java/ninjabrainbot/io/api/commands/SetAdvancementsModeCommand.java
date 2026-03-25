package ninjabrainbot.io.api.commands;

import ninjabrainbot.io.api.documentation.ApiParam;
import ninjabrainbot.io.api.interfaces.IParametrizedCommand;
import ninjabrainbot.model.actions.IAction;
import ninjabrainbot.model.actions.alladvancements.SetHasEnteredEndAction;
import ninjabrainbot.model.datastate.IDataState;
import ninjabrainbot.model.datastate.alladvancements.IAllAdvancementsDataState;
import ninjabrainbot.model.domainmodel.IDomainModel;

public class SetAdvancementsModeCommand implements IParametrizedCommand<SetAdvancementsModeCommand.Args> {

	@Override
	public String name() {
		return "set_aa_mode";
	}

	@Override
	public String summary() {
		return "Enables or disables all advancements mode.";
	}

	@Override
	public String description() {
		return "Enables or disables all advancements mode.";
	}

	@Override
	public IAction[] mapToActions(IDomainModel domainModel, IDataState dataState, Args args) {
		IAllAdvancementsDataState allAdvancementsDataState = dataState.allAdvancementsDataState();
		return new IAction[] { new SetHasEnteredEndAction(allAdvancementsDataState, args.enabled) };
	}

	public static class Args {

		@ApiParam(description = "Whether all advancements mode should be enabled or disabled.",
				required = true,
				example = "true")
		private boolean enabled;

	}

}