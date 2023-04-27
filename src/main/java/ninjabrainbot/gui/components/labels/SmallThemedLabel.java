package ninjabrainbot.gui.components.labels;

import ninjabrainbot.gui.style.SizePreference;
import ninjabrainbot.gui.style.StyleManager;

public class SmallThemedLabel extends ThemedLabel {

	public SmallThemedLabel(StyleManager styleManager) {
		super(styleManager);
	}

	public SmallThemedLabel(StyleManager styleManager, String text) {
		super(styleManager, text);
	}

	@Override
	public int getTextSize(SizePreference p) {
		return p.TEXT_SIZE_SMALL;
	}

}
