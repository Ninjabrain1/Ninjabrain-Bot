package ninjabrainbot.gui.components;

import java.awt.Color;

import javax.swing.JPanel;

import ninjabrainbot.gui.StyleManager;
import ninjabrainbot.gui.SizePreference;
import ninjabrainbot.gui.Theme;

public class ThemedPanel extends JPanel implements ThemedComponent {
	
	private static final long serialVersionUID = 1363577008580584264L;
	public boolean bold;
	
	public ThemedPanel(StyleManager styleManager) {
		this(styleManager, false);
	}
	
	public ThemedPanel(StyleManager styleManager, boolean bold) {
		super();
		styleManager.registerThemedComponent(this);
		this.bold = bold;
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
		return p.TEXT_SIZE_MEDIUM;
	}
	
	public Color getBackgroundColor(Theme theme) {
		return null;
	}
	
	public Color getForegroundColor(Theme theme) {
		return null;
	}
	
}
