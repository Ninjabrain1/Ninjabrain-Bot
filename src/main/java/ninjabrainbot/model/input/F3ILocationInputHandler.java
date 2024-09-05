package ninjabrainbot.model.input;

import ninjabrainbot.event.DisposeHandler;
import ninjabrainbot.event.IDisposable;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.model.actions.IActionExecutor;
import ninjabrainbot.model.actions.alladvancements.SetAllAdvancementsGeneralLocationAction;
import ninjabrainbot.model.actions.common.SetFossilAction;
import ninjabrainbot.model.datastate.IDataState;
import ninjabrainbot.model.datastate.divine.Fossil;
import ninjabrainbot.model.datastate.endereye.F3IData;

/**
 * Listens to the stream of F3+I-inputs and decides if/how they should be inputted into the data state.
 */
public class F3ILocationInputHandler implements IDisposable {

	private final NinjabrainBotPreferences preferences;
	private final IDataState dataState;
	private final IActionExecutor actionExecutor;

	final DisposeHandler disposeHandler = new DisposeHandler();

	public F3ILocationInputHandler(IF3ILocationInputSource f3iLocationInputSource, IDataState dataState, IActionExecutor actionExecutor, NinjabrainBotPreferences preferences) {
		this.preferences = preferences;
		this.dataState = dataState;
		this.actionExecutor = actionExecutor;
		disposeHandler.add(f3iLocationInputSource.whenNewF3ILocationInputted().subscribe(this::onNewF3ILocation));
	}

	private void onNewF3ILocation(F3IData f3IData) {
		if (dataState.locked().get())
			return;

		if (dataState.allAdvancementsDataState().allAdvancementsModeEnabled().get()) {
			// Only execute if 1.20+ AA mode enabled.
			if (preferences.oneDotTwentyPlusAA.get() && preferences.allAdvancements.get()) {
				actionExecutor.executeImmediately(new SetAllAdvancementsGeneralLocationAction(dataState.allAdvancementsDataState(), f3IData));
			}
		} else {
			Fossil fossil = Fossil.tryCreateFromF3I(f3IData);
			actionExecutor.executeImmediately(new SetFossilAction(dataState.getDivineContext(), fossil));
		}
	}

	@Override
	public void dispose() {
		disposeHandler.dispose();
	}
}
