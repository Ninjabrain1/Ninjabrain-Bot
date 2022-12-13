package ninjabrainbot.gui.frames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.geom.RoundRectangle2D;
import java.text.DecimalFormat;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.View;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;

import ninjabrainbot.gui.buttons.FlatButton;
import ninjabrainbot.gui.components.CustomCheckbox;
import ninjabrainbot.gui.components.DecimalTextField;
import ninjabrainbot.gui.components.ThemedLabel;
import ninjabrainbot.gui.components.ThemedTextArea;
import ninjabrainbot.gui.panels.ThemedPanel;
import ninjabrainbot.gui.panels.settings.CalibrationPanel;
import ninjabrainbot.gui.panels.settings.RadioButtonGroup;
import ninjabrainbot.gui.panels.settings.ThemeSelectionPanel;
import ninjabrainbot.gui.panels.settings.ThemedTabbedPane;
import ninjabrainbot.gui.style.SizePreference;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.gui.style.WrappedColor;
import ninjabrainbot.io.KeyboardListener;
import ninjabrainbot.io.OBSOverlay;
import ninjabrainbot.io.preferences.BooleanPreference;
import ninjabrainbot.io.preferences.FloatPreference;
import ninjabrainbot.io.preferences.HotkeyPreference;
import ninjabrainbot.io.preferences.IMultipleChoicePreferenceDataType;
import ninjabrainbot.io.preferences.MultipleChoicePreference;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.util.I18n;

public class OptionsFrame extends ThemedFrame {

	private static final long serialVersionUID = 8033865173874423916L;

	private NinjabrainBotPreferences preferences;

	private StyleManager styleManager;
	private ThemedTabbedPane tabbedPane;
	private CalibrationPanel calibrationPanel;
	private FloatPreferencePanel sigma;
	private FloatPreferencePanel sigmaAlt;
	private FloatPreferencePanel resolutionHeight;
	private FloatPreferencePanel sensitivity;
	private HotkeyPanel sigmaAltHotkey;
	private FloatPreferencePanel overlayResetDelay;

	static int WINDOW_WIDTH = 560;
	static int COLUMN_WIDTH = WINDOW_WIDTH / 2;
	static int PADDING = 6;

	private static final String TITLE_TEXT = I18n.get("settings");

	public OptionsFrame(StyleManager styleManager, NinjabrainBotPreferences preferences) {
		super(styleManager, preferences, TITLE_TEXT);
		this.styleManager = styleManager;
		this.preferences = preferences;
		setLayout(null);
		tabbedPane = new ThemedTabbedPane(styleManager);
		add(tabbedPane);
		calibrationPanel = new CalibrationPanel(styleManager, preferences, this);
		add(calibrationPanel);
		tabbedPane.addTab(I18n.get("settings.basic"), getBasicPanel());
		tabbedPane.addTab(I18n.get("settings.advanced"), getAdvancedPanel());
		tabbedPane.addTab(I18n.get("settings.high_precision"), getHighPrecisionPanel());
		tabbedPane.addTab(I18n.get("settings.theme"), new ThemeSelectionPanel(styleManager, preferences, this));
		tabbedPane.addTab(I18n.get("settings.keyboard_shortcuts"), getHotkeyPanel());
		tabbedPane.addTab(I18n.get("settings.overlay"), getOBSPanel());
		tabbedPane.addTab(I18n.get("settings.language"), getLanguagePanel());

		// Title bar
		titlebarPanel.setFocusable(true);

		// Subscriptions
		sh.add(preferences.alwaysOnTop.whenModified().subscribe(b -> setAlwaysOnTop(b)));
		sh.add(preferences.useAltStd.whenModified().subscribe(b -> setAltSigmaEnabled(b)));
		sh.add(preferences.useTallRes.whenModified().subscribe(b -> setTallResolutionEnabled(b)));
		sh.add(preferences.usePreciseAngle.whenModified().subscribe(b -> setPreciseAngleEnabled(b)));
		sh.add(preferences.overlayAutoHide.whenModified().subscribe(b -> setOverlayAutoHideEnabled(b)));
	}

