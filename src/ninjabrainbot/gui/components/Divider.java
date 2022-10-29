package ninjabrainbot.gui.components;

import java.awt.Color;

import javax.swing.JSeparator;

import ninjabrainbot.gui.style.ConfigurableColor;
import ninjabrainbot.gui.style.StyleManager;

public class Divider extends JSeparator implements ThemedComponent {

	private static final long serialVersionUID = 4116749757122783747L;

	private ConfigurableColor bgCol;

	public Divider(StyleManager styleManager) {
		styleManager.registerThemedComponent(this);
		bgCol = styleManager.currentTheme.COLOR_STRONGEST;
	}

	@Override
	public void updateSize(StyleManager styleManager) {
	}

	@Override
	public void updateColors() {
		setBackground(getBackgroundColor());
		setForeground(getBackgroundColor());
	}

	public void setBackgroundColor(ConfigurableColor color) {
		bgCol = color;
	}

	protected Color getBackgroundColor() {
		return bgCol.color();
	}

}
