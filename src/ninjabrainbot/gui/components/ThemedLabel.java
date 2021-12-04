package ninjabrainbot.gui.components;

import java.awt.Color;

import javax.swing.JLabel;

import ninjabrainbot.gui.GUI;
import ninjabrainbot.gui.TextSizePreference;
import ninjabrainbot.gui.Theme;

public class ThemedLabel extends JLabel implements ThemedComponent {
	
	private static final long serialVersionUID = 1363577008580584264L;
	public boolean bold;
	
	public ThemedLabel(GUI gui) {
		this(gui, "");
	}
	
	public ThemedLabel(GUI gui, String text) {
		this(gui, text, false);
	}
	
	public ThemedLabel(GUI gui, String text, boolean bold) {
		super(text);
		gui.registerThemedComponent(this);
		this.bold = bold;
	}
	
	public final void updateFont(GUI gui) {
		setFont(gui.fontSize(getTextSize(gui.textSize), !bold));
	}
	
	public void updateColors(GUI gui) {
		Color bg = getBackgroundColor(gui.theme);
		if (bg != null)
			setBackground(bg);
		Color fg = getForegroundColor(gui.theme);
		if (fg != null)
			setForeground(fg);
	}
	
	public int getTextSize(TextSizePreference p) {
		return p.MAIN_TEXT_SIZE;
	}
	
	public Color getBackgroundColor(Theme theme) {
		return null;
	}
	
	public Color getForegroundColor(Theme theme) {
		return theme.TEXT_COLOR_STRONG;
	}
	
}
