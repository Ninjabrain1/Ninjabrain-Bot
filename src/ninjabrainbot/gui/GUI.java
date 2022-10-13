package ninjabrainbot.gui;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import ninjabrainbot.Main;
import ninjabrainbot.calculator.Calculator;
import ninjabrainbot.calculator.DataState;
import ninjabrainbot.calculator.DataStateHandler;
import ninjabrainbot.calculator.IDataState;
import ninjabrainbot.calculator.IDataStateHandler;
import ninjabrainbot.calculator.IThrow;
import ninjabrainbot.calculator.StandardStdProfile;
import ninjabrainbot.gui.components.NinjabrainBotFrame;
import ninjabrainbot.gui.components.ThemedComponent;
import ninjabrainbot.io.AutoResetTimer;
import ninjabrainbot.io.ClipboardReader;
import ninjabrainbot.io.KeyboardListener;
import ninjabrainbot.io.NinjabrainBotPreferences;
import ninjabrainbot.util.I18n;
import ninjabrainbot.util.Profiler;

/**
 * Main class for the user interface.
 */
public class GUI {

	public NinjabrainBotFrame frame;
	public OptionsFrame optionsFrame;

	public Theme theme;
	public SizePreference size;
	private final ArrayList<ThemedComponent> themedComponents;

	private Timer overlayHideTimer;
	private Timer overlayUpdateTimer;
	private long lastOverlayUpdate = System.currentTimeMillis();
	private final long minOverlayUpdateDelayMillis = 1000;
	public Timer autoResetTimer;
	private boolean targetLocked = false;

	private Font font;
	private HashMap<String, Font> fonts;

	public final File OBS_OVERLAY;
	
	private final IDataState dataState;
	private final IDataStateHandler dataStateHandler;

