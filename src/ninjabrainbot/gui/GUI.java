package ninjabrainbot.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsDevice.WindowTranslucency;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.geom.RoundRectangle2D;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import ninjabrainbot.Main;
import ninjabrainbot.calculator.Throw;
import ninjabrainbot.calculator.TriangulationResult;
import ninjabrainbot.calculator.Triangulator;
import ninjabrainbot.gui.components.CalibrationPanel;
import ninjabrainbot.gui.components.FlatButton;
import ninjabrainbot.gui.components.JThrowPanel;
import ninjabrainbot.gui.components.JThrowPanelHeader;
import ninjabrainbot.gui.components.NotificationsButton;
import ninjabrainbot.gui.components.ThemedComponent;
import ninjabrainbot.gui.components.ThemedFrame;
import ninjabrainbot.gui.components.ThemedLabel;
import ninjabrainbot.gui.components.TitleBarButton;
import ninjabrainbot.gui.components.TitleBarPanel;
import ninjabrainbot.io.NinjabrainBotPreferences;
import ninjabrainbot.io.VersionURL;
import ninjabrainbot.util.Profiler;

/**
 * Main class for the user interface.
 */
public class GUI {

	public static final int WINDOW_WIDTH = 320;
	public static final int WINDOW_ROUNDING = 7;
	public static final int TITLE_BAR_HEIGHT = 28;
	public static final int THROW_PANEL_HEIGHT = 16;
	public static final int MAIN_TEXT_HEIGHT = 20;
	public static final int BUTTON_HEIGHT = 24;
	public static final int BUTTON_WIDTH = 72;
	public static final int PADDING = 6;
	public static final int MAIN_TEXT_TOP_PADDING = 2;
	public static final int THROW_PANEL_HEADER_HEIGHT = 16;
	public static final int THROW_PANEL_PADDING = 0;
	
	public static final int TITLE_BAR_BUTTON_WH = TITLE_BAR_HEIGHT;
	public static final int MAIN_PANEL_HEIGHT = 3 * MAIN_TEXT_HEIGHT + 2 * MAIN_TEXT_TOP_PADDING;

	public static final String TITLE_TEXT = "Ninjabrain Bot ";
	public static final String CERTAINTY_TEXT = "Certainty: ";
	public static final String VERSION_TEXT =  "v" + Main.VERSION;
	public static final int MAX_THROWS = 10;
	public static final int DEFAULT_SHOWN_THROWS = 3;
	
	public ThemedFrame frame;
	private NotificationsButton notificationsButton;
	private JLabel maintextLabel;
	private JLabel certaintytextLabel;
	private JLabel certaintyLabel;
	private JLabel netherLabel;
	private JLabel versiontextLabel;
	private JLabel throwsLabelBG;
	private JLabel throwsLabel;
	private FlatButton resetButton;
	private FlatButton undoButton;
	private JThrowPanelHeader throwPanelHeader;
	private JThrowPanel[] throwPanels;
	private OptionsFrame optionsFrame;
	private NotificationsFrame notificationsFrame;
	private CalibrationPanel calibrationPanel;
	
	private Font font;
	private Font fontLight;
	public Theme theme;
	public TextSizePreference textSize = TextSizePreference.REGULAR;
	private ArrayList<ThemedComponent> themedComponents;
	
	private TextAnimator textAnimator;
	public Timer autoResetTimer;
	private static int autoResetDelay = 15 * 60 * 1000;
	
	private Triangulator triangulator;
	private ArrayList<Throw> eyeThrows;
	private ArrayList<Throw> eyeThrowsLast;
	private double lastCertainty = 0.0;

	public GUI() {
		theme = Main.preferences.theme.get() == Theme.DARK.name ? Theme.DARK : Theme.LIGHT;
		Locale.setDefault(Locale.US);
		themedComponents = new ArrayList<ThemedComponent>();
		
		Profiler.start("Create frame");
		frame = new ThemedFrame(this, TITLE_TEXT);
		frame.setLocation(Main.preferences.windowX.get(), Main.preferences.windowY.get()); // Set window position
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Load fonts
		Profiler.stopAndStart("Load fonts");
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, Main.class.getResourceAsStream("/resources/OpenSans-Regular.ttf"));
			fontLight = Font.createFont(Font.TRUETYPE_FONT, Main.class.getResourceAsStream("/resources/OpenSans-Light.ttf"));
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(font);
			ge.registerFont(fontLight);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// Set application icon
		Profiler.stopAndStart("Set app icon");
		URL iconURL = Main.class.getResource("/resources/icon.png");
		ImageIcon img = new ImageIcon(iconURL);
		frame.setIconImage(img.getImage());

