package ninjabrainbot.io;

import javax.swing.Timer;

import ninjabrainbot.calculator.IDataState;

public class AutoResetTimer extends Timer {

	private static final long serialVersionUID = 607205414762178442L;

	private static final int AUTO_RESET_DELAY = 15 * 60 * 1000;

	public AutoResetTimer(IDataState dataState) {
		super(AUTO_RESET_DELAY, null);
		addActionListener(p -> {
			dataState.reset();
			restart();
			stop();
		});
	}

}