	private JPanel getBasicPanel() {
		JPanel mainPanel = new JPanel();
		mainPanel.setOpaque(false);
		mainPanel.setLayout(new GridLayout(1, 2));
		mainPanel.setBorder(new EmptyBorder(PADDING - 3, PADDING, PADDING, PADDING));
		JPanel column1 = new JPanel();
		JPanel column2 = new JPanel();
		column1.setOpaque(false);
		column2.setOpaque(false);
		mainPanel.add(column1);
		mainPanel.add(column2);
		column1.setLayout(new BoxLayout(column1, BoxLayout.Y_AXIS));
		column2.setLayout(new BoxLayout(column2, BoxLayout.Y_AXIS));

		// Column 1
		column1.add(new CheckboxPanel(styleManager, I18n.get("settings.show_nether_coordinates"), preferences.showNetherCoords));
		column1.add(new CheckboxPanel(styleManager, I18n.get("settings.auto_reset"), preferences.autoReset));
		column1.add(new CheckboxPanel(styleManager, I18n.get("settings.always_on_top"), preferences.alwaysOnTop));
		column1.add(new CheckboxPanel(styleManager, I18n.get("settings.translucent_window"), preferences.translucent));
		column1.add(new CheckboxPanel(styleManager, I18n.get("settings.notify_when_a_new_version_is_available"), preferences.checkForUpdates));
		column1.add(Box.createGlue());

		// Column 2
		column2.add(Box.createVerticalStrut(10));
		column2.add(new RadioButtonPanel(styleManager, I18n.get("settings.display_stronghold_location_using"), preferences.strongholdDisplayType));
		column2.add(new RadioButtonPanel(styleManager, I18n.get("settings.view_type"), preferences.view));
		column2.add(new RadioButtonPanel(styleManager, I18n.get("settings.window_size"), preferences.size));
		column2.add(new RadioButtonPanel(styleManager, I18n.get("settings.mc_version"), preferences.mcVersion));
		column2.add(Box.createGlue());
		return mainPanel;
	}

	private JPanel getAdvancedPanel() {
		JPanel mainPanel = new JPanel();
		mainPanel.setOpaque(false);
		mainPanel.setLayout(new GridLayout(1, 2));
		mainPanel.setBorder(new EmptyBorder(PADDING, PADDING, PADDING, PADDING));
		JPanel column1 = new JPanel();
		JPanel column2 = new JPanel();
		column1.setOpaque(false);
		column2.setOpaque(false);
		mainPanel.add(column1);
		mainPanel.add(column2);
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
		return mainPanel;
	}

	private JPanel getHighPrecisionPanel() {
		JPanel mainPanel = new JPanel();
		mainPanel.setOpaque(false);
		mainPanel.setLayout(new GridLayout(1, 2));
		mainPanel.setBorder(new EmptyBorder(PADDING, PADDING, PADDING, PADDING));
		JPanel column1 = new JPanel();
		JPanel column2 = new JPanel();
		column1.setOpaque(false);
		column2.setOpaque(false);
		mainPanel.add(column1);
		mainPanel.add(column2);
		column1.setLayout(new BoxLayout(column1, BoxLayout.Y_AXIS));
		column2.setLayout(new BoxLayout(column2, BoxLayout.Y_AXIS));

		// Tall Res Column
		column1.add(new CheckboxPanel(styleManager, I18n.get("settings.tall_resolution"), preferences.useTallRes));
		resolutionHeight = new FloatPreferencePanel(styleManager, I18n.get("settings.window_height"), preferences.resolutionHeight);
		resolutionHeight.setDecimals(0);
		resolutionHeight.setEnabled(preferences.useTallRes.get());
		column1.add(resolutionHeight);
		column1.add(new Box.Filler(new Dimension(0,0), new Dimension(0,Short.MAX_VALUE),
				new Dimension(0, Short.MAX_VALUE)));

		// Precise Sens Column
		column2.add(new CheckboxPanel(styleManager, I18n.get("settings.use_precise_angle"), preferences.usePreciseAngle));
		sensitivity = new FloatPreferencePanel(styleManager, I18n.get("settings.sensitivity"), preferences.sensitivity);
		sensitivity.setWidth(100);
		sensitivity.setDecimals(9);
		sensitivity.setEnabled(preferences.usePreciseAngle.get());
		column2.add(sensitivity);
		column2.add(new Box.Filler(new Dimension(0,0), new Dimension(0,Short.MAX_VALUE),
				new Dimension(0, Short.MAX_VALUE)));

		return mainPanel;
	}

