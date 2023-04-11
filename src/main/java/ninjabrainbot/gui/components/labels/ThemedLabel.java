package ninjabrainbot.gui.components.labels;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import ninjabrainbot.gui.components.ThemedComponent;
import ninjabrainbot.gui.style.SizePreference;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.gui.style.theme.WrappedColor;

public class ThemedLabel extends JLabel implements ThemedComponent, ILabel {

	public final boolean bold;

	private WrappedColor bgCol;
	private WrappedColor fgCol;

	public ThemedLabel(StyleManager styleManager) {
		this(styleManager, "");
	}

	public ThemedLabel(StyleManager styleManager, boolean centered) {
		this(styleManager, "", false, centered);
	}

	public ThemedLabel(StyleManager styleManager, String text) {
		this(styleManager, text, false);
	}

	public ThemedLabel(StyleManager styleManager, String text, boolean bold) {
		this(styleManager, text, bold, false);
	}

	public ThemedLabel(StyleManager styleManager, String text, boolean bold, boolean centered) {
		super(text);
		styleManager.registerThemedComponent(this);
		this.bold = bold;
		setHorizontalAlignment(centered ? SwingConstants.CENTER : SwingConstants.LEFT);

		bgCol = styleManager.currentTheme.COLOR_NEUTRAL;
		fgCol = styleManager.currentTheme.TEXT_COLOR_NEUTRAL;
	}

	public void updateSize(StyleManager styleManager) {
		setFont(styleManager.fontSize(getTextSize(styleManager.size), !bold));
	}

	@Override
	public void updateColors() {
		Color bg = getBackgroundColor();
		if (bg != null)
			setBackground(bg);
		Color fg = getForegroundColor();
		if (fg != null)
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
