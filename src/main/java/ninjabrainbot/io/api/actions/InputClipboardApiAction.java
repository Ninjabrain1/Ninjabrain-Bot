package ninjabrainbot.io.api.actions;

import ninjabrainbot.io.IClipboardListener;

public class InputClipboardApiAction implements IApiAction {

	private final IClipboardListener clipboardListener;

	public InputClipboardApiAction(IClipboardListener clipboardListener) {
		this.clipboardListener = clipboardListener;
	}

	@Override
	public String post(String body) {
		clipboardListener.setClipboard(body);
		return "copied";
	}
}
