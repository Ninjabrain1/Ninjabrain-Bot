package ninjabrainbot.gui.components.inputfields;

import ninjabrainbot.gui.style.StyleManager;

public class LimitedThemedTextField extends ThemedTextField {

	private static final long serialVersionUID = 8018919933607184642L;

	private int maxNumberOfCharacters;

	public LimitedThemedTextField(StyleManager styleManager, int maxNumberOfCharacters) {
		super(styleManager);
		this.maxNumberOfCharacters = maxNumberOfCharacters;
	}

	@Override
	protected boolean verifyInput(String text) {
		return text.length() <= maxNumberOfCharacters;
	}

}
