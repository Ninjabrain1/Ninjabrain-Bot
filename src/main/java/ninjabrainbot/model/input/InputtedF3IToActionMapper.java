package ninjabrainbot.model.input;

import java.util.Optional;

import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.model.actions.IAction;
import ninjabrainbot.model.actions.alladvancements.SetAllAdvancementsGeneralLocationAction;
import ninjabrainbot.model.actions.common.SetFossilAction;
import ninjabrainbot.model.datastate.IDataState;
import ninjabrainbot.model.datastate.divine.Fossil;
import ninjabrainbot.model.datastate.endereye.F3IData;

public class InputtedF3IToActionMapper implements IInputtedF3IToActionMapper {

	private final IDataState dataState;
	private final NinjabrainBotPreferences preferences;

	public InputtedF3IToActionMapper(IDataState dataState, NinjabrainBotPreferences preferences) {
		this.dataState = dataState;
		this.preferences = preferences;
	}

	public IAction[] getActionsForInputtedF3I(F3IData f3IData) {
		Optional<IAction> action = getActionForInputtedF3I(f3IData);
		return action.isPresent()
				? new IAction[] { action.get() }
				: new IAction[0];
	}

	private Optional<IAction> getActionForInputtedF3I(F3IData f3IData) {
		if (dataState.locked().get())
			return Optional.empty();

		if (dataState.allAdvancementsDataState().allAdvancementsModeEnabled().get()) {
			// Only execute if 1.20+ AA mode enabled.
			if (preferences.oneDotTwentyPlusAA.get() && preferences.allAdvancements.get()) {
				return Optional.of(new SetAllAdvancementsGeneralLocationAction(dataState.allAdvancementsDataState(), f3IData));
			}
			return Optional.empty();
		} else {
			Fossil fossil = Fossil.tryCreateFromF3I(f3IData);
			return Optional.of(new SetFossilAction(dataState.getDivineContext(), fossil));
		}
	}

}
