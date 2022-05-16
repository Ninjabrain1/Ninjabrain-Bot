package ninjabrainbot.gui;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsDevice;
import java.awt.GraphicsDevice.WindowTranslucency;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import ninjabrainbot.Main;
import ninjabrainbot.calculator.BlindResult;
import ninjabrainbot.calculator.Calculator;
import ninjabrainbot.calculator.CalculatorResult;
import ninjabrainbot.calculator.ChunkPrediction;
import ninjabrainbot.calculator.DivineContext;
import ninjabrainbot.calculator.DivineResult;
import ninjabrainbot.calculator.Fossil;
import ninjabrainbot.calculator.Throw;
import ninjabrainbot.gui.components.CalibrationPanel;
import ninjabrainbot.gui.components.EnderEyePanel;
import ninjabrainbot.gui.components.MainButtonPanel;
import ninjabrainbot.gui.components.MainTextArea;
import ninjabrainbot.gui.components.NinjabrainBotFrame;
import ninjabrainbot.gui.components.ThemedComponent;
import ninjabrainbot.io.NinjabrainBotPreferences;
import ninjabrainbot.io.VersionURL;
import ninjabrainbot.util.I18n;
import ninjabrainbot.util.Profiler;

/**
 * Main class for the user interface.
 */
public class GUI {

	private final MainTextArea mainTextArea;
	private final EnderEyePanel enderEyePanel;

	public NinjabrainBotFrame frame;
	public OptionsFrame optionsFrame;
	private final NotificationsFrame notificationsFrame;
	private final CalibrationPanel calibrationPanel;

	public Theme theme;
	public SizePreference size;
	private final ArrayList<ThemedComponent> themedComponents;

	public Timer autoResetTimer;
	private static final int AUTO_RESET_DELAY = 15 * 60 * 1000;

	public static final int MAX_THROWS = 10;
	private final Calculator calculator;
	private CalculatorResult latestResult;
	private ArrayList<Throw> eyeThrows;
	private ArrayList<Throw> eyeThrowsLast;
	private DivineContext divineContext;
	private DivineContext divineContextLast;

	private boolean targetLocked = false;

	private Font font;
	private HashMap<String, Font> fonts;
	
	public final File OBS_OVERLAY;

	public GUI() {
		theme = Theme.get(Main.preferences.theme.get());
		size = SizePreference.get(Main.preferences.size.get());
		font = new Font(null, Font.BOLD, 25);
		Locale.setDefault(Locale.US);
		themedComponents = new ArrayList<>();
		calculator = new Calculator();
		eyeThrows = new ArrayList<>();
		eyeThrowsLast = new ArrayList<>();

		Profiler.start("Create frame");
		frame = new NinjabrainBotFrame(this);
		notificationsFrame = frame.getNotificationsFrame();

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

		Profiler.stopAndStart("Create gui components");
		// Main text
		Profiler.stopAndStart("Create main text area");
		mainTextArea = new MainTextArea(this);
		frame.add(mainTextArea);

		// "Throws" text
		Profiler.stopAndStart("Create main button area");
		MainButtonPanel mainButtonPanel = new MainButtonPanel(this);
		frame.add(mainButtonPanel);

		// Throw panels
		Profiler.stopAndStart("Create throw panels");
		enderEyePanel = new EnderEyePanel(this);
		frame.add(enderEyePanel);

		// Settings window
		Profiler.stopAndStart("Create settings window");
		optionsFrame = new OptionsFrame(this);
		calibrationPanel = optionsFrame.getCalibrationPanel();
		Profiler.stop();

		Profiler.stopAndStart("Update fonts and colors");
		updateFontsAndColors();
		Profiler.stopAndStart("Update bounds");
		updateBounds();
		checkIfOffScreen(frame);
		Profiler.stopAndStart("Set visible");
		frame.setVisible(true);
		Profiler.stopAndStart("Set translucency");
		setTranslucent(Main.preferences.translucent.get());

		// Auto reset timer
		autoResetTimer = new Timer(AUTO_RESET_DELAY, p -> {
			resetThrows();
			autoResetTimer.stop();
		});
		SwingUtilities.invokeLater(() -> updateOBSOverlay());
	}
	
	public void setTranslucent(boolean t) {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		if (gd.isWindowTranslucencySupported(WindowTranslucency.TRANSLUCENT)) {
			frame.setOpacity(t ? 0.75f : 1.0f);
		}
	}

	public void setAlwaysOnTop(boolean b) {
		frame.setAlwaysOnTop(b);
		optionsFrame.setAlwaysOnTop(b);
		notificationsFrame.setAlwaysOnTop(b);
	}

	public void setNotificationsEnabled(boolean b) {
		frame.getNotificationsButton().setVisible(b && frame.getNotificationsButton().hasURL());
	}