		Profiler.stopAndStart("Create gui components");
		// Create title bar
		Profiler.start("Create title bar");
		TitleBarPanel titleBar = frame.getTitleBar();
		versiontextLabel = new ThemedLabel(this, VERSION_TEXT) {
			private static final long serialVersionUID = 7210941876032010219L;
			@Override
			public int getTextSize(TextSizePreference p) {
				return p.VERSION_TEXT_SIZE;
			}
			@Override
			public Color getForegroundColor(Theme theme) {
				return theme.TEXT_COLOR_WEAK;
			}
		};
		titleBar.add(versiontextLabel);
		titleBar.addButton(getExitButton());
		titleBar.addButton(getMinimizeButton());
		titleBar.addButton(getSettingsButton());
		notificationsButton = new NotificationsButton(this);
		notificationsFrame = notificationsButton.getNotificationsFrame();
		titleBar.addButton(notificationsButton);

		// Main text
		Profiler.stopAndStart("Create main text area");
		maintextLabel = new ThemedLabel(this, "Waiting for F3+C input...");
		certaintytextLabel = new ThemedLabel(this, "");
		certaintyLabel = new ThemedLabel(this, "") {
			private static final long serialVersionUID = -6995689057641195351L;
			@Override
			public Color getForegroundColor(Theme theme) {
				return theme.CERTAINTY_COLOR_MAP.get(lastCertainty);
			}
		};
		netherLabel = new ThemedLabel(this, "");
		netherLabel.setVisible(Main.preferences.showNetherCoords.get());
		frame.add(maintextLabel);
		frame.add(certaintytextLabel);
		frame.add(certaintyLabel);
		frame.add(netherLabel);
		
		// "Throws" text
		Profiler.stopAndStart("Create main text area");
		throwsLabelBG = new ThemedLabel(this) {
			private static final long serialVersionUID = -8143875137607726122L;
			@Override
			public Color getBackgroundColor(Theme theme) {
				return theme.COLOR_STRONG;
			}
		};
		throwsLabelBG.setOpaque(true);
		throwsLabel = new ThemedLabel(this, "Ender eye throws:", true);
		frame.add(throwsLabel);
		frame.add(throwsLabelBG);
		throwPanelHeader = new JThrowPanelHeader(this);
		frame.add(throwPanelHeader);
		
		// Reset/Undo button
		Profiler.stopAndStart("Create reset/undo buttons");
		resetButton = getResetButton();
		frame.add(resetButton);
		undoButton = getUndoButton();
		frame.add(undoButton);
		
		// Throw panels
		Profiler.stopAndStart("Create throw panels");
		throwPanels = new JThrowPanel[MAX_THROWS];
		for (int i = 0; i < MAX_THROWS; i++) {
			throwPanels[i] = new JThrowPanel(this, i);
			frame.add(throwPanels[i]);
		}
		triangulator = new Triangulator();
		eyeThrows = new ArrayList<Throw>();
		eyeThrowsLast = new ArrayList<Throw>();
		
		// Settings window
		Profiler.stopAndStart("Create settings window");
		optionsFrame = new OptionsFrame(this);
		calibrationPanel = optionsFrame.getCalibrationPanel();
		Profiler.stop();
		
		Profiler.stopAndStart("Create settings window");
		textAnimator = new TextAnimator(theme.TEXT_COLOR_STRONG, theme.TEXT_COLOR_NEUTRAL, 200);
		
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
		autoResetTimer = new Timer(autoResetDelay, p -> {
			resetThrows();
			autoResetTimer.stop();
		});
		