	private JPanel getHotkeyPanel() {
		JPanel ac2 = new JPanel();
		ac2.setOpaque(false);
		ac2.setLayout(new GridBagLayout());
		ac2.setBorder(new EmptyBorder(PADDING, PADDING, PADDING, PADDING));

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridy = GridBagConstraints.RELATIVE;
		constraints.gridx = 0;
		constraints.insets = new Insets(0, 0, PADDING, 0);
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weightx = 1;
		if (KeyboardListener.registered) {
			ac2.add(new HotkeyPanel(styleManager, I18n.get("settings.up_001_to_last_angle"), preferences.hotkeyIncrement), constraints);
			ac2.add(new HotkeyPanel(styleManager, I18n.get("settings.down_001_to_last_angle"), preferences.hotkeyDecrement), constraints);
			ac2.add(new HotkeyPanel(styleManager, I18n.get("reset"), preferences.hotkeyReset), constraints);
			ac2.add(new HotkeyPanel(styleManager, I18n.get("undo"), preferences.hotkeyUndo), constraints);
			ac2.add(new HotkeyPanel(styleManager, I18n.get("lock"), preferences.hotkeyLock), constraints);
			ac2.add(new HotkeyPanel(styleManager, I18n.get("hide_show_window"), preferences.hotkeyMinimize), constraints);
		}
		constraints.weighty = 1;
		ac2.add(Box.createGlue(), constraints);
		return ac2;
	}

	private JPanel getOBSPanel() {
		JPanel ac2 = new JPanel();
		ac2.setOpaque(false);
		ac2.setLayout(new GridBagLayout());
		ac2.setBorder(new EmptyBorder(PADDING, PADDING, PADDING, PADDING));

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridy = GridBagConstraints.RELATIVE;
		constraints.gridx = 0;
		constraints.insets = new Insets(0, PADDING, 0, PADDING);
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weightx = 1;
		ThemedLabel overlayExplanation = new ThemedLabel(styleManager, "<html>" + I18n.get("settings.overlay_explanation") + "</html>") {
			private static final long serialVersionUID = 7980539999697524316L;

			public int getTextSize(SizePreference p) {
				return p.TEXT_SIZE_SMALL;
			};

			public Dimension getPreferredSize() {
				View view = (View) getClientProperty(javax.swing.plaf.basic.BasicHTML.propertyKey);
				view.setSize(WINDOW_WIDTH - 2 * PADDING, 0);
				float w = view.getPreferredSpan(View.X_AXIS);
				float h = view.getPreferredSpan(View.Y_AXIS);
				return new java.awt.Dimension((int) Math.ceil(w), (int) Math.ceil(h));
			}
		};
		ac2.add(overlayExplanation, constraints);
		ac2.add(new ThemedTextArea(styleManager, OBSOverlay.OBS_OVERLAY.getAbsolutePath()), constraints);
		constraints.insets = new Insets(0, 0, 0, 0);
		ac2.add(new CheckboxPanel(styleManager, I18n.get("settings.overlay_enable"), preferences.useOverlay), constraints);
		ac2.add(new CheckboxPanel(styleManager, I18n.get("settings.overlay_hide_locked"), preferences.overlayHideWhenLocked), constraints);
		ac2.add(new CheckboxPanel(styleManager, I18n.get("settings.overlay_auto_hide"), preferences.overlayAutoHide), constraints);
		overlayResetDelay = new FloatPreferencePanel(styleManager, I18n.get("settings.overlay_auto_hide_duration"), preferences.overlayHideDelay);
		overlayResetDelay.setDecimals(0);
		overlayResetDelay.setEnabled(preferences.overlayAutoHide.get());
		ac2.add(overlayResetDelay, constraints);

		constraints.weighty = 1;
		ac2.add(Box.createGlue(), constraints);
		return ac2;
	}

	private JPanel getLanguagePanel() {
		JPanel ac2 = new JPanel();
		ac2.setOpaque(false);
		ac2.setAlignmentX(0);
		ac2.setLayout(new FlowLayout(FlowLayout.LEFT));
		ac2.setBorder(new EmptyBorder(0, PADDING, PADDING, PADDING));
		ac2.add(new RadioButtonPanel(styleManager, I18n.get("settings.language.hint"), preferences.language));
		return ac2;
	}

