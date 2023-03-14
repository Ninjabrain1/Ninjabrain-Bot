package ninjabrainbot.gui.style;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.font.FontRenderContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.SwingUtilities;

import ninjabrainbot.Main;
import ninjabrainbot.gui.components.ThemedComponent;
import ninjabrainbot.gui.frames.ThemedDialog;
import ninjabrainbot.gui.frames.ThemedFrame;
import ninjabrainbot.util.I18n;
import ninjabrainbot.util.Profiler;

public class StyleManager {

	private boolean initialized = false;

	public CurrentTheme currentTheme;
	public SizePreference size;
	private final ArrayList<ThemedComponent> themedComponents;
	private final ArrayList<ThemedFrame> themedFrames;
	private final ArrayList<ThemedDialog> themedDialogs;

	private Font font;
	private HashMap<String, Font> fonts;

	private final FontRenderContext frc = new FontRenderContext(null, true, false);

	public StyleManager(Theme theme, SizePreference size) {
		currentTheme = new CurrentTheme();
		currentTheme.setTheme(theme);
		this.size = size;
		font = new Font(null, Font.BOLD, 25);
		themedComponents = new ArrayList<>();
		themedFrames = new ArrayList<>();
		themedDialogs = new ArrayList<>();

		initFonts();
		currentTheme.whenModified().subscribe(__ -> updateFontsAndColors());
	}

	private void initFonts() {
		font = loadFont();
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		ge.registerFont(font);
		fonts = new HashMap<>();
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
		if (initialized)
			SwingUtilities.invokeLater(() -> initComponent(c));
	}

	public void registerThemedFrame(ThemedFrame f) {
		themedFrames.add(f);
	}

	public void registerThemedDialog(ThemedDialog d) {
		themedDialogs.add(d);
	}

	public void unregisterThemedDialog(ThemedDialog d) {
		themedDialogs.remove(d);
	}

	private void updateBounds() {
		for (ThemedComponent tc : themedComponents) {
			tc.updateSize(this);
		}
		for (ThemedFrame tf : themedFrames) {
			tf.updateBounds(this);
		}
		for (ThemedDialog tf : themedDialogs) {
			tf.updateBounds(this);
		}
	}

	private void updateFontsAndColors() {
		for (ThemedFrame tf : themedFrames) {
			tf.updateFontsAndColors();
		}
		for (ThemedDialog tf : themedDialogs) {
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

	public void setSizePreference(SizePreference size) {
		this.size = size;
		updateFontsAndColors();
		updateBounds();
	}

	private Font loadFont() {
		Font font = null;
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, Main.class.getResourceAsStream("/OpenSans-Regular.ttf"));
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
		initialized = true;
	}

	private void initComponent(ThemedComponent component) {
		component.updateColors();
		component.updateSize(this);
	}

}
