package ninjabrainbot.gui.style;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.font.FontRenderContext;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.SwingUtilities;
import javax.swing.Timer;

import ninjabrainbot.Main;
import ninjabrainbot.gui.components.ThemedComponent;
import ninjabrainbot.gui.frames.ThemedFrame;
import ninjabrainbot.util.I18n;
import ninjabrainbot.util.Profiler;

public class StyleManager {

	public Theme theme;
	public SizePreference size;
	private final ArrayList<ThemedComponent> themedComponents;
	private final ArrayList<ThemedFrame> themedFrames;

	private Timer overlayHideTimer;
	private Timer overlayUpdateTimer;
	private long lastOverlayUpdate = System.currentTimeMillis();
	private final long minOverlayUpdateDelayMillis = 1000;
	public Timer autoResetTimer;
	private boolean targetLocked = false;

	private Font font;
	private HashMap<String, Font> fonts;

	public final File OBS_OVERLAY;

	public StyleManager() {
		setupSettingsSubscriptions();

		// OLD
		theme = Theme.get(Main.preferences.theme.get());
		size = SizePreference.get(Main.preferences.size.get());
		font = new Font(null, Font.BOLD, 25);
		themedComponents = new ArrayList<>();
		themedFrames = new ArrayList<>();

		OBS_OVERLAY = new File(System.getProperty("java.io.tmpdir"), "nb-overlay.png");

		// Load fonts
		Profiler.stopAndStart("Load fonts");
		font = loadFont();
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		ge.registerFont(font);
		fonts = new HashMap<>();
		
		overlayHideTimer = new Timer((int) (Main.preferences.overlayHideDelay.get() * 1000f), p -> {
			clearOBSOverlay();
		});
		overlayUpdateTimer = new Timer(1000, p -> {
			overlayUpdateTimer.stop();
			SwingUtilities.invokeLater(() -> updateOBSOverlay());
		});
		SwingUtilities.invokeLater(() -> updateOBSOverlay());
	}

	private void setupSettingsSubscriptions() {
		Main.preferences.overlayHideDelay.whenModified().subscribe(__ -> updateOBSOverlay());
		Main.preferences.overlayAutoHide.whenModified().subscribe(__ -> updateOBSOverlay());
		Main.preferences.overlayHideWhenLocked.whenModified().subscribe(__ -> updateOBSOverlay());
		Main.preferences.useOverlay.whenModified().subscribe(b -> setOverlayEnabled(b));
		Main.preferences.theme.whenModified().subscribe(__ -> updateTheme());
		Main.preferences.size.whenModified().subscribe(__ -> updateSizePreference());
	}

	private void updateTheme() {
		theme = Theme.get(Main.preferences.theme.get());
		updateFontsAndColors();
		updateOBSOverlay();
	}

	private void updateSizePreference() {
		size = SizePreference.get(Main.preferences.size.get());
		updateFontsAndColors();
		updateBounds();
		SwingUtilities.invokeLater(() -> updateOBSOverlay());
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
		updateFontsAndColors();
	}

	private void updateFontsAndColors() {
		for (ThemedFrame tf : themedFrames) {
			tf.updateFontsAndColors(this);
		}
		for (ThemedComponent tc : themedComponents) {
			tc.updateColors(this);
			tc.updateSize(this);
		}
	}

	private final FontRenderContext frc = new FontRenderContext(null, true, false);

	public int getTextWidth(String text, Font font) {
		return (int) font.getStringBounds(text, frc).getWidth();
	}

	private void updateOBSOverlay() {
//		long time = System.currentTimeMillis();
//		if (time - lastOverlayUpdate < minOverlayUpdateDelayMillis) {
//			overlayUpdateTimer.setInitialDelay((int) (10 + minOverlayUpdateDelayMillis - (time - lastOverlayUpdate)));
//			overlayUpdateTimer.restart();
//			return;
//		}
//		lastOverlayUpdate = time;
//		if (Main.preferences.useOverlay.get()) {
//			BufferedImage img = new BufferedImage(frame.getWidth(), frame.getHeight(), BufferedImage.TYPE_INT_ARGB);
//			boolean hideBecauseLocked = Main.preferences.overlayHideWhenLocked.get() && targetLocked;
//			if (!mainTextArea.isIdle() && !hideBecauseLocked) {
//				frame.paint(img.createGraphics());
//				if (Main.preferences.overlayAutoHide.get()) {
//					overlayHideTimer.setInitialDelay((int) (Main.preferences.overlayHideDelay.get() * 1000f));
//					overlayHideTimer.restart();
//				} else {
//					overlayHideTimer.stop();
//				}
//			}
//			try {
//				ImageIO.write(img, "png", OBS_OVERLAY);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
	}

	private void clearOBSOverlay() {
//		if (Main.preferences.useOverlay.get()) {
//			BufferedImage img = new BufferedImage(frame.getWidth(), frame.getHeight(), BufferedImage.TYPE_INT_ARGB);
//			try {
//				ImageIO.write(img, "png", OBS_OVERLAY);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			if (Main.preferences.overlayAutoHide.get()) {
//				overlayHideTimer.stop();
//			}
//		}
	}

	private void setOverlayEnabled(boolean b) {
		if (b) {
			updateOBSOverlay();
		} else {
			OBS_OVERLAY.delete();
		}
	}

	public void init() {
		updateFontsAndColors();
		updateBounds();
	}

}
