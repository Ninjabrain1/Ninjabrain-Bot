package ninjabrainbot.event;

import java.util.ArrayList;

public class DisposeHandler implements IDisposable {

	public final ArrayList<IDisposable> disposables = new ArrayList<>(2);

	public <T extends IDisposable> T add(T disposable) {
		disposables.add(disposable);
		return disposable;
	}

	@Override
	public void dispose() {
		for (IDisposable disposable : disposables) {
			disposable.dispose();
		}
	}

}
