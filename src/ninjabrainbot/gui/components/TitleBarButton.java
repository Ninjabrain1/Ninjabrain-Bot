package ninjabrainbot.gui.components;

import java.awt.Color;

import javax.swing.ImageIcon;

import ninjabrainbot.gui.GUI;
import ninjabrainbot.gui.Theme;

public class TitleBarButton extends FlatButton {

	private static final long serialVersionUID = -8794077055512451437L;
	
	public TitleBarButton(GUI gui, ImageIcon img) {
		super(gui, img);
	}
	
	public TitleBarButton(GUI gui, String str) {
		super(gui, str);
	}
	
	@Override
	public Color getBackgroundColor(Theme theme) {
		return theme.COLOR_STRONGEST;
	}
	
	@Override
	public Color getHoverColor(Theme theme) {
		return theme.COLOR_STRONGER;
	}
	
}
