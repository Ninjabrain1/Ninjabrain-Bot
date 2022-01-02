package ninjabrainbot.gui.components;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JTextArea;

import ninjabrainbot.gui.GUI;
import ninjabrainbot.gui.SizePreference;
import ninjabrainbot.gui.Theme;

public class ThemedTextArea extends JTextArea implements ThemedComponent {
	
	private static final long serialVersionUID = -1769219771406000716L;
	public boolean bold;
	
	public ThemedTextArea(GUI gui) {
		this(gui, "");
	}
	
	public ThemedTextArea(GUI gui, String text) {
		this(gui, text, false);
	}
	
	public ThemedTextArea(GUI gui, String text, boolean bold) {
		super(text);
		gui.registerThemedComponent(this);
		this.bold = bold;
		setEditable(false);
		setLineWrap(false);
		setFocusable(false);
	}
	
	public void updateSize(GUI gui) {
		setFont(gui.fontSize(getTextSize(gui.size), !bold));
	}
	
	public void updateColors(GUI gui) {
		Color bg = getBackgroundColor(gui.theme);
		if (bg != null)
			setBackground(bg);
		Color fg = getForegroundColor(gui.theme);
		if (fg != null)
			setForeground(fg);
	}
	
	public int getTextSize(SizePreference p) {
		return p.TEXT_SIZE_TINY;
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
