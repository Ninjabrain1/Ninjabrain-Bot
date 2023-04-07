package ninjabrainbot.io;

import ninjabrainbot.event.IObservable;

public interface IClipboardProvider {

	IObservable<String> clipboardText();

}
