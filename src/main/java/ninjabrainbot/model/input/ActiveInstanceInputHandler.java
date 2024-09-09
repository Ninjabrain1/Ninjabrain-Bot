package ninjabrainbot.model.input;

import ninjabrainbot.event.DisposeHandler;
import ninjabrainbot.event.IDisposable;
import ninjabrainbot.io.mcinstance.IActiveInstanceProvider;
import ninjabrainbot.io.mcinstance.IMinecraftWorldFile;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.io.preferences.enums.AllAdvancementsToggleType;
import ninjabrainbot.model.actions.IActionExecutor;
import ninjabrainbot.model.actions.alladvancements.SetHasEnteredEndAction;
import ninjabrainbot.model.actions.common.ResetAction;
import ninjabrainbot.model.datastate.IDataState;
import ninjabrainbot.model.domainmodel.IDomainModel;
import ninjabrainbot.model.environmentstate.IEnvironmentState;

/**
 * Listens to active instance changes and decides if/how the changes should affect the data state.
 */
public class ActiveInstanceInputHandler implements IDisposable {

	private final IActiveInstanceProvider activeInstanceProvider;
	private final NinjabrainBotPreferences preferences;
	private final IDomainModel domainModel;
	private final IDataState dataState;
	private final IActionExecutor actionExecutor;

	private final DisposeHandler disposeHandler = new DisposeHandler();

	private IMinecraftWorldFile lastActiveMinecraftWorldFile;

	public ActiveInstanceInputHandler(IActiveInstanceProvider activeInstanceProvider, IDomainModel domainModel, IDataState dataState, IActionExecutor actionExecutor, NinjabrainBotPreferences preferences) {
		this.activeInstanceProvider = activeInstanceProvider;
		this.preferences = preferences;
		this.domainModel = domainModel;
		this.dataState = dataState;
		this.actionExecutor = actionExecutor;
		lastActiveMinecraftWorldFile = activeInstanceProvider.activeMinecraftWorld().get();

		disposeHandler.add(activeInstanceProvider.activeMinecraftWorld().subscribe(this::onActiveMinecraftWorldChanged));
		disposeHandler.add(activeInstanceProvider.whenActiveMinecraftWorldModified().subscribe(this::updateHasEnteredEndState));
	}

	private void onActiveMinecraftWorldChanged(IMinecraftWorldFile newWorldFile) {
		if (!dataState.locked().get() && !domainModel.isReset() && preferences.autoResetWhenChangingInstance.get() && lastActiveMinecraftWorldFile != null)
			actionExecutor.executeImmediately(new ResetAction(domainModel));
		lastActiveMinecraftWorldFile = newWorldFile;
	}

	private void updateHasEnteredEndState() {
		if (preferences.allAdvancementsToggleType.get() == AllAdvancementsToggleType.Automatic) {
			IMinecraftWorldFile worldFile = activeInstanceProvider.activeMinecraftWorld().get();
			if (worldFile != null && worldFile.hasEnteredEnd())
				// Do not set to false if hasEnteredEnd() == false, it should not be possible for automatic detection to go from hasEnteredEnd = true to hasEnteredEnd = false.
				actionExecutor.executeImmediately(new SetHasEnteredEndAction(dataState.allAdvancementsDataState(), true));
		}
	}

	@Override
	public void dispose() {
		disposeHandler.dispose();
	}

}