	public void updateTheme() {
		theme = Theme.get(Main.preferences.theme.get());
		updateFontsAndColors();
		updateOBSOverlay();
	}

	public void updateSizePreference() {
		size = SizePreference.get(Main.preferences.size.get());
		updateFontsAndColors();
		updateBounds();
		SwingUtilities.invokeLater(() -> updateOBSOverlay());
	}

	public void setNetherCoordsEnabled(boolean b) {
		mainTextArea.setNetherCoordsEnabled(b);
	}

	public void setAngleErrorsEnabled(boolean b) {
		enderEyePanel.setAngleErrorsEnabled(b);
		updateBounds();
	}

	public void setAngleUpdatesEnabled(boolean b) {
		mainTextArea.setAngleUpdatesEnabled(b);
		updateBounds();
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

	private void updateBounds() {
		for (ThemedComponent tc : themedComponents) {
			tc.updateSize(this);
		}
		updateFontsAndColors();
		frame.updateBounds(this);
		optionsFrame.updateBounds(this);
		notificationsFrame.updateBounds(this);
		int extraWidth = Main.preferences.showAngleUpdates.get() && Main.preferences.view.get().equals(NinjabrainBotPreferences.DETAILED) ? size.ANGLE_COLUMN_WIDTH : 0;
		frame.setSize(size.WIDTH + extraWidth, frame.getPreferredSize().height);
		frame.setShape(new RoundRectangle2D.Double(0, 0, frame.getWidth(), frame.getHeight(), size.WINDOW_ROUNDING, size.WINDOW_ROUNDING));
	}

	private void updateFontsAndColors() {
		// Color and font
		frame.getContentPane().setBackground(theme.COLOR_NEUTRAL);
		frame.setBackground(theme.COLOR_NEUTRAL);
		optionsFrame.updateFontsAndColors();
		notificationsFrame.updateFontsAndColors();
		for (ThemedComponent tc : themedComponents) {
			tc.updateColors(this);
			tc.updateSize(this);
		}
	}

	private final FontRenderContext frc = new FontRenderContext(null, true, false);
	public int getTextWidth(String text, Font font) {
		return (int) font.getStringBounds(text, frc).getWidth();
	}

	public void toggleOptionsWindow() {
		if (optionsFrame.isVisible()) {
			optionsFrame.close();
		} else {
			optionsFrame.setVisible(true);
			Rectangle bounds = frame.getBounds();
			optionsFrame.setLocation(bounds.x + 40, bounds.y + 30);
		}
	}

	public void toggleMinimized() {
		frame.toggleMinimized();
	}

	public void resetThrows() {
		mainTextArea.onReset();
		if (eyeThrows.size() > 0 || divineContext != null) {
			ArrayList<Throw> temp = eyeThrowsLast;
			eyeThrowsLast = eyeThrows;
			eyeThrows = temp;
			eyeThrows.clear();
			divineContextLast = divineContext;
			divineContext = null;
			latestResult = null;
			setTargetLocked(false);
		}
		onThrowsUpdated();
	}

	public void undo() {
		setTargetLocked(false);
		ArrayList<Throw> temp = eyeThrowsLast;
		eyeThrowsLast = eyeThrows;
		eyeThrows = temp;
		DivineContext temp2 = divineContextLast;
		divineContextLast = divineContext;
		divineContext = temp2;
		onThrowsUpdated();
	}

	public void removeThrow(Throw t) {
		if (eyeThrows.contains(t)) {
			saveThrowsForUndo();
			eyeThrows.remove(t);
			onThrowsUpdated();
		}
	}
	
	public void removeDivineContext() {
		if (divineContext != null) {
			saveThrowsForUndo();
			divineContext = null;
			onThrowsUpdated();
		}
	}

	private void processClipboardUpdate(String clipboard) {
		Throw t = Throw.parseF3C(clipboard);
		if (!calibrationPanel.isCalibrating()) {
			int i = eyeThrows.size();
			if (t != null) {
				if (t.isNether()) {
					if (i > 0) {
						onAngleUpdate(t, true);
					} else {
						BlindResult result = calculator.blind(t.toBlind(), divineContext, true);
						mainTextArea.setResult(result, this);
						if (Main.preferences.autoReset.get()) {
							autoResetTimer.restart();
						}
						SwingUtilities.invokeLater(() -> updateOBSOverlay());
					}
					return;
				}
				if (i < MAX_THROWS) {
					if (i > 0 && (targetLocked || t.lookingBelowHorizon())) {
						onAngleUpdate(t, true);
					} else if (!targetLocked && !t.lookingBelowHorizon()) {
						updateWithNewThrow(t, i);
					}
				}
			} else {
				Fossil f = Fossil.parseF3I(clipboard);
				if (f != null) {
					saveThrowsForUndo();
					divineContext = new DivineContext(f);
					onThrowsUpdated();
				}
			}
		} else {
			if (t != null) {
				try {
					calibrationPanel.add(t);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void updateWithNewThrow(Throw t, int index) {
		saveThrowsForUndo();
		eyeThrows.add(t);
		enderEyePanel.setThrow(index, t);
		onThrowsUpdated();
	}

	public void changeLastAngle(double delta) {
		if (!calibrationPanel.isCalibrating()) {
			int i = eyeThrows.size() - 1;
			if (i == -1) {
				return;
			}
			Throw last = eyeThrows.get(i);
			Throw t = new Throw(last.x, last.z, last.alpha + delta, last.beta, last.correction + delta, last.altStd, last.isNether());
			saveThrowsForUndo();
			eyeThrows.remove(last);
			eyeThrows.add(t);
			enderEyePanel.setThrow(i, t);
			onThrowsUpdated();
		} else {
			calibrationPanel.changeLastAngle(delta);
		}
	}

	public void toggleLastSTD() {
		if (!calibrationPanel.isCalibrating()) {
			int i = eyeThrows.size() - 1;
			if (i == -1) {
				return;
			}
			Throw last = eyeThrows.get(i);
			Throw t = last.withToggledSTD();
			saveThrowsForUndo();
			eyeThrows.remove(last);
			eyeThrows.add(t);
			enderEyePanel.setThrow(i, t);
			onThrowsUpdated();
		}
	}

	private void setTargetLocked(boolean locked) {
		targetLocked = locked;
		frame.setLocked(locked);
		updateBounds();
	}

	public void toggleTargetLocked() {
		if (latestResult != null) {
			setTargetLocked(!targetLocked);
		}
	}

	public boolean isTargetLocked() {
		return targetLocked;
	}

	private void setUpdateURL(VersionURL url) {
		frame.setURL(url);
	}

	public void recalculateStronghold() {
		onThrowsUpdated();
	}

	private void onThrowsUpdated() {
		if (eyeThrows.size() == 0 && divineContext != null) {
			DivineResult result = calculator.divine(divineContext.fossil);
			mainTextArea.setResult(result, this);
			enderEyePanel.setErrors(null);
		} else {
			double[] errors = null;
			if (eyeThrows.size() >= 1) {
				latestResult = calculator.triangulate(eyeThrows, divineContext);
				if (latestResult.success()) {
					errors = latestResult.getAngleErrors();
					Throw latestThrow = eyeThrows.get(eyeThrows.size() - 1);
					onAngleUpdate(latestThrow, false);
				}
			} else {
				latestResult = null;
			}
			mainTextArea.setResult(latestResult, this);
			enderEyePanel.setErrors(errors);
		}
		// Update throw panels
		enderEyePanel.setThrows(eyeThrows, divineContext);
		// Update auto reset timer
		if (Main.preferences.autoReset.get()) {
			autoResetTimer.restart();
		}
		// Update bounds
		updateBounds();
		// Update overlay
		SwingUtilities.invokeLater(() -> updateOBSOverlay());
	}

	private void onAngleUpdate(Throw updateThrow, boolean updateGui) {
		if (latestResult != null && latestResult.success()) {
			latestResult.getBestPrediction().updateWithTravelAngle(updateThrow);
			List<ChunkPrediction> topPredictions = latestResult.getTopPredictions(SizePreference.NUM_DETAILED_PANELS);
			for (ChunkPrediction topPrediction : topPredictions) {
				topPrediction.updateWithTravelAngle(updateThrow);
			}
			if (updateGui) {
				mainTextArea.setResult(latestResult, this);
			}
		}
	}

	public void onClipboardUpdated(String newClipboard) {
		SwingUtilities.invokeLater(() -> processClipboardUpdate(newClipboard));
	}

	public void onNewUpdateAvailable(VersionURL url) {
		SwingUtilities.invokeLater(() -> setUpdateURL(url));
	}

	private void saveThrowsForUndo() {
		eyeThrowsLast.clear();
		eyeThrowsLast.addAll(eyeThrows);
		divineContextLast = divineContext;
	}

	public Calculator getTriangulator() {
		return this.calculator;
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
		if (Main.preferences.useOverlay.get()) {
			BufferedImage img = new BufferedImage(frame.getWidth(), frame.getHeight(), BufferedImage.TYPE_INT_ARGB);
			if (!mainTextArea.isIdle()) {
				frame.paint(img.createGraphics());
			}
			try {
				ImageIO.write(img, "png", OBS_OVERLAY);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void clearOBSOverlay() {
		if (Main.preferences.useOverlay.get()) {
			BufferedImage img = new BufferedImage(frame.getWidth(), frame.getHeight(), BufferedImage.TYPE_INT_ARGB);
			try {
				ImageIO.write(img, "png", OBS_OVERLAY);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void setOverlayEnabled(boolean b) {
		if (b) {
			updateOBSOverlay();
		} else {
			OBS_OVERLAY.delete();
		}
	}

}
