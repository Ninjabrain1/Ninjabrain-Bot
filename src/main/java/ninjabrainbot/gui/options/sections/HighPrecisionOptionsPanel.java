package ninjabrainbot.gui.options.sections;

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import ninjabrainbot.event.DisposeHandler;
import ninjabrainbot.gui.components.layout.StackPanel;
import ninjabrainbot.gui.components.preferences.CheckboxPanel;
import ninjabrainbot.gui.components.preferences.FloatPreferencePanel;
import ninjabrainbot.gui.components.preferences.HotkeyPanel;
import ninjabrainbot.gui.frames.OptionsFrame;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.io.KeyboardListener;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.util.I18n;

public class HighPrecisionOptionsPanel extends JPanel {

	private final CheckboxPanel sensitivityCheckbox;
	private final FloatPreferencePanel resolutionHeight;
	private final FloatPreferencePanel sensitivity;
	private final FloatPreferencePanel boatErrorLimit;
	private final FloatPreferencePanel sigmaBoat;
	private HotkeyPanel enterBoatHotkey;

	public HighPrecisionOptionsPanel(StyleManager styleManager, NinjabrainBotPreferences preferences, DisposeHandler disposeHandler) {
		setOpaque(false);
		setLayout(new GridLayout(1, 2, 2 * OptionsFrame.PADDING, 0));
		setBorder(new EmptyBorder(2 * OptionsFrame.PADDING, 2 * OptionsFrame.PADDING, 2 * OptionsFrame.PADDING, 2 * OptionsFrame.PADDING));
		JPanel column1 = new StackPanel();
		JPanel column2 = new StackPanel();
		column1.setOpaque(false);
		column2.setOpaque(false);
		add(column1);
		add(column2);

		// Tall Res Column
		column1.add(new CheckboxPanel(styleManager, I18n.get("settings.tall_resolution"), preferences.useTallRes));
		resolutionHeight = new FloatPreferencePanel(styleManager, I18n.get("settings.resolution_height"), preferences.resolutionHeight);
		resolutionHeight.setDecimals(0);
		resolutionHeight.setEnabled(preferences.useTallRes.get());
		column1.add(resolutionHeight);

		// Precise Sens Column
		sensitivityCheckbox = new CheckboxPanel(styleManager, I18n.get("settings.use_precise_angle"), preferences.usePreciseAngle);
		sensitivityCheckbox.setEnabled(preferences.useTallRes.get());
		column2.add(sensitivityCheckbox);

		sensitivity = new FloatPreferencePanel(styleManager, I18n.get("settings.sensitivity"), preferences.sensitivity);
		sensitivity.setWidth(150);
		sensitivity.setDecimals(9);
		sensitivity.setEnabled(preferences.usePreciseAngle.get() && preferences.useTallRes.get());
		column2.add(sensitivity);
		if (KeyboardListener.registered) {
			enterBoatHotkey = new HotkeyPanel(styleManager, I18n.get("settings.enter_boat"), preferences.hotkeyBoat);
			enterBoatHotkey.setEnabled(preferences.usePreciseAngle.get() && preferences.useTallRes.get());
			column2.add(enterBoatHotkey);
		}
		boatErrorLimit = new FloatPreferencePanel(styleManager, I18n.get("settings.boat_error"), preferences.boatErrorLimit);
		boatErrorLimit.setDecimals(2);
		boatErrorLimit.setEnabled(preferences.usePreciseAngle.get() && preferences.useTallRes.get());
		column2.add(boatErrorLimit);

		sigmaBoat = new FloatPreferencePanel(styleManager, I18n.get("settings.boat_standard_deviation"), preferences.sigmaBoat);
		sigmaBoat.setEnabled(preferences.usePreciseAngle.get() && preferences.useTallRes.get());
		column2.add(sigmaBoat);

		disposeHandler.add(preferences.useTallRes.whenModified().subscribe(b -> setTallResolutionEnabled(b, preferences)));
		disposeHandler.add(preferences.usePreciseAngle.whenModified().subscribe(b -> setPreciseAngleEnabled(b)));
	}

	private void setTallResolutionEnabled(boolean b, NinjabrainBotPreferences preferences) {
		resolutionHeight.setEnabled(b);
		resolutionHeight.descLabel.updateColors();
		sensitivityCheckbox.setEnabled(b);
		sensitivityCheckbox.descLabel.updateColors();
		setPreciseAngleEnabled(b && preferences.usePreciseAngle.get());
	}

	private void setPreciseAngleEnabled(boolean b) {
		sensitivity.setEnabled(b);
		sensitivity.descLabel.updateColors();
		enterBoatHotkey.setEnabled(b);
		enterBoatHotkey.descLabel.updateColors();
		boatErrorLimit.setEnabled(b);
		boatErrorLimit.descLabel.updateColors();
		sigmaBoat.setEnabled(b);
		sigmaBoat.descLabel.updateColors();
	}

}
