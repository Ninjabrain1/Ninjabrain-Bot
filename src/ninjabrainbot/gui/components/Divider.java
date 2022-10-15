package ninjabrainbot.gui.components;

import java.awt.Color;

import javax.swing.JSeparator;

import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.gui.style.Theme;

public class Divider extends JSeparator implements ThemedComponent {
	
	private static final long serialVersionUID = 4116749757122783747L;

	public Divider(StyleManager styleManager) {
		styleManager.registerThemedComponent(this);
	}

	@Override
	public void updateSize(StyleManager styleManager) {
	}

	@Override
	public void updateColors(StyleManager styleManager) {
		setBackground(getBackgroundColor(styleManager.theme));
		setForeground(getBackgroundColor(styleManager.theme));
	}
	
	public Color getBackgroundColor(Theme theme) {
		return theme.COLOR_STRONGEST;
	}
	
}
