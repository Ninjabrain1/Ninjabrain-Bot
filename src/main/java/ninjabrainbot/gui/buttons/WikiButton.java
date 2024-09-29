package ninjabrainbot.gui.buttons;

import java.awt.Desktop;
import java.net.URL;
import java.util.Objects;

import javax.swing.ImageIcon;

import ninjabrainbot.Main;
import ninjabrainbot.gui.style.StyleManager;

public class WikiButton extends FlatButton {

	private final String wikiUrl;

	public WikiButton(StyleManager styleManager, String wikiUrl) {
		super(styleManager, new ImageIcon(Objects.requireNonNull(Main.class.getResource("/help_icon.png"))));
		this.wikiUrl = wikiUrl;
		setBackgroundColor(styleManager.currentTheme.COLOR_NEUTRAL);
		setHoverColor(styleManager.currentTheme.COLOR_NEUTRAL);
		addActionListener(__ -> openWikiPageInBrowser());
	}

	private void openWikiPageInBrowser() {
		try {
			Desktop.getDesktop().browse(new URL(wikiUrl).toURI());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
