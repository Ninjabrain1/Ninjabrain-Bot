package ninjabrainbot.util;

import ninjabrainbot.event.IObservable;
import ninjabrainbot.event.ObservableField;
import ninjabrainbot.io.IClipboardProvider;

public class MockedClipboardReader implements IClipboardProvider {

	private final ObservableField<String> clipboard = new ObservableField<>("");

	public void setClipboard(String string) {
		clipboard.set(string);
	}

	@Override
	public IObservable<String> clipboardText() {
		return clipboard;
	}
}
