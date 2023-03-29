package ninjabrainbot.event;

import java.util.ArrayList;

public class DisposeHandler implements IDisposable {

	public ArrayList<IDisposable> disposables = new ArrayList<>(2);

	public void add(IDisposable disposable) {
		disposables.add(disposable);
	}

	@Override
	public void dispose() {
		for (IDisposable disposable : disposables) {
			disposable.dispose();
		}
	}

}
