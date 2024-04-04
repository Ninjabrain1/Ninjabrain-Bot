package ninjabrainbot.gui.options.sections;

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import ninjabrainbot.event.DisposeHandler;
import ninjabrainbot.gui.components.layout.StackPanel;
import ninjabrainbot.gui.components.preferences.*;
import ninjabrainbot.gui.frames.OptionsFrame;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.io.KeyboardListener;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.util.I18n;

public class BoatMeasurementOptionsPanel extends JPanel {

	private final RadioButtonPanel boatTypeRadioButtonPanel;
    private final DoublePreferencePanel sensitivity;
	private final FloatPreferencePanel boatErrorLimit;
	private final FloatPreferencePanel sigmaBoat;
	private HotkeyPanel enterBoatHotkey;

	public BoatMeasurementOptionsPanel(StyleManager styleManager, NinjabrainBotPreferences preferences, DisposeHandler disposeHandler) {
		setOpaque(false);
		setLayout(new GridLayout(1, 1, 2 * OptionsFrame.PADDING, 0));
		setBorder(new EmptyBorder(2 * OptionsFrame.PADDING, 2 * OptionsFrame.PADDING, 2 * OptionsFrame.PADDING, 2 * OptionsFrame.PADDING));
		JPanel column1 = new StackPanel();
		column1.setOpaque(false);
		add(column1);

        CheckboxPanel enableBoatMeasurementsCheckbox = new CheckboxPanel(styleManager, I18n.get("settings.use_precise_angle"), preferences.usePreciseAngle);
		column1.add(enableBoatMeasurementsCheckbox);

		sensitivity = new DoublePreferencePanel(styleManager, I18n.get("settings.sensitivity"), preferences.sensitivity);
		sensitivity.setWidth(150);
		sensitivity.setDecimals(10);
		column1.add(sensitivity);
		if (KeyboardListener.registered) {
			enterBoatHotkey = new HotkeyPanel(styleManager, I18n.get("settings.enter_boat"), preferences.hotkeyBoat);
			column1.add(enterBoatHotkey);
		}

		column1.add(boatTypeRadioButtonPanel = new RadioButtonPanel(styleManager, I18n.get("settings.boat_eye.default_boat_type"), preferences.defaultBoatType, true));

		boatErrorLimit = new FloatPreferencePanel(styleManager, I18n.get("settings.boat_error"), preferences.boatErrorLimit);
		boatErrorLimit.setDecimals(2);
		column1.add(boatErrorLimit);

		sigmaBoat = new FloatPreferencePanel(styleManager, I18n.get("settings.boat_standard_deviation"), preferences.sigmaBoat);
		column1.add(sigmaBoat);

		disposeHandler.add(preferences.usePreciseAngle.whenModified().subscribeEDT(this::setPreciseAngleEnabled));
		disposeHandler.add(preferences.sigmaBoat.whenModified().subscribeEDT(sigmaBoat::updateValue));
		setPreciseAngleEnabled(preferences.usePreciseAngle.get());
	}

	private void setPreciseAngleEnabled(boolean b) {
		sensitivity.setEnabled(b);
		boatTypeRadioButtonPanel.setEnabled(b);
		boatErrorLimit.setEnabled(b);
		sigmaBoat.setEnabled(b);
		if (enterBoatHotkey != null)
			enterBoatHotkey.setEnabled(b);
	}

}
