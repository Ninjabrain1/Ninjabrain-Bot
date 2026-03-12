package ninjabrainbot.util;

import ninjabrainbot.event.IObservable;
import ninjabrainbot.event.ObservableField;
import ninjabrainbot.io.IClipboardListener;
import ninjabrainbot.io.IClipboardProvider;

public class MockedClipboardReader implements IClipboardProvider, IClipboardListener {

	private final ObservableField<String> clipboard = new ObservableField<>("");

	@Override
	public void setClipboard(String string) {
		clipboard.set(string);
	}

	@Override
	public IObservable<String> clipboardText() {
		return clipboard;
	}
}
