package ninjabrainbot.gui.frames;

import java.awt.Rectangle;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JFrame;

import ninjabrainbot.gui.options.CalibrationPanel;
import ninjabrainbot.gui.options.sections.ThemeSelectionPanel;
import ninjabrainbot.gui.options.sections.AdvancedOptionsPanel;
import ninjabrainbot.gui.options.sections.BasicOptionsPanel;
import ninjabrainbot.gui.options.sections.HotkeyOptionsPanel;
import ninjabrainbot.gui.options.sections.LanguageOptionsPanel;
import ninjabrainbot.gui.options.sections.ObsOptionsPanel;
import ninjabrainbot.gui.options.sections.OptionalFeaturesPanel;
import ninjabrainbot.gui.components.layout.ThemedTabbedPane;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.io.KeyboardListener;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.util.I18n;

public class OptionsFrame extends ThemedFrame {

	private ThemedTabbedPane tabbedPane;

	private CalibrationPanel calibrationPanel;

	public static int WINDOW_WIDTH = 560;
	public static int COLUMN_WIDTH = WINDOW_WIDTH / 2;
	public static int PADDING = 6;

	private static final String TITLE_TEXT = I18n.get("settings");

	public OptionsFrame(StyleManager styleManager, NinjabrainBotPreferences preferences) {
		super(styleManager, preferences, TITLE_TEXT);
		setLayout(null);
		tabbedPane = new ThemedTabbedPane(styleManager);
		add(tabbedPane);
		calibrationPanel = new CalibrationPanel(styleManager, preferences, this);
		add(calibrationPanel);

		tabbedPane.addTab(I18n.get("settings.basic"), new BasicOptionsPanel(styleManager, preferences));
		tabbedPane.addTab(I18n.get("settings.advanced"), new AdvancedOptionsPanel(styleManager, preferences, calibrationPanel, sh));
		tabbedPane.addTab(I18n.get("settings.theme"), new ThemeSelectionPanel(styleManager, preferences, this));
		tabbedPane.addTab(I18n.get("settings.keyboard_shortcuts"), new HotkeyOptionsPanel(styleManager, preferences));
		tabbedPane.addTab(I18n.get("settings.overlay"), new ObsOptionsPanel(styleManager, preferences, sh));
		tabbedPane.addTab(I18n.get("settings.language"), new LanguageOptionsPanel(styleManager, preferences));
		tabbedPane.addTab(I18n.get("settings.optional_features"), new OptionalFeaturesPanel(styleManager, preferences, sh));

		// Title bar
		titlebarPanel.setFocusable(true);

		// Subscriptions
		sh.add(preferences.alwaysOnTop.whenModified().subscribe(b -> setAlwaysOnTop(b)));
	}

	public void stopCalibrating() {
//		tabbedPane.setVisible(true);
//		titlebarPanel.setVisible(true);
//		calibrationPanel.setVisible(false);
//		calibrationPanel.cancel();
//		updateBounds(styleManager);
//		sigma.updateValue();
	}

	public void updateBounds(StyleManager styleManager) {
		WINDOW_WIDTH = styleManager.size.WIDTH / 4 * (I18n.localeRequiresExtraSpace() ? 8 : 7);
		COLUMN_WIDTH = WINDOW_WIDTH / 2;
		int titleBarHeight = titlebarPanel.getPreferredSize().height;
		int panelHeight = tabbedPane.getPreferredSize().height;
		titlebarPanel.setBounds(0, 0, WINDOW_WIDTH, titleBarHeight);
		super.updateBounds(styleManager);
		if (!calibrationPanel.isVisible()) {
			tabbedPane.setBounds(0, titleBarHeight, WINDOW_WIDTH, panelHeight);
			setSize(WINDOW_WIDTH, titleBarHeight + panelHeight);
		} else {
			calibrationPanel.setBounds(0, 0, WINDOW_WIDTH, 2 * panelHeight + titleBarHeight);
			setSize(WINDOW_WIDTH, titleBarHeight + 2 * panelHeight);
		}
		setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), styleManager.size.WINDOW_ROUNDING, styleManager.size.WINDOW_ROUNDING));
	}

	@Override
	protected void onExitButtonClicked() {
		close();
	}

	public void close() {
		setVisible(false);
		stopCalibrating();
		if (KeyboardListener.registered) {
			KeyboardListener.instance.cancelConsumer();
		}
	}

	public CalibrationPanel getCalibrationPanel() {
		return calibrationPanel;
	}

	public void toggleWindow(JFrame parent) {
		if (isVisible()) {
			close();
		} else {
			setVisible(true);
			Rectangle bounds = parent.getBounds();
			setLocation(bounds.x + 40, bounds.y + 30);
		}
	}

}