package ninjabrainbot.model.input;

import ninjabrainbot.event.DisposeHandler;
import ninjabrainbot.event.IDisposable;
import ninjabrainbot.io.mcinstance.IActiveInstanceProvider;
import ninjabrainbot.io.mcinstance.IMinecraftWorldFile;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.model.actions.IActionExecutor;
import ninjabrainbot.model.actions.common.ResetAction;
import ninjabrainbot.model.datastate.IDataState;
import ninjabrainbot.model.domainmodel.IDomainModel;
import ninjabrainbot.model.environmentstate.IEnvironmentState;

/**
 * Listens to active instance changes and decides if/how the changes should affect the data state.
 */
public class ActiveInstanceInputHandler implements IDisposable {

	private final NinjabrainBotPreferences preferences;
	private final IDomainModel domainModel;
	private final IDataState dataState;
	private final IEnvironmentState environmentState;
	private final IActionExecutor actionExecutor;

	private final DisposeHandler disposeHandler = new DisposeHandler();

	private IMinecraftWorldFile lastActiveMinecraftWorldFile;

	public ActiveInstanceInputHandler(IActiveInstanceProvider activeInstanceProvider, IDomainModel domainModel, IDataState dataState, IEnvironmentState environmentState, IActionExecutor actionExecutor, NinjabrainBotPreferences preferences) {
		this.preferences = preferences;
		this.domainModel = domainModel;
		this.dataState = dataState;
		this.environmentState = environmentState;
		this.actionExecutor = actionExecutor;
		lastActiveMinecraftWorldFile = activeInstanceProvider.activeMinecraftWorld().get();

		disposeHandler.add(activeInstanceProvider.activeMinecraftWorld().subscribe(this::onActiveMinecraftWorldChanged));
		disposeHandler.add(activeInstanceProvider.whenActiveMinecraftWorldModified().subscribe(this::onActiveMinecraftWorldModified));
	}

	private void onActiveMinecraftWorldChanged(IMinecraftWorldFile newWorldFile) {
		if (!dataState.locked().get() && preferences.autoResetWhenChangingInstance.get() && lastActiveMinecraftWorldFile != null)
			actionExecutor.executeImmediately(new ResetAction(domainModel));
		lastActiveMinecraftWorldFile = newWorldFile;
	}

	private void onActiveMinecraftWorldModified(IMinecraftWorldFile modifiedWorldFile) {
		if (modifiedWorldFile != null)
			environmentState.setHasEnteredEnd(modifiedWorldFile.hasEnteredEnd());
	}

	@Override
	public void dispose() {
		disposeHandler.dispose();
	}

}
