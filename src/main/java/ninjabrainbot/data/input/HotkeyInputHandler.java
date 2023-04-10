package ninjabrainbot.data.input;

import ninjabrainbot.data.IDataState;
import ninjabrainbot.data.actions.ChangeLastAngleAction;
import ninjabrainbot.data.actions.IActionExecutor;
import ninjabrainbot.data.actions.ResetAction;
import ninjabrainbot.data.actions.ToggleAltStdOnLastThrowAction;
import ninjabrainbot.data.actions.ToggleEnteringBoatAction;
import ninjabrainbot.data.actions.ToggleLockedAction;
import ninjabrainbot.data.actions.UndoAction;
import ninjabrainbot.data.domainmodel.IDomainModel;
import ninjabrainbot.event.DisposeHandler;
import ninjabrainbot.event.IDisposable;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;

public class HotkeyInputHandler implements IDisposable {

	private final IDomainModel domainModel;
	private final IDataState dataState;
	private final IActionExecutor actionExecutor;

	private final DisposeHandler disposeHandler = new DisposeHandler();

	public HotkeyInputHandler(NinjabrainBotPreferences preferences, IDomainModel domainModel, IDataState dataState, IActionExecutor actionExecutor) {
		this.domainModel = domainModel;
		this.dataState = dataState;
		this.actionExecutor = actionExecutor;

		disposeHandler.add(preferences.hotkeyReset.whenTriggered().subscribe(this::resetIfNotLocked));
		preferences.hotkeyUndo.whenTriggered().subscribe(this::undoIfNotLocked);
		preferences.hotkeyIncrement.whenTriggered().subscribe(__ -> actionExecutor.executeImmediately(new ChangeLastAngleAction(dataState, preferences, true)));
		preferences.hotkeyDecrement.whenTriggered().subscribe(__ -> actionExecutor.executeImmediately(new ChangeLastAngleAction(dataState, preferences, false)));
		preferences.hotkeyAltStd.whenTriggered().subscribe(__ -> actionExecutor.executeImmediately(new ToggleAltStdOnLastThrowAction(dataState, preferences)));
		preferences.hotkeyBoat.whenTriggered().subscribe(__ -> actionExecutor.executeImmediately(new ToggleEnteringBoatAction(dataState)));
		preferences.hotkeyLock.whenTriggered().subscribe(__ -> actionExecutor.executeImmediately(new ToggleLockedAction(dataState)));
	}

	private void resetIfNotLocked() {
		if (!dataState.locked().get())
			actionExecutor.executeImmediately(new ResetAction(domainModel));
	}

	private void undoIfNotLocked() {
		if (!dataState.locked().get())
			actionExecutor.executeImmediately(new UndoAction(domainModel));
	}

	@Override
	public void dispose() {
		disposeHandler.dispose();
	}
}
