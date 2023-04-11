package ninjabrainbot.io;

import javax.swing.Timer;

import ninjabrainbot.model.IDataState;
import ninjabrainbot.model.actions.IActionExecutor;
import ninjabrainbot.model.actions.ResetAction;
import ninjabrainbot.model.domainmodel.IDomainModel;
import ninjabrainbot.event.DisposeHandler;
import ninjabrainbot.event.IDisposable;

public class AutoResetTimer extends Timer implements IDisposable {

	private static final int AUTO_RESET_DELAY = 15 * 60 * 1000;

	final DisposeHandler disposeHandler = new DisposeHandler();

	public AutoResetTimer(IDataState dataState, IDomainModel domainModel, IActionExecutor actionExecutor) {
		super(AUTO_RESET_DELAY, null);
		addActionListener(p -> {
			if (!dataState.locked().get())
				actionExecutor.executeImmediately(new ResetAction(domainModel));
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
