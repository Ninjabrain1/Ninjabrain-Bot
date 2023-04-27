package ninjabrainbot.util;

import ninjabrainbot.event.IObservable;
import ninjabrainbot.event.ObservableField;
import ninjabrainbot.io.IClipboardProvider;

public class FakeClipboardProvider implements IClipboardProvider {

	private ObservableField<String> clipboard = new ObservableField<String>("");

	@Override
	public IObservable<String> clipboardText() {
		return clipboard;
	}

}
