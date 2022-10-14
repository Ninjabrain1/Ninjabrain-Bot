package ninjabrainbot.gui.components;

import java.awt.Color;

import ninjabrainbot.gui.StyleManager;
import ninjabrainbot.gui.Theme;

public class ThemedOpaquePanel extends ThemedPanel {
	
	
	private static final long serialVersionUID = 6139785742324691300L;

	public ThemedOpaquePanel(StyleManager styleManager) {
		super(styleManager);
	}
	
	public ThemedOpaquePanel(StyleManager styleManager, boolean bold) {
		super(styleManager, bold);
	}
	
	public Color getBackgroundColor(Theme theme) {
		return theme.COLOR_NEUTRAL;
	}
	
	public Color getForegroundColor(Theme theme) {
		return theme.TEXT_COLOR_NEUTRAL;
	}
	
}
