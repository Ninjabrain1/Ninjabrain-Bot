package ninjabrainbot.util;

import ninjabrainbot.event.ISubscribable;
import ninjabrainbot.event.ObservableField;
import ninjabrainbot.io.IClipboardProvider;

public class FakeClipboardProvider implements IClipboardProvider {

	private ObservableField<String> clipboard = new ObservableField<String>("");

	@Override
	public ISubscribable<String> whenClipboardChanged() {
		return clipboard;
	}

}
