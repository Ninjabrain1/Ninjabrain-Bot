package ninjabrainbot.gui.components.layout;

import java.awt.Color;

import javax.swing.JSeparator;

import ninjabrainbot.gui.components.ThemedComponent;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.gui.style.theme.WrappedColor;

public class Divider extends JSeparator implements ThemedComponent {

	private WrappedColor bgCol;

	public Divider(StyleManager styleManager) {
		styleManager.registerThemedComponent(this);
		bgCol = styleManager.currentTheme.COLOR_DIVIDER_DARK;
	}

	@Override
	public void updateSize(StyleManager styleManager) {
	}

	@Override
	public void updateColors() {
		setBackground(getBackgroundColor());
		setForeground(getBackgroundColor());
	}

	public void setBackgroundColor(WrappedColor color) {
		bgCol = color;
	}

	protected Color getBackgroundColor() {
		return bgCol.color();
	}

}
