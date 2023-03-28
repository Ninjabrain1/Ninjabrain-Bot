package ninjabrainbot.io;

import javax.swing.Timer;

import ninjabrainbot.data.IDataState;
import ninjabrainbot.data.IDataStateHandler;
import ninjabrainbot.event.IDisposable;
import ninjabrainbot.event.SubscriptionHandler;

public class AutoResetTimer extends Timer implements IDisposable {

	private static final int AUTO_RESET_DELAY = 15 * 60 * 1000;

	SubscriptionHandler sh = new SubscriptionHandler();

	public AutoResetTimer(IDataState dataState, IDataStateHandler dataStateHandler) {
		super(AUTO_RESET_DELAY, null);
		addActionListener(p -> {
			dataStateHandler.resetIfNotLocked();
			restart();
			stop();
		});
		sh.add(dataState.locked().subscribe(__ -> restart()));
		sh.add(dataState.blindResult().subscribe(__ -> restart()));
		sh.add(dataState.calculatorResult().subscribe(__ -> restart()));
	}

	public void setAutoResetEnabled(boolean b) {
		if (b)
			start();
		else
			stop();
	}

	@Override
	public void dispose() {
		sh.dispose();
	}

}
