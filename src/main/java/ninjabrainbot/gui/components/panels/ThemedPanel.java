package ninjabrainbot.gui.components.panels;

import java.awt.Color;

import javax.swing.JPanel;

import ninjabrainbot.gui.components.ThemedComponent;
import ninjabrainbot.gui.style.SizePreference;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.gui.style.theme.WrappedColor;

public class ThemedPanel extends JPanel implements ThemedComponent {

	private static final long serialVersionUID = 1363577008580584264L;
	public boolean bold;

	private WrappedColor bgCol;
	private WrappedColor fgCol;

	public ThemedPanel(StyleManager styleManager) {
		this(styleManager, false);
	}

	public ThemedPanel(StyleManager styleManager, boolean bold) {
		super();
		styleManager.registerThemedComponent(this);
		this.bold = bold;

		bgCol = styleManager.currentTheme.COLOR_NEUTRAL;
		fgCol = styleManager.currentTheme.TEXT_COLOR_NEUTRAL;
	}

	public void updateSize(StyleManager styleManager) {
		setFont(styleManager.fontSize(getTextSize(styleManager.size), !bold));
	}

	@Override
	public void updateColors() {
		Color bg = getBackgroundColor();
		setBackground(bg);
		Color fg = getForegroundColor();
		setForeground(fg);
	}

	public int getTextSize(SizePreference p) {
		return p.TEXT_SIZE_MEDIUM;
	}

	public void setBackgroundColor(WrappedColor color) {
		bgCol = color;
	}

	public void setForegroundColor(WrappedColor color) {
		fgCol = color;
	}

	protected Color getBackgroundColor() {
		return bgCol.color();
	}

	protected Color getForegroundColor() {
		return fgCol.color();
	}

}
