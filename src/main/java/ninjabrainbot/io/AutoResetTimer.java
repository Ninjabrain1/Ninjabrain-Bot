package ninjabrainbot.io;

import javax.swing.Timer;

import ninjabrainbot.event.DisposeHandler;
import ninjabrainbot.event.IDisposable;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.model.actions.IActionExecutor;
import ninjabrainbot.model.actions.common.ResetAction;
import ninjabrainbot.model.datastate.IDataState;
import ninjabrainbot.model.domainmodel.IDomainModel;

public class AutoResetTimer extends Timer implements IDisposable {

	private static final int AUTO_RESET_DELAY = 15 * 60 * 1000;

	private final NinjabrainBotPreferences preferences;

	final DisposeHandler disposeHandler = new DisposeHandler();

	public AutoResetTimer(IDataState dataState, IDomainModel domainModel, IActionExecutor actionExecutor, NinjabrainBotPreferences preferences) {
		super(AUTO_RESET_DELAY, null);
		this.preferences = preferences;
		addActionListener(p -> {
			if (!dataState.locked().get())
				actionExecutor.executeImmediately(new ResetAction(domainModel));
			restart();
			stop();
		});

		refresh();
		disposeHandler.add(domainModel.whenModified().subscribe(this::refresh));
		disposeHandler.add(preferences.autoReset.whenModified().subscribe(this::refresh));
	}

	private void refresh() {
		restart();
		if (preferences.autoReset.get()) {
			start();
		} else {
			stop();
		}
	}

	@Override
	public void dispose() {
		disposeHandler.dispose();
	}

}