	private void startCalibrating() {
		calibrationPanel.startCalibrating();
		tabbedPane.setVisible(false);
		titlebarPanel.setVisible(false);
		calibrationPanel.setVisible(true);
		updateBounds(styleManager);
		if (KeyboardListener.registered) {
			KeyboardListener.instance.cancelConsumer();
		}
	}

	public void stopCalibrating() {
		tabbedPane.setVisible(true);
		titlebarPanel.setVisible(true);
		calibrationPanel.setVisible(false);
		calibrationPanel.cancel();
		updateBounds(styleManager);
		sigma.updateValue();
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

	private void setAltSigmaEnabled(boolean b) {
		sigmaAlt.setEnabled(b);
		sigmaAlt.descLabel.updateColors();
		sigmaAltHotkey.setEnabled(b);
		sigmaAltHotkey.descLabel.updateColors();
	}

	private void setTallResolutionEnabled(boolean b) {
		resolutionHeight.setEnabled(b);
		resolutionHeight.descLabel.updateColors();
	}

	private void setPreciseAngleEnabled(boolean b) {
		sensitivity.setEnabled(b);
		sensitivity.descLabel.updateColors();
	}

	private void setOverlayAutoHideEnabled(boolean b) {
		overlayResetDelay.setEnabled(b);
		overlayResetDelay.descLabel.updateColors();
	}

}

class CheckboxPanel extends ThemedPanel {

	private static final long serialVersionUID = -7054967229481740724L;

	ThemedLabel descLabel;
	CustomCheckbox checkbox;
	BooleanPreference preference;

	public CheckboxPanel(StyleManager styleManager, String description, BooleanPreference preference) {
		super(styleManager);
		this.preference = preference;
		setLayout(new FlowLayout(FlowLayout.LEFT));
		CheckboxPanel t = this;
		descLabel = new ThemedLabel(styleManager, "<html>" + description + "</html>") {
			private static final long serialVersionUID = 2113195400239083116L;

			@Override
			public int getTextSize(SizePreference p) {
				return p.TEXT_SIZE_SMALL;
			}

			public Dimension getPreferredSize() {
				return new Dimension(t.getWidth() - 2 * OptionsFrame.PADDING - 32, super.getPreferredSize().height);
			}
		};
		checkbox = new CustomCheckbox(preference.get()) {
			private static final long serialVersionUID = 1507233642665292025L;

			@Override
			public void onChanged(boolean ticked) {
				preference.set(ticked);
			}
		};
		add(checkbox, BorderLayout.LINE_START);
		add(descLabel, BorderLayout.CENTER);
		setOpaque(false);
	}

}

class FloatPreferencePanel extends ThemedPanel {

	private static final long serialVersionUID = -7054967229481740724L;

	ThemedLabel descLabel;
	DecimalTextField textfield;
	FloatPreference preference;
	DecimalFormat format;

	WrappedColor disabledCol;

	public FloatPreferencePanel(StyleManager styleManager, String description, FloatPreference preference) {
		super(styleManager);
		this.preference = preference;
		setLayout(new FlowLayout(FlowLayout.LEFT));
		descLabel = new ThemedLabel(styleManager, description) {
			private static final long serialVersionUID = 2113195400239083116L;

			@Override
			public int getTextSize(SizePreference p) {
				return p.TEXT_SIZE_SMALL;
			}

			@Override
			public Color getForegroundColor() {
				if (textfield.isEnabled()) {
					return super.getForegroundColor();
				}
				return disabledCol.color();
			}
		};
		textfield = new DecimalTextField(styleManager, preference.get(), preference.min(), preference.max()) {
			private static final long serialVersionUID = -1357640224921308648L;

			@Override
			public void onChanged(double newValue) {
				preference.set(Float.parseFloat(format.format(newValue)));
			}
		};
		this.setDecimals(4);

		Dimension size = textfield.getPreferredSize();
		size.width = 60;
		textfield.setPreferredSize(size);
		add(Box.createHorizontalStrut(0));
		add(descLabel);
		add(textfield);
		setOpaque(false);

		disabledCol = styleManager.currentTheme.TEXT_COLOR_WEAK;
	}

	public void updateValue() {
		textfield.setValue((double) preference.get());
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		textfield.setEnabled(enabled);
	}

