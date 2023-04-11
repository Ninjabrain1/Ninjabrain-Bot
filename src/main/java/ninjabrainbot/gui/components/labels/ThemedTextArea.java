package ninjabrainbot.gui.components.labels;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JTextArea;

import ninjabrainbot.gui.components.ThemedComponent;
import ninjabrainbot.gui.style.SizePreference;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.gui.style.theme.WrappedColor;

public class ThemedTextArea extends JTextArea implements ThemedComponent {

	public final boolean bold;

	private WrappedColor bgCol;
	private WrappedColor fgCol;

	public ThemedTextArea(StyleManager styleManager) {
		this(styleManager, "");
	}

	public ThemedTextArea(StyleManager styleManager, String text) {
		this(styleManager, text, false);
	}

	public ThemedTextArea(StyleManager styleManager, String text, boolean bold) {
		super(text);
		styleManager.registerThemedComponent(this);
		this.bold = bold;
		setEditable(false);
		setLineWrap(false);

		bgCol = styleManager.currentTheme.COLOR_HEADER;
		fgCol = styleManager.currentTheme.TEXT_COLOR_HEADER;
	}

	public void updateSize(StyleManager styleManager) {
		setFont(styleManager.fontSize(getTextSize(styleManager.size), !bold));
	}

	public void updateColors() {
		Color bg = getBackgroundColor();
		if (bg != null)
			setBackground(bg);
		Color fg = getForegroundColor();
		if (fg != null)
			setForeground(fg);
	}

	public int getTextSize(SizePreference p) {
		return p.TEXT_SIZE_SMALL;
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

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(0, super.getPreferredSize().height);
	}

}
