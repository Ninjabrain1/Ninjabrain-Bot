package ninjabrainbot.gui.panels.settings;

import java.awt.Dimension;

import ninjabrainbot.gui.panels.ThemedPanel;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.gui.style.WrappedColor;

public class ColorPreviewPanel extends ThemedPanel {

	private static final long serialVersionUID = -204692569559526638L;

	public ColorPreviewPanel(StyleManager styleManager, WrappedColor color) {
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