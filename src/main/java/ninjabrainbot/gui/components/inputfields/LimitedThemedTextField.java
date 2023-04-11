package ninjabrainbot.gui.components.inputfields;

import ninjabrainbot.gui.style.StyleManager;

public class LimitedThemedTextField extends ThemedTextField {

	private final int maxNumberOfCharacters;

	public LimitedThemedTextField(StyleManager styleManager, int maxNumberOfCharacters) {
		super(styleManager);
		this.maxNumberOfCharacters = maxNumberOfCharacters;
	}

	@Override
	protected boolean verifyInput(String text) {
		return text.length() <= maxNumberOfCharacters;
	}

}
