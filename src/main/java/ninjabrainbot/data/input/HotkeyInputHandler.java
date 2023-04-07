package ninjabrainbot.data.input;

import ninjabrainbot.data.IDataState;
import ninjabrainbot.data.actions.IActionExecutor;
import ninjabrainbot.data.actions.ResetAction;
import ninjabrainbot.data.temp.IDomainModel;
import ninjabrainbot.event.DisposeHandler;
import ninjabrainbot.event.IDisposable;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;

public class HotkeyInputHandler implements IDisposable {

	private final NinjabrainBotPreferences preferences;
	private final IDomainModel domainModel;
	private final IDataState dataState;
	private final IActionExecutor actionExecutor;

	private final DisposeHandler disposeHandler = new DisposeHandler();

	public HotkeyInputHandler(NinjabrainBotPreferences preferences, IDomainModel domainModel, IDataState dataState, IActionExecutor actionExecutor) {
		this.preferences = preferences;
		this.domainModel = domainModel;
		this.dataState = dataState;
		this.actionExecutor = actionExecutor;

		disposeHandler.add(preferences.hotkeyReset.whenTriggered().subscribe(this::resetIfNotLocked));
//		preferences.hotkeyUndo.whenTriggered().subscribe(__ -> dataStateHandler.undoIfNotLocked());
//		preferences.hotkeyIncrement.whenTriggered().subscribe(__ -> dataStateHandler.changeLastAngleIfNotLocked(true, preferences));
//		preferences.hotkeyDecrement.whenTriggered().subscribe(__ -> dataStateHandler.changeLastAngleIfNotLocked(false, preferences));
//		preferences.hotkeyAltStd.whenTriggered().subscribe(__ -> dataStateHandler.toggleAltStdOnLastThrowIfNotLocked());
//		preferences.hotkeyBoat.whenTriggered().subscribe(__ -> dataStateHandler.toggleEnteringBoatIfNotLocked());
//		preferences.hotkeyLock.whenTriggered().subscribe(__ -> dataStateHandler.toggleLocked());
	}

	private void resetIfNotLocked() {
		if (!dataState.locked().get())
			actionExecutor.executeImmediately(new ResetAction(domainModel));
	}

	@Override
	public void dispose() {
		disposeHandler.dispose();
	}
}