		// Shutdown hook
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				Main.preferences.windowX.set(frame.getX());
				Main.preferences.windowY.set(frame.getY());
			}
		});
	}
	
	public void setTranslucent(boolean t) {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		if (gd.isWindowTranslucencySupported(WindowTranslucency.TRANSLUCENT))
			frame.setOpacity(t ? 0.75f : 1.0f);
	}
	
	public void setAlwaysOnTop(boolean b) {
		frame.setAlwaysOnTop(b);
		optionsFrame.setAlwaysOnTop(b);
		notificationsFrame.setAlwaysOnTop(b);
	}
	
	public void setNotificationsEnabled(boolean b) {
		notificationsButton.setVisible(b && notificationsButton.hasURL());
	}
	
	public void updateTheme() {
		theme = Main.preferences.theme.get() == Theme.DARK.name ? Theme.DARK : Theme.LIGHT;
		updateFontsAndColors();
	}

	public void setNetherCoordsEnabled(boolean b) {
		netherLabel.setVisible(b);
	}

	public void setAdvancedOptionsEnabled(boolean b) {
		optionsFrame.setAdvancedOptionsEnabled(b);
	}

	public void setAngleErrorsEnabled(boolean b) {
		throwPanelHeader.setAngleErrorsEnabled(b);
		for (JThrowPanel p : throwPanels) {
			p.setAngleErrorsEnabled(b);
		}
		updateBounds();
	}
	
	public Font fontSize(float size, boolean light) {
		return light ? fontLight.deriveFont(Font.BOLD, size) : font.deriveFont(Font.BOLD, size);
	}

	private FlatButton getExitButton() {
		URL iconURL = Main.class.getResource("/resources/exit_icon.png");
		ImageIcon img = new ImageIcon(iconURL);
		FlatButton button = new TitleBarButton(this, img) {
			private static final long serialVersionUID = -5122431392273627666L;
			@Override
			public Color getHoverColor(Theme theme) {
				return theme.COLOR_EXIT_BUTTON_HOVER;
			}
		};
		button.addActionListener(p -> System.exit(0));
		return button;
	}

	private FlatButton getMinimizeButton() {
		URL iconURL = Main.class.getResource("/resources/minimize_icon.png");
		ImageIcon img = new ImageIcon(iconURL);
		FlatButton button = new TitleBarButton(this, img);
		button.addActionListener(p -> frame.setState(JFrame.ICONIFIED));
		return button;
	}
	
	private FlatButton getSettingsButton() {
		URL iconURL = Main.class.getResource("/resources/settings_icon.png");
		ImageIcon img = new ImageIcon(iconURL);
		FlatButton button = new TitleBarButton(this, img);
		button.addActionListener(p -> toggleOptionsWindow());
		return button;
	}
	
	
	
	private FlatButton getResetButton() {
		FlatButton button = new FlatButton(this, "Reset");
		button.addActionListener(p -> resetThrows());
		return button;
	}
	
	private FlatButton getUndoButton() {
		FlatButton button = new FlatButton(this, "Undo");
		button.addActionListener(p -> undo());
		return button;
	}
	
	public void registerThemedComponent(ThemedComponent c) {
		themedComponents.add(c);
	}

	private void updateBounds() {
		// Size
		int modifiedWindowHeight = TITLE_BAR_HEIGHT + MAIN_PANEL_HEIGHT + BUTTON_HEIGHT + THROW_PANEL_HEADER_HEIGHT + Math.max(eyeThrows.size(), DEFAULT_SHOWN_THROWS)*THROW_PANEL_HEIGHT;
		frame.setShape(new RoundRectangle2D.Double(0, 0, WINDOW_WIDTH, modifiedWindowHeight, WINDOW_ROUNDING, WINDOW_ROUNDING));
		frame.setSize(WINDOW_WIDTH, modifiedWindowHeight);
		maintextLabel.setBounds(PADDING, MAIN_TEXT_TOP_PADDING + TITLE_BAR_HEIGHT, WINDOW_WIDTH - 2*PADDING, MAIN_TEXT_HEIGHT);
		certaintytextLabel.setBounds(PADDING, MAIN_TEXT_TOP_PADDING + TITLE_BAR_HEIGHT + MAIN_TEXT_HEIGHT, WINDOW_WIDTH - 2*PADDING, MAIN_TEXT_HEIGHT);
		int certaintywidth = getTextWidth(CERTAINTY_TEXT, fontSize(textSize.MAIN_TEXT_SIZE, true));
		certaintyLabel.setBounds(PADDING + certaintywidth, MAIN_TEXT_TOP_PADDING + TITLE_BAR_HEIGHT + MAIN_TEXT_HEIGHT, WINDOW_WIDTH - 2*PADDING - certaintywidth, MAIN_TEXT_HEIGHT);
		netherLabel.setBounds(PADDING, MAIN_TEXT_TOP_PADDING + TITLE_BAR_HEIGHT + MAIN_TEXT_HEIGHT * 2, WINDOW_WIDTH - 2*PADDING, MAIN_TEXT_HEIGHT);
		int titlewidth = getTextWidth(TITLE_TEXT, fontSize(textSize.TITLE_BAR_TEXT_SIZE, false));
		versiontextLabel.setBounds(titlewidth + (TITLE_BAR_HEIGHT - textSize.VERSION_TEXT_SIZE)/2, (textSize.TITLE_BAR_TEXT_SIZE - textSize.VERSION_TEXT_SIZE)/2, 50, TITLE_BAR_HEIGHT);
		int throwPanelY = TITLE_BAR_HEIGHT + MAIN_PANEL_HEIGHT;
		resetButton.setBounds(WINDOW_WIDTH - BUTTON_WIDTH, throwPanelY, BUTTON_WIDTH, BUTTON_HEIGHT);
		undoButton.setBounds(WINDOW_WIDTH - BUTTON_WIDTH*2, throwPanelY, BUTTON_WIDTH, BUTTON_HEIGHT);
		throwsLabelBG.setBounds(0, throwPanelY, WINDOW_WIDTH - BUTTON_WIDTH*2, BUTTON_HEIGHT);
		throwsLabel.setBounds(PADDING, throwPanelY, WINDOW_WIDTH - BUTTON_WIDTH*2 - PADDING, BUTTON_HEIGHT);
		throwPanelHeader.setBounds(0, throwPanelY + BUTTON_HEIGHT, WINDOW_WIDTH, THROW_PANEL_HEADER_HEIGHT);
		for (int i = 0; i < MAX_THROWS; i++) {
			throwPanels[i].setBounds(0, throwPanelY + i*THROW_PANEL_HEIGHT + BUTTON_HEIGHT + THROW_PANEL_HEADER_HEIGHT, WINDOW_WIDTH, THROW_PANEL_HEIGHT);
		}
		frame.updateBounds(this);
		optionsFrame.updateBounds(this);
		notificationsFrame.updateBounds(this);
	}
	
	private void updateFontsAndColors() {
		// Color and font
		frame.getContentPane().setBackground(theme.COLOR_NEUTRAL);
		frame.setBackground(theme.COLOR_NEUTRAL);
		textAnimator.setColors(theme.TEXT_COLOR_STRONG, theme.TEXT_COLOR_NEUTRAL);
		optionsFrame.updateFontsAndColors();
		notificationsFrame.updateFontsAndColors();
		for (ThemedComponent tc : themedComponents) {
			tc.updateColors(this);
			tc.updateFont(this);
		}
	}
	
	private FontRenderContext frc = new FontRenderContext(null, true, false);
	private int getTextWidth(String text, Font font) {
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

	public void resetThrows() {
		if (eyeThrows.size() > 0) {
			ArrayList<Throw> temp = eyeThrowsLast;
			eyeThrowsLast = eyeThrows;
			eyeThrows = temp;
			eyeThrows.clear();
			onThrowsUpdated();
		}
	}
	
	public void undo() {
		ArrayList<Throw> temp = eyeThrowsLast;
		eyeThrowsLast = eyeThrows;
		eyeThrows = temp;
		onThrowsUpdated();
	}
	
	public void removeThrow(Throw t) {
		if (eyeThrows.contains(t)) {
			saveThrowsForUndo();
			eyeThrows.remove(t);
			onThrowsUpdated();
		}
	}
	
	private void processClipboardUpdate(String clipboard) {
		Throw t = Throw.parseF3C(clipboard);
		if (!calibrationPanel.isCalibrating()) {
			int i = eyeThrows.size();
			if (t != null && i < MAX_THROWS && shouldAddThrow(t)) {
				saveThrowsForUndo();
				eyeThrows.add(t);
				throwPanels[i].setThrow(t);
				textAnimator.setJThrowPanel(throwPanels[i]);
				onThrowsUpdated();
			}
		} else {
			if (t != null && shouldAddThrow(t)) {
				try {
					calibrationPanel.add(t);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void changeLastAngle(double delta) {
		if (!calibrationPanel.isCalibrating()) {
			int i = eyeThrows.size() - 1;
			if (i == -1)
				return;
			Throw last = eyeThrows.get(i);
			Throw t = new Throw(last.x, last.z, last.alpha + delta, last.correction + delta);
			saveThrowsForUndo();
			eyeThrows.remove(last);
			eyeThrows.add(t);
			throwPanels[i].setThrow(t);
			textAnimator.setJThrowPanel(throwPanels[i]);
			onThrowsUpdated();
		}
	}
	
	private void setUpdateURL(VersionURL url) {
		notificationsButton.setURL(url);
	}
	
	/**
	 * Returns true if the newly inputed throw t should be added.
	 */
	private boolean shouldAddThrow(Throw t) {
//		for (Throw existing : eyeThrows) {
//			// Dont add if closer than 1 block to existing throw
//			if (t.distance2(existing) < 1.0) {
//				return false;
//			}
//		}
		return true;
	}
	
	public void recalculateStronghold() {
		onThrowsUpdated();
	}
	
	private void onThrowsUpdated() {
		if (eyeThrows.size() >= 1) {
			TriangulationResult result = triangulator.triangulate(eyeThrows);
			if (result.success) {
				int distance = result.getDistance(eyeThrows.get(eyeThrows.size() - 1));
				lastCertainty = result.weight;
				switch (Main.preferences.strongholdDisplayType.get()) {
				case NinjabrainBotPreferences.FOURFOUR:
					maintextLabel.setText(String.format(Locale.US, "Location: (%d, %d), %d blocks away ", result.fourfour_x, result.fourfour_z, distance));
					break;
				case NinjabrainBotPreferences.EIGHTEIGHT:
					maintextLabel.setText(String.format(Locale.US, "Location: (%d, %d), %d blocks away ", result.fourfour_x + 4, result.fourfour_z + 4, distance));
					break;
				case NinjabrainBotPreferences.CHUNK:
					maintextLabel.setText(String.format(Locale.US, "Chunk: (%d, %d), %d blocks away ", result.x, result.z, distance));
					break;
				}
				certaintytextLabel.setText(CERTAINTY_TEXT);
				certaintyLabel.setText(String.format(Locale.US, "%.1f%%", result.weight*100.0));
				netherLabel.setText(String.format(Locale.US, "Nether coordinates: (%d, %d)", result.x*2, result.z*2));
				double[] errors = result.getAngleErrors(eyeThrows);
				for (int i = 0; i < throwPanels.length; i++) {
					JThrowPanel throwPanel = throwPanels[i];
					if (i < errors.length)
						throwPanel.setError(errors[i]);
					else
						throwPanel.setError("");
				}
			} else {
				maintextLabel.setText("The throws do not intersect.");
				certaintytextLabel.setText("Try undoing your last throw.");
				certaintyLabel.setText("");
				netherLabel.setText("");
				for (int i = 0; i < throwPanels.length; i++) {
					throwPanels[i].setError("");
				}
			}
		} else {
			maintextLabel.setText("Waiting for F3+C input...");
			certaintytextLabel.setText("");
			certaintyLabel.setText("");
			netherLabel.setText("");
			for (int i = 0; i < throwPanels.length; i++) {
				throwPanels[i].setError("");
			}
		}
		// Update throw panels
		for (int i = 0; i < throwPanels.length; i++) {
			JThrowPanel throwPanel = throwPanels[i];
			throwPanel.setThrow(i < eyeThrows.size() ? eyeThrows.get(i) : null);
		}
		// Update auto reset timer
		if (Main.preferences.autoReset.get()) {
			autoResetTimer.restart();
		}
		// Update bounds
		updateBounds();
		certaintyLabel.setForeground(theme.CERTAINTY_COLOR_MAP.get(lastCertainty));
	}

	public void onClipboardUpdated(String newClipboard) {
		SwingUtilities.invokeLater(() -> processClipboardUpdate(newClipboard));
	}
	
	public void onNewUpdateAvailable(VersionURL url) {
		SwingUtilities.invokeLater(() -> setUpdateURL(url));
	}
	
	private void saveThrowsForUndo() {
		eyeThrowsLast.clear();
		for (int i = 0; i < eyeThrows.size(); i++) {
			eyeThrowsLast.add(eyeThrows.get(i));
		}
	}
	
	public Triangulator getTriangulator() {
		return this.triangulator;
	}

	private void checkIfOffScreen(JFrame frame) {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice lstGDs[] = ge.getScreenDevices();
		for (GraphicsDevice gd : lstGDs) {
			if (gd.getDefaultConfiguration().getBounds().contains(frame.getBounds()))
				return;
		}
		frame.setLocation(100, 100);
	}

}
