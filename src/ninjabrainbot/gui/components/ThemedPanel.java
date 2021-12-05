package ninjabrainbot.gui.components;

import java.awt.Color;

import javax.swing.JPanel;

import ninjabrainbot.gui.GUI;
import ninjabrainbot.gui.SizePreference;
import ninjabrainbot.gui.Theme;

public class ThemedPanel extends JPanel implements ThemedComponent {
	
	private static final long serialVersionUID = 1363577008580584264L;
	public boolean bold;
	
	public ThemedPanel(GUI gui) {
		this(gui, false);
	}
	
	public ThemedPanel(GUI gui, boolean bold) {
		super();
		gui.registerThemedComponent(this);
		this.bold = bold;
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
		return p.TEXT_SIZE_MEDIUM;
	}
	
	public Color getBackgroundColor(Theme theme) {
		return null;
	}
	
	public Color getForegroundColor(Theme theme) {
		return null;
	}
	
}