	public GUI() {
		ClipboardReader clipboardReader = new ClipboardReader();
		Thread clipboardThread = new Thread(clipboardReader);
		KeyboardListener.init(clipboardReader);
		clipboardThread.start();
		
		setupHotkeys();
		setupSettingsSubscriptions();
		dataState = new DataState(new Calculator(), clipboardReader.whenNewThrowInputed(), clipboardReader.whenNewFossilInputed(), new StandardStdProfile());
		dataStateHandler = new DataStateHandler();
		
		// OLD
		theme = Theme.get(Main.preferences.theme.get());
		size = SizePreference.get(Main.preferences.size.get());
		font = new Font(null, Font.BOLD, 25);
		Locale.setDefault(Locale.US);
		themedComponents = new ArrayList<>();

		Profiler.start("Create frame");
		frame = new NinjabrainBotFrame(this, dataState, dataStateHandler);

		OBS_OVERLAY = new File(System.getProperty("java.io.tmpdir"), "nb-overlay.png");

		// Load fonts
		Profiler.stopAndStart("Load fonts");
		font = loadFont();
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		ge.registerFont(font);
		fonts = new HashMap<>();

		// Set application icon
		Profiler.stopAndStart("Set app icon");
		URL iconURL = Main.class.getResource("/resources/icon.png");
		ImageIcon img = new ImageIcon(iconURL);
		frame.setIconImage(img.getImage());

		

		// Settings window
		Profiler.stopAndStart("Create settings window");
		optionsFrame = new OptionsFrame(this);
		frame.getSettingsButton().addActionListener(__ -> toggleOptionsWindow());
		Profiler.stop();

		Profiler.stopAndStart("Update fonts and colors");
		updateFontsAndColors();
		Profiler.stopAndStart("Update bounds");
		updateBounds();
		checkIfOffScreen(frame);
		Profiler.stopAndStart("Set visible");
		frame.setVisible(true);

		// Auto reset timer
		autoResetTimer = new AutoResetTimer(dataState);
		overlayHideTimer = new Timer((int) (Main.preferences.overlayHideDelay.get() * 1000f), p -> {
			clearOBSOverlay();
		});
		overlayUpdateTimer = new Timer(1000, p -> {
			overlayUpdateTimer.stop();
			SwingUtilities.invokeLater(() -> updateOBSOverlay());
		});
		SwingUtilities.invokeLater(() -> updateOBSOverlay());
		
		// Shutdown hook
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				Main.preferences.windowX.set(frame.getX());
				Main.preferences.windowY.set(frame.getY());
				clearOBSOverlay();
			}
		});
	}
	
	private void setupHotkeys() {
		Main.preferences.hotkeyReset.whenTriggered().subscribe(__ -> resetCalculatorIfNotLocked());
		Main.preferences.hotkeyUndo.whenTriggered().subscribe(__ -> undoIfNotLocked());
		Main.preferences.hotkeyIncrement.whenTriggered().subscribe(__ -> changeLastAngleIfNotLocked(0.01));
		Main.preferences.hotkeyDecrement.whenTriggered().subscribe(__ -> changeLastAngleIfNotLocked(-0.01));
		Main.preferences.hotkeyAltStd.whenTriggered().subscribe(__ -> toggleAltStdOnLastThrowIfNotLocked());
		Main.preferences.hotkeyLock.whenTriggered().subscribe(__ -> dataState.toggleLocked());
	}
	
	private void setupSettingsSubscriptions() {
		Main.preferences.overlayHideDelay.whenModified().subscribe(__ -> updateOBSOverlay());
		Main.preferences.overlayAutoHide.whenModified().subscribe(__ -> updateOBSOverlay());
		Main.preferences.overlayHideWhenLocked.whenModified().subscribe(__ -> updateOBSOverlay());
		Main.preferences.useOverlay.whenModified().subscribe(b -> setOverlayEnabled(b));
		Main.preferences.autoReset.whenModified().subscribe(b -> onAutoResetEnabledChanged(b));
		Main.preferences.theme.whenModified().subscribe(__ -> updateTheme());
		Main.preferences.size.whenModified().subscribe(__ -> updateSizePreference());
	}
	
	private void resetCalculatorIfNotLocked() {
		if (!targetLocked)
			dataState.reset();
	}

	private void undoIfNotLocked() {
		// TODO
	}

	private void changeLastAngleIfNotLocked(double delta) {
		if (!targetLocked && dataState.getThrowSet().size() != 0) {
			IThrow last = dataState.getThrowSet().getLast();
			if (last != null)
				last.addCorrection(delta);
		}
	}
	
	private void toggleAltStdOnLastThrowIfNotLocked() {
		if (!targetLocked && dataState.getThrowSet().size() != 0) {
			IThrow last = dataState.getThrowSet().getLast();
			int stdProfile = last.getStdProfileNumber();
			switch (stdProfile) {
			case StandardStdProfile.NORMAL:
				last.setStdProfileNumber(StandardStdProfile.ALTERNATIVE);
				break;
			case StandardStdProfile.ALTERNATIVE:
				last.setStdProfileNumber(StandardStdProfile.NORMAL);
				break;
			case StandardStdProfile.MANUAL:
				break;
			}
		}
	}

	private void onAutoResetEnabledChanged(boolean b) {
		if (b) {
			autoResetTimer.start();
		} else {
			autoResetTimer.stop();
		}
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
			font = Font.createFont(Font.TRUETYPE_FONT,
					Main.class.getResourceAsStream("/resources/OpenSans-Regular.ttf"));
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

	private void updateBounds() {
		for (ThemedComponent tc : themedComponents) {
			tc.updateSize(this);
		}
		updateFontsAndColors();
		frame.updateBounds(this);
		optionsFrame.updateBounds(this);
//		notificationsFrame.updateBounds(this);
		int extraWidth = Main.preferences.showAngleUpdates.get()
				&& Main.preferences.view.get().equals(NinjabrainBotPreferences.DETAILED) ? size.ANGLE_COLUMN_WIDTH : 0;
		frame.setSize(size.WIDTH + extraWidth, frame.getPreferredSize().height);
		frame.setShape(new RoundRectangle2D.Double(0, 0, frame.getWidth(), frame.getHeight(), size.WINDOW_ROUNDING,
				size.WINDOW_ROUNDING));
	}

	private void updateFontsAndColors() {
		// Color and font
		frame.getContentPane().setBackground(theme.COLOR_NEUTRAL);
		frame.setBackground(theme.COLOR_NEUTRAL);
		optionsFrame.updateFontsAndColors();
//		notificationsFrame.updateFontsAndColors();
		for (ThemedComponent tc : themedComponents) {
			tc.updateColors(this);
			tc.updateSize(this);
		}
	}

	private final FontRenderContext frc = new FontRenderContext(null, true, false);

	public int getTextWidth(String text, Font font) {
		return (int) font.getStringBounds(text, frc).getWidth();
	}

	private void toggleOptionsWindow() {
		if (optionsFrame.isVisible()) {
			optionsFrame.close();
		} else {
			optionsFrame.setVisible(true);
			Rectangle bounds = frame.getBounds();
			optionsFrame.setLocation(bounds.x + 40, bounds.y + 30);
		}
	}

	private void checkIfOffScreen(JFrame frame) {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		for (GraphicsDevice gd : ge.getScreenDevices()) {
			if (gd.getDefaultConfiguration().getBounds().contains(frame.getBounds())) {
				return;
			}
		}
		frame.setLocation(100, 100);
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

}
