package ninjabrainbot.gui.options.sections;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import ninjabrainbot.event.DisposeHandler;
import ninjabrainbot.gui.buttons.FlatButton;
import ninjabrainbot.gui.buttons.WikiButton;
import ninjabrainbot.gui.components.layout.StackPanel;
import ninjabrainbot.gui.components.preferences.CheckboxPanel;
import ninjabrainbot.gui.components.preferences.DoublePreferencePanel;
import ninjabrainbot.gui.components.preferences.FloatPreferencePanel;
import ninjabrainbot.gui.components.preferences.HotkeyPanel;
import ninjabrainbot.gui.frames.CalibrationDialog;
import ninjabrainbot.gui.frames.OptionsFrame;
import ninjabrainbot.gui.style.SizePreference;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.io.KeyboardListener;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.model.actions.IActionExecutor;
import ninjabrainbot.model.datastate.calibrator.ICalibratorFactory;
import ninjabrainbot.util.I18n;

public class AdvancedOptionsPanel extends JPanel {

	private final StyleManager styleManager;
	private final NinjabrainBotPreferences preferences;
	private final ICalibratorFactory calibratorFactory;
	private final IActionExecutor actionExecutor;
	private final JFrame owner;

	private final FloatPreferencePanel sigmaAlt;
	private HotkeyPanel sigmaAltHotkey;

	public AdvancedOptionsPanel(StyleManager styleManager, NinjabrainBotPreferences preferences, ICalibratorFactory calibratorFactory, IActionExecutor actionExecutor, JFrame owner, DisposeHandler disposeHandler) {
		this.styleManager = styleManager;
		this.preferences = preferences;
		this.calibratorFactory = calibratorFactory;
		this.actionExecutor = actionExecutor;
		this.owner = owner;
		setOpaque(false);
		setLayout(new GridLayout(1, 2, 2 * OptionsFrame.PADDING, 0));
		setBorder(new EmptyBorder(2 * OptionsFrame.PADDING, 2 * OptionsFrame.PADDING, 2 * OptionsFrame.PADDING, 2 * OptionsFrame.PADDING));
		JPanel column1 = new StackPanel(2 * OptionsFrame.PADDING);
		JPanel column2 = new StackPanel(3 * OptionsFrame.PADDING);
		column1.setOpaque(false);
		column2.setOpaque(false);
		add(column1);
		add(column2);

		FloatPreferencePanel sigmaPanel = new FloatPreferencePanel(styleManager, I18n.get("settings.standard_deviation"), preferences.sigma);
		column1.add(sigmaPanel);
		JButton calibrateButton = new FlatButton(styleManager, I18n.get("settings.calibrate_standard_deviation")) {
			@Override
			public int getTextSize(SizePreference p) {
				return p.TEXT_SIZE_SMALL;
			}
		};
		calibrateButton.addActionListener(p -> startCalibrating(false));
		calibrateButton.setAlignmentX(0.5f);
		column1.add(calibrateButton);

		FloatPreferencePanel sigmaManualPanel = new FloatPreferencePanel(styleManager, I18n.get("settings.standard_deviation_manual"), preferences.sigmaManual);
		column1.add(sigmaManualPanel);
		JButton manualCalibrateButton = new FlatButton(styleManager, I18n.get("settings.calibrate_standard_deviation_manual")) {
			@Override
			public int getTextSize(SizePreference p) {
				return p.TEXT_SIZE_SMALL;
			}
		};
		manualCalibrateButton.addActionListener(p -> startCalibrating(true));
		manualCalibrateButton.setAlignmentX(0.5f);
		column1.add(manualCalibrateButton);

		column1.add(new CheckboxPanel(styleManager, I18n.get("settings.enable_standard_deviation_toggle"), preferences.useAltStd));
		sigmaAlt = new FloatPreferencePanel(styleManager, I18n.get("settings.alt_standard_deviation"), preferences.sigmaAlt);
		sigmaAlt.setEnabled(preferences.useAltStd.get());
		column1.add(sigmaAlt);
		if (KeyboardListener.registered) {
			sigmaAltHotkey = new HotkeyPanel(styleManager, I18n.get("settings.alt_std_on_last_angle"), preferences.hotkeyAltStd);
			sigmaAltHotkey.setEnabled(preferences.useAltStd.get());
			column1.add(sigmaAltHotkey);
		}

		column2.add(new DoublePreferencePanel(styleManager, I18n.get("settings.crosshair_correction"), preferences.crosshairCorrection));
		column2.add(new CheckboxPanel(styleManager, I18n.get("settings.show_angle_errors"), preferences.showAngleErrors));
		column2.add(new CheckboxPanel(styleManager, I18n.get("settings.color_negative_coords"), preferences.colorCodeNegativeCoords));
		column2.add(new CheckboxPanel(styleManager, I18n.get("settings.use_advanced_stronghold_statistics"), preferences.useAdvStatistics));
		column2.add(new CheckboxPanel(styleManager, I18n.get("settings.use_alternative_clipboard_reader"), preferences.altClipboardReader));
		column2.add(new CheckboxPanel(styleManager, I18n.get("settings.enable_api"), preferences.enableHttpServer)
				.withWikiButton(new WikiButton(styleManager, "https://github.com/Ninjabrain1/Ninjabrain-Bot/wiki/API")));
		column2.add(new CheckboxPanel(styleManager, I18n.get("settings.save_state"), preferences.saveState));

		disposeHandler.add(preferences.useAltStd.whenModified().subscribeEDT(this::setAltSigmaEnabled));
		disposeHandler.add(preferences.sigma.whenModified().subscribeEDT(sigmaPanel::updateValue));
		disposeHandler.add(preferences.sigmaManual.whenModified().subscribeEDT(sigmaManualPanel::updateValue));
	}

	private void startCalibrating(boolean isManualCalibrator) {
		CalibrationDialog d = new CalibrationDialog(styleManager, preferences, calibratorFactory, actionExecutor, owner, isManualCalibrator);
		d.setLocation(owner.getX() - 140, owner.getY() + 30);
		styleManager.init();
		SwingUtilities.invokeLater(() -> d.setVisible(true));
	}

	private void setAltSigmaEnabled(boolean b) {
		sigmaAlt.setEnabled(b);
		sigmaAltHotkey.setEnabled(b);
	}

}
