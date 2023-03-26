package ninjabrainbot.gui.options;

import java.awt.Dimension;

import ninjabrainbot.gui.components.panels.ThemedPanel;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.gui.style.theme.WrappedColor;

public class ColorDisplayPanel extends ThemedPanel {

	private static final long serialVersionUID = -204692569559526638L;

	public ColorDisplayPanel(StyleManager styleManager, WrappedColor color) {
		super(styleManager);
		setBackgroundColor(color);
	}

	@Override
	public void updateSize(StyleManager styleManager) {
		super.updateSize(styleManager);
		int textSize = getTextSize(styleManager.size);
		setPreferredSize(new Dimension(textSize * 2, textSize * 2));
	}
}