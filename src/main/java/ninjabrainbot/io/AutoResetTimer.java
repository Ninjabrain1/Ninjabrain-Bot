package ninjabrainbot.io;

import javax.swing.Timer;

import ninjabrainbot.data.IDataState;
import ninjabrainbot.data.IDataStateHandler;
import ninjabrainbot.event.IDisposable;
import ninjabrainbot.event.DisposeHandler;

public class AutoResetTimer extends Timer implements IDisposable {

	private static final int AUTO_RESET_DELAY = 15 * 60 * 1000;

	DisposeHandler disposeHandler = new DisposeHandler();

	public AutoResetTimer(IDataState dataState, IDataStateHandler dataStateHandler) {
		super(AUTO_RESET_DELAY, null);
		addActionListener(p -> {
			dataStateHandler.resetIfNotLocked();
			restart();
			stop();
		});
		disposeHandler.add(dataState.locked().subscribe(__ -> restart()));
		disposeHandler.add(dataState.blindResult().subscribe(__ -> restart()));
		disposeHandler.add(dataState.calculatorResult().subscribe(__ -> restart()));
	}

	public void setAutoResetEnabled(boolean b) {
		if (b)
			start();
		else
			stop();
	}

	@Override
	public void dispose() {
		disposeHandler.dispose();
	}

}
