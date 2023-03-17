package ninjabrainbot.io;

import ninjabrainbot.event.IObservable;

public interface IClipboardProvider {

	public IObservable<String> clipboardText();

}
