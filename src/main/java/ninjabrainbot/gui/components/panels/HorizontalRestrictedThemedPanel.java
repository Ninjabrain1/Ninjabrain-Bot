package ninjabrainbot.gui.components.panels;

import java.awt.Dimension;

import ninjabrainbot.gui.components.panels.ThemedPanel;
import ninjabrainbot.gui.style.StyleManager;

public class HorizontalRestrictedThemedPanel extends ThemedPanel {

	private static final long serialVersionUID = 5615638088344083425L;

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
