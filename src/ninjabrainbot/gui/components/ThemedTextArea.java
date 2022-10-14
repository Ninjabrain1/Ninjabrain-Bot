package ninjabrainbot.gui.components;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JTextArea;

import ninjabrainbot.gui.StyleManager;
import ninjabrainbot.gui.SizePreference;
import ninjabrainbot.gui.Theme;

public class ThemedTextArea extends JTextArea implements ThemedComponent {
	
	private static final long serialVersionUID = -1769219771406000716L;
	public boolean bold;
	
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
	}
	
	public void updateSize(StyleManager styleManager) {
		setFont(styleManager.fontSize(getTextSize(styleManager.size), !bold));
	}
	
	public void updateColors(StyleManager styleManager) {
		Color bg = getBackgroundColor(styleManager.theme);
		if (bg != null)
			setBackground(bg);
		Color fg = getForegroundColor(styleManager.theme);
		if (fg != null)
			setForeground(fg);
	}
	
	public int getTextSize(SizePreference p) {
		return p.TEXT_SIZE_SMALL;
	}
	
	public Color getBackgroundColor(Theme theme) {
		return theme.COLOR_STRONG;
	}
	
	public Color getForegroundColor(Theme theme) {
		return theme.TEXT_COLOR_STRONG;
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(0, super.getPreferredSize().height);
	}
	
}
