package ninjabrainbot.model.input;

import ninjabrainbot.model.IDataState;
import ninjabrainbot.model.actions.IActionExecutor;
import ninjabrainbot.model.actions.ResetAction;
import ninjabrainbot.model.actions.SetAllAdvancementsModeAction;
import ninjabrainbot.model.domainmodel.IDomainModel;
import ninjabrainbot.event.DisposeHandler;
import ninjabrainbot.event.IDisposable;
import ninjabrainbot.io.mcinstance.IActiveInstanceProvider;
import ninjabrainbot.io.mcinstance.IMinecraftWorldFile;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;

/**
 * Listens to active instance changes and decides if/how the changes should affect the data state.
 */
public class ActiveInstanceInputHandler implements IDisposable {

	private final IActiveInstanceProvider activeInstanceProvider;
	private final IDomainModel domainModel;
	private final IDataState dataState;
	private final IActionExecutor actionExecutor;
	private final NinjabrainBotPreferences preferences;

	final DisposeHandler disposeHandler = new DisposeHandler();

	public ActiveInstanceInputHandler(IActiveInstanceProvider activeInstanceProvider, IDomainModel domainModel, IDataState dataState, IActionExecutor actionExecutor, NinjabrainBotPreferences preferences) {
		this.activeInstanceProvider = activeInstanceProvider;
		this.domainModel = domainModel;
		this.dataState = dataState;
		this.actionExecutor = actionExecutor;
		this.preferences = preferences;
		disposeHandler.add(activeInstanceProvider.activeMinecraftWorld().subscribe(this::onActiveMinecraftWorldChanged));
		disposeHandler.add(activeInstanceProvider.whenActiveMinecraftWorldModified().subscribe(this::onActiveMinecraftWorldModified));
		disposeHandler.add(preferences.allAdvancements.whenModified().subscribe(this::updateAllAdvancementsMode));
	}

	private void onActiveMinecraftWorldChanged(IMinecraftWorldFile newWorldFile) {
		if (!dataState.locked().get())
			actionExecutor.executeImmediately(new ResetAction(domainModel));
	}

	private void onActiveMinecraftWorldModified(IMinecraftWorldFile modifiedWorldFile) {
		updateAllAdvancementsMode();
	}

	private void updateAllAdvancementsMode() {
		IMinecraftWorldFile modifiedWorldFile = activeInstanceProvider.activeMinecraftWorld().get();
		boolean allAdvancementsModeEnabled = modifiedWorldFile != null && preferences.allAdvancements.get() && modifiedWorldFile.hasEnteredEnd();
		actionExecutor.executeImmediately(new SetAllAdvancementsModeAction(dataState.allAdvancementsDataState(), allAdvancementsModeEnabled));
	}

	@Override
	public void dispose() {
		disposeHandler.dispose();
	}

}
