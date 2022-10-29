package ninjabrainbot.gui.style;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.font.FontRenderContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import ninjabrainbot.Main;
import ninjabrainbot.gui.components.ThemedComponent;
import ninjabrainbot.gui.frames.ThemedFrame;
import ninjabrainbot.util.I18n;
import ninjabrainbot.util.Profiler;

public class StyleManager {

	public CurrentTheme currentTheme;
	public SizePreference size;
	private final ArrayList<ThemedComponent> themedComponents;
	private final ArrayList<ThemedFrame> themedFrames;

	private Font font;
	private HashMap<String, Font> fonts;

	private final FontRenderContext frc = new FontRenderContext(null, true, false);

	public StyleManager() {
		currentTheme = new CurrentTheme();
		currentTheme.setTheme(Theme.get(Main.preferences.theme.get()));
		size = SizePreference.get(Main.preferences.size.get());
		font = new Font(null, Font.BOLD, 25);
		themedComponents = new ArrayList<>();
		themedFrames = new ArrayList<>();

		initFonts();
		setupSettingsSubscriptions();
	}

	private void initFonts() {
		font = loadFont();
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		ge.registerFont(font);
		fonts = new HashMap<>();
	}

	private void setupSettingsSubscriptions() {
		Main.preferences.theme.whenModified().subscribe(__ -> updateTheme());
		Main.preferences.size.whenModified().subscribe(__ -> updateSizePreference());
	}

	public Font fontSize(float size, boolean light) {
		String key = size + " " + light;
		if (!fonts.containsKey(key)) {
			Font f = font.deriveFont(light ? Font.PLAIN : Font.BOLD, size);
			fonts.put(key, f);
			return f;
		}
		return fonts.get(key);
	}

	public void registerThemedComponent(ThemedComponent c) {
		themedComponents.add(c);
	}

	public void registerThemedFrame(ThemedFrame f) {
		themedFrames.add(f);
	}

	private void updateBounds() {
		for (ThemedComponent tc : themedComponents) {
			tc.updateSize(this);
		}
		for (ThemedFrame tf : themedFrames) {
			tf.updateBounds(this);
		}
		// updateFontsAndColors();
	}

	private void updateFontsAndColors() {
		for (ThemedFrame tf : themedFrames) {
			tf.updateFontsAndColors();
		}
		for (ThemedComponent tc : themedComponents) {
			tc.updateColors();
			tc.updateSize(this);
		}
	}

	public int getTextWidth(String text, Font font) {
		return (int) font.getStringBounds(text, frc).getWidth();
	}

	private void updateTheme() {
		currentTheme.setTheme(Theme.get(Main.preferences.theme.get()));
		updateFontsAndColors();
//		updateOBSOverlay();
	}

	private void updateSizePreference() {
		size = SizePreference.get(Main.preferences.size.get());
		updateFontsAndColors();
		updateBounds();
//		SwingUtilities.invokeLater(() -> updateOBSOverlay());
	}

	private Font loadFont() {
		Font font = null;
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, Main.class.getResourceAsStream("/resources/OpenSans-Regular.ttf"));
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
		if (font == null || font.canDisplayUpTo(I18n.get("lang")) != -1) {
			font = new Font(null);
		}
		if (font == null || font.canDisplayUpTo(I18n.get("lang")) != -1) {
			Font[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
			for (Font f : fonts) {
				if (f.canDisplayUpTo(I18n.get("lang")) < 0) {
					return f;
				}
			}
		}
		return font;
	}

	public void init() {
		Profiler.start("Fonts and colors");
		updateFontsAndColors();
		Profiler.stopAndStart("Bounds");
		updateBounds();
		Profiler.stop();
	}

}
