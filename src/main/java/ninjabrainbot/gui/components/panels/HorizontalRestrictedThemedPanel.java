package ninjabrainbot.gui.components.panels;

import java.awt.Dimension;

import ninjabrainbot.gui.style.StyleManager;

public class HorizontalRestrictedThemedPanel extends ThemedPanel {

	public HorizontalRestrictedThemedPanel(StyleManager styleManager) {
		super(styleManager);
	}

	@Override
	public Dimension getPreferredSize() {
		Dimension preferredSize = super.getPreferredSize();
		preferredSize.width = 0;
		return preferredSize;
	}

}
