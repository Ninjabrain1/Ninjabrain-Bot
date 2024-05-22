package ninjabrainbot.gui.options.sections;

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import ninjabrainbot.event.DisposeHandler;
import ninjabrainbot.gui.components.labels.ThemedLabel;
import ninjabrainbot.gui.components.layout.Divider;
import ninjabrainbot.gui.components.layout.StackPanel;
import ninjabrainbot.gui.components.preferences.CheckboxPanel;
import ninjabrainbot.gui.components.preferences.DoublePreferencePanel;
import ninjabrainbot.gui.components.preferences.FloatPreferencePanel;
import ninjabrainbot.gui.components.preferences.HotkeyPanel;
import ninjabrainbot.gui.frames.OptionsFrame;
import ninjabrainbot.gui.style.SizePreference;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.io.KeyboardListener;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.util.I18n;

public class HighPrecisionOptionsPanel extends JPanel {

	private final CheckboxPanel enableBoatMeasurementsCheckbox;
	private final CheckboxPanel activateBoatOnResetCheckbox;
	private final FloatPreferencePanel resolutionHeight;
	private final DoublePreferencePanel sensitivity;
	private final FloatPreferencePanel boatErrorLimit;
	private final FloatPreferencePanel sigmaBoat;
	private HotkeyPanel enterBoatHotkey;

	public HighPrecisionOptionsPanel(StyleManager styleManager, NinjabrainBotPreferences preferences, DisposeHandler disposeHandler) {
		setOpaque(false);
		setLayout(new GridLayout(1, 1, 2 * OptionsFrame.PADDING, 0));
		setBorder(new EmptyBorder(2 * OptionsFrame.PADDING, 2 * OptionsFrame.PADDING, 2 * OptionsFrame.PADDING, 2 * OptionsFrame.PADDING));
		JPanel column1 = new StackPanel();
		column1.setOpaque(false);
		add(column1);

		// Tall Res Column
		column1.add(new ThemedLabel(styleManager, "<html>" + I18n.get("settings.tall_resolution_explanation") + "</html>") {
			public int getTextSize(SizePreference p) {
				return p.TEXT_SIZE_SMALL;
			}
		});
		JPanel tallResRow = new JPanel(new GridLayout(1, 2, 2 * OptionsFrame.PADDING, 0));
		tallResRow.setOpaque(false);
		tallResRow.add(new CheckboxPanel(styleManager, I18n.get("settings.tall_resolution"), preferences.useTallRes));
		resolutionHeight = new FloatPreferencePanel(styleManager, I18n.get("settings.resolution_height"), preferences.resolutionHeight);
		resolutionHeight.setDecimals(0);
		tallResRow.add(resolutionHeight);
		column1.add(tallResRow);

		// Precise Sens Column
		column1.add(new Divider(styleManager));

		enableBoatMeasurementsCheckbox = new CheckboxPanel(styleManager, I18n.get("settings.use_precise_angle"), preferences.usePreciseAngle);
		column1.add(enableBoatMeasurementsCheckbox);

		sensitivity = new DoublePreferencePanel(styleManager, I18n.get("settings.sensitivity"), preferences.sensitivity);
		sensitivity.setWidth(150);
		sensitivity.setDecimals(10);
		column1.add(sensitivity);
		if (KeyboardListener.registered) {
			enterBoatHotkey = new HotkeyPanel(styleManager, I18n.get("settings.enter_boat"), preferences.hotkeyBoat);
			column1.add(enterBoatHotkey);
		}
		activateBoatOnResetCheckbox = new CheckboxPanel(styleManager, I18n.get("settings.enter_boat_on_reset"), preferences.activateBoatOnReset);
		column1.add(activateBoatOnResetCheckbox);

		boatErrorLimit = new FloatPreferencePanel(styleManager, I18n.get("settings.boat_error"), preferences.boatErrorLimit);
		boatErrorLimit.setDecimals(2);
		column1.add(boatErrorLimit);

		sigmaBoat = new FloatPreferencePanel(styleManager, I18n.get("settings.boat_standard_deviation"), preferences.sigmaBoat);
		column1.add(sigmaBoat);

		disposeHandler.add(preferences.useTallRes.whenModified().subscribeEDT(b -> setTallResolutionEnabled(b, preferences)));
		disposeHandler.add(preferences.usePreciseAngle.whenModified().subscribeEDT(this::setPreciseAngleEnabled));
		disposeHandler.add(preferences.sigmaBoat.whenModified().subscribeEDT(sigmaBoat::updateValue));
		setTallResolutionEnabled(preferences.useTallRes.get(), preferences);
	}

	private void setTallResolutionEnabled(boolean b, NinjabrainBotPreferences preferences) {
		resolutionHeight.setEnabled(b);
		resolutionHeight.descLabel.updateColors();
		enableBoatMeasurementsCheckbox.setEnabled(b);
		enableBoatMeasurementsCheckbox.descLabel.updateColors();
		setPreciseAngleEnabled(b && preferences.usePreciseAngle.get());
	}

	private void setPreciseAngleEnabled(boolean b) {
		sensitivity.setEnabled(b);
		activateBoatOnResetCheckbox.setEnabled(b);
		activateBoatOnResetCheckbox.descLabel.updateColors();
		boatErrorLimit.setEnabled(b);
		sigmaBoat.setEnabled(b);
		if (enterBoatHotkey != null)
			enterBoatHotkey.setEnabled(b);
	}

}
