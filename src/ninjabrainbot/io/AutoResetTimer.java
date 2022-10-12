package ninjabrainbot.io;

import javax.swing.Timer;

import ninjabrainbot.calculator.IDataState;
import ninjabrainbot.util.IDisposable;
import ninjabrainbot.util.SubscriptionHandler;

public class AutoResetTimer extends Timer implements IDisposable {

	private static final long serialVersionUID = 607205414762178442L;

	private static final int AUTO_RESET_DELAY = 15 * 60 * 1000;

	SubscriptionHandler sh = new SubscriptionHandler();
	
	public AutoResetTimer(IDataState dataState) {
		super(AUTO_RESET_DELAY, null);
		addActionListener(p -> {
			dataState.reset();
			restart();
			stop();
		});
		sh.add(dataState.whenLockedChanged().subscribe(__ -> restart()));
		sh.add(dataState.whenBlindResultChanged().subscribe(__ -> restart()));
		sh.add(dataState.whenCalculatorResultChanged().subscribe(__ -> restart()));
	}
	
	@Override
	public void dispose() {
		sh.dispose();
	}

}
