package ninjabrainbot.gui.components;

import java.awt.Color;

import ninjabrainbot.gui.GUI;
import ninjabrainbot.gui.Theme;

public class ThemedOpaquePanel extends ThemedPanel {
	
	
	private static final long serialVersionUID = 6139785742324691300L;

	public ThemedOpaquePanel(GUI gui) {
		super(gui);
	}
	
	public ThemedOpaquePanel(GUI gui, boolean bold) {
		super(gui, bold);
	}
	
	public Color getBackgroundColor(Theme theme) {
		return theme.COLOR_NEUTRAL;
	}
	
	public Color getForegroundColor(Theme theme) {
		return theme.TEXT_COLOR_NEUTRAL;
	}
	
}
