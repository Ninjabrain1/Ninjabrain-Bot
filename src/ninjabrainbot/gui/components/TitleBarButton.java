package ninjabrainbot.gui.components;

import java.awt.Color;

import javax.swing.ImageIcon;

import ninjabrainbot.gui.StyleManager;
import ninjabrainbot.gui.Theme;

public class TitleBarButton extends FlatButton {

	private static final long serialVersionUID = -8794077055512451437L;
	
	public TitleBarButton(StyleManager styleManager, ImageIcon img) {
		super(styleManager, img);
	}
	
	public TitleBarButton(StyleManager styleManager, String str) {
		super(styleManager, str);
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
