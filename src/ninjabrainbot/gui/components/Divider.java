package ninjabrainbot.gui.components;

import java.awt.Color;

import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;

import ninjabrainbot.gui.GUI;
import ninjabrainbot.gui.Theme;

public class Divider extends JSeparator implements ThemedComponent {
	
	private static final long serialVersionUID = 4116749757122783747L;

	public Divider(GUI gui) {
		gui.registerThemedComponent(this);
	}

	@Override
	public void updateFont(GUI gui) {
	}

	@Override
	public void updateColors(GUI gui) {
		setBackground(getBackgroundColor(gui.theme));
		setForeground(getBackgroundColor(gui.theme));
	}
	
	public Color getBackgroundColor(Theme theme) {
		return theme.COLOR_STRONGEST;
	}
	
}