	public void setWidth(int width) {
		Dimension size = textfield.getPreferredSize();
		size.width = width;
		textfield.setPreferredSize(size);
	}

	public void setDecimals(int decimals) {
		String newFormat = "#";
		if (decimals > 0) {
			newFormat += "." + "#".repeat(decimals);
		}
		this.format = new DecimalFormat(newFormat);
		textfield.setEditor(new JSpinner.NumberEditor(textfield, newFormat));
	}

}

class RadioButtonPanel extends ThemedPanel {

	private static final long serialVersionUID = -7054967229481740724L;

	JLabel descLabel;
	RadioButtonGroup radioButtomGroup;
	MultipleChoicePreference<?> preference;

	public RadioButtonPanel(StyleManager styleManager, String description, MultipleChoicePreference<?> preference) {
		super(styleManager);
		this.preference = preference;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		descLabel = new ThemedLabel(styleManager, description) {
			private static final long serialVersionUID = 2113195400239083116L;

			@Override
			public int getTextSize(SizePreference p) {
				return p.TEXT_SIZE_SMALL;
			}
		};
		radioButtomGroup = new RadioButtonGroup(styleManager, preference.getChoices(), preference.get(), preference.getChoices().length >= 4) {
			private static final long serialVersionUID = -1357640224921308648L;

			@Override
			public void onChanged(IMultipleChoicePreferenceDataType newValue) {
				preference.set(newValue);
			}
		};
		descLabel.setAlignmentX(0);
		radioButtomGroup.setAlignmentX(0);
		add(descLabel);
		add(radioButtomGroup);
		setOpaque(false);
	}

}

class HotkeyPanel extends ThemedPanel {

	private static final long serialVersionUID = -7054967229481740724L;

	ThemedLabel descLabel;
	FlatButton button;
	HotkeyPreference preference;
	boolean editing = false;

	WrappedColor disabledCol;

	public HotkeyPanel(StyleManager styleManager, String description, HotkeyPreference preference) {
		super(styleManager);
		this.preference = preference;
		setLayout(new GridLayout(1, 2, 0, 0));
		setBorder(new EmptyBorder(0, 10, 0, 0));
		descLabel = new ThemedLabel(styleManager, description) {
			private static final long serialVersionUID = -658733822961822860L;

			@Override
			public int getTextSize(SizePreference p) {
				return p.TEXT_SIZE_SMALL;
			}

			@Override
			public Color getForegroundColor() {
				if (button.isEnabled()) {
					return super.getForegroundColor();
				}
				return disabledCol.color();
			}
		};
		descLabel.setHorizontalAlignment(SwingConstants.LEFT);
		button = new FlatButton(styleManager, getKeyText()) {
			private static final long serialVersionUID = 1865599754734492942L;

			@Override
			public int getTextSize(SizePreference p) {
				return p.TEXT_SIZE_SMALL;
			}
		};
		button.addActionListener(p -> clicked());
		Dimension size = button.getPreferredSize();
		size.width = OptionsFrame.WINDOW_WIDTH / 4;
		button.setPreferredSize(size);
		add(descLabel);
		add(button);
		setOpaque(false);

		disabledCol = styleManager.currentTheme.TEXT_COLOR_WEAK;
	}

	private void clicked() {
		if (!editing) {
			editing = true;
			button.setText("...");
			KeyboardListener.instance.setConsumer((code, modifier) -> {
				if (code == -1) {
					// Canceled, dont change anything
				} else if (code == KeyEvent.VK_ESCAPE) {
					preference.setCode(-1);
					preference.setModifier(-1);
				} else {
					preference.setCode(code);
					preference.setModifier(modifier);
				}
				String s = getKeyText();
				SwingUtilities.invokeLater(() -> {
					button.setText(s);
					editing = false;
				});
			});
		}
	}

	private String getKeyText() {
		if (preference.getCode() == -1)
			return I18n.get("settings.not_in_use");
		String k = KeyEvent.getKeyText(preference.getCode());
		if (k.startsWith("Unknown")) {
			k = k.substring(17);
		}
		if (preference.getModifier() == 0) {
			return k;
		} else {
			return NativeKeyEvent.getModifiersText(preference.getModifier()) + "+" + k;
		}
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		button.setEnabled(enabled);
	}

}
