package ninjabrainbot.io;

import ninjabrainbot.event.ISubscribable;

public interface IClipboardProvider {

	public ISubscribable<String> whenClipboardChanged();

}
