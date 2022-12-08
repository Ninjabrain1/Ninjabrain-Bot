package ninjabrainbot.gui.buttons;

import javax.swing.ImageIcon;

import ninjabrainbot.gui.style.StyleManager;

public class TitleBarButton extends FlatButton {

	private static final long serialVersionUID = -8794077055512451437L;

	public TitleBarButton(StyleManager styleManager, ImageIcon img) {
		super(styleManager, img);
		setBackgroundColor(styleManager.currentTheme.COLOR_STRONGEST);
		setHoverColor(styleManager.currentTheme.COLOR_DIVIDER);
	}

	public TitleBarButton(StyleManager styleManager, String str) {
		super(styleManager, str);
		setBackgroundColor(styleManager.currentTheme.COLOR_STRONGEST);
		setHoverColor(styleManager.currentTheme.COLOR_DIVIDER);
	}

}
