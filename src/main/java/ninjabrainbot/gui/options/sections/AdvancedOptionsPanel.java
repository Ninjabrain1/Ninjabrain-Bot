package ninjabrainbot.gui.options.sections;

import java.awt.GridLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import ninjabrainbot.event.SubscriptionHandler;
import ninjabrainbot.gui.buttons.FlatButton;
import ninjabrainbot.gui.frames.OptionsFrame;
import ninjabrainbot.gui.options.CalibrationPanel;
import ninjabrainbot.gui.options.components.CheckboxPanel;
import ninjabrainbot.gui.options.components.FloatPreferencePanel;
import ninjabrainbot.gui.options.components.HotkeyPanel;
import ninjabrainbot.gui.style.SizePreference;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.io.KeyboardListener;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.util.I18n;

@SuppressWarnings("serial")
public class AdvancedOptionsPanel extends JPanel {

	private CalibrationPanel calibrationPanel;

	private FloatPreferencePanel sigma;
	private FloatPreferencePanel sigmaAlt;
	private HotkeyPanel sigmaAltHotkey;

	public AdvancedOptionsPanel(StyleManager styleManager, NinjabrainBotPreferences preferences, CalibrationPanel calibrationPanel, SubscriptionHandler subscriptionHandler) {
		this.calibrationPanel = calibrationPanel;
		setOpaque(false);
		setLayout(new GridLayout(1, 2));
		setBorder(new EmptyBorder(OptionsFrame.PADDING, OptionsFrame.PADDING, OptionsFrame.PADDING, OptionsFrame.PADDING));
		JPanel column1 = new JPanel();
		JPanel column2 = new JPanel();
		column1.setOpaque(false);
		column2.setOpaque(false);
		add(column1);
		add(column2);
		column1.setLayout(new BoxLayout(column1, BoxLayout.Y_AXIS));
		column2.setLayout(new BoxLayout(column2, BoxLayout.Y_AXIS));

		// Left advanced column
		sigma = new FloatPreferencePanel(styleManager, I18n.get("settings.standard_deviation"), preferences.sigma);
		column1.add(sigma);
		JButton calibrateButton = new FlatButton(styleManager, I18n.get("settings.calibrate_standard_deviation")) {
			private static final long serialVersionUID = -673676238214760361L;

			@Override
			public int getTextSize(SizePreference p) {
				return p.TEXT_SIZE_SMALL;
			}
		};
		calibrateButton.addActionListener(p -> startCalibrating());
		calibrateButton.setAlignmentX(0.5f);
		column1.add(calibrateButton);
		column1.add(new FloatPreferencePanel(styleManager, I18n.get("settings.standard_deviation_manual"), preferences.sigmaManual));
		column1.add(new CheckboxPanel(styleManager, I18n.get("settings.enable_standard_deviation_toggle"), preferences.useAltStd));
		sigmaAlt = new FloatPreferencePanel(styleManager, I18n.get("settings.alt_standard_deviation"), preferences.sigmaAlt);
		sigmaAlt.setEnabled(preferences.useAltStd.get());
		column1.add(sigmaAlt);
		if (KeyboardListener.registered) {
			sigmaAltHotkey = new HotkeyPanel(styleManager, I18n.get("settings.alt_std_on_last_angle"), preferences.hotkeyAltStd);
			sigmaAltHotkey.setEnabled(preferences.useAltStd.get());
			column1.add(sigmaAltHotkey);
		}
		column2.add(new FloatPreferencePanel(styleManager, I18n.get("settings.crosshair_correction"), preferences.crosshairCorrection));
		column2.add(new CheckboxPanel(styleManager, I18n.get("settings.show_angle_errors"), preferences.showAngleErrors));
		column2.add(new CheckboxPanel(styleManager, I18n.get("settings.show_angle_updates"), preferences.showAngleUpdates));
		column2.add(new CheckboxPanel(styleManager, I18n.get("settings.use_advanced_stronghold_statistics"), preferences.useAdvStatistics));
		column2.add(new CheckboxPanel(styleManager, I18n.get("settings.use_alternative_clipboard_reader"), preferences.altClipboardReader));
		column2.add(Box.createGlue());

		subscriptionHandler.add(preferences.useAltStd.whenModified().subscribe(b -> setAltSigmaEnabled(b)));
	}

	private void startCalibrating() {
		calibrationPanel.startCalibrating();
//		tabbedPane.setVisible(false);
//		titlebarPanel.setVisible(false);
//		calibrationPanel.setVisible(true);
//		updateBounds(styleManager);
//		if (KeyboardListener.registered) {
//			KeyboardListener.instance.cancelConsumer();
//		}
	}

	private void setAltSigmaEnabled(boolean b) {
		sigmaAlt.setEnabled(b);
		sigmaAlt.descLabel.updateColors();
		sigmaAltHotkey.setEnabled(b);
		sigmaAltHotkey.descLabel.updateColors();
	}

}
