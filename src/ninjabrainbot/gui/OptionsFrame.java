package ninjabrainbot.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.geom.RoundRectangle2D;
import java.net.URL;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.text.View;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;

import ninjabrainbot.Main;
import ninjabrainbot.gui.components.CalibrationPanel;
import ninjabrainbot.gui.components.CustomCheckbox;
import ninjabrainbot.gui.components.DecimalTextField;
import ninjabrainbot.gui.components.Divider;
import ninjabrainbot.gui.components.FlatButton;
import ninjabrainbot.gui.components.RadioButtonGroup;
import ninjabrainbot.gui.components.ThemedFrame;
import ninjabrainbot.gui.components.ThemedLabel;
import ninjabrainbot.gui.components.ThemedPanel;
import ninjabrainbot.gui.components.ThemedTextArea;
import ninjabrainbot.gui.components.TitleBarButton;
import ninjabrainbot.io.BooleanPreference;
import ninjabrainbot.io.FloatPreference;
import ninjabrainbot.io.HotkeyPreference;
import ninjabrainbot.io.KeyboardListener;
import ninjabrainbot.io.MultipleChoicePreference;
import ninjabrainbot.util.I18n;

public class OptionsFrame extends ThemedFrame {

	private static final long serialVersionUID = 8033865173874423916L;

	private GUI gui;
	private JPanel settingsPanel;
	private CalibrationPanel calibrationPanel;
	private FlatButton exitButton;
	private TextboxPanel sigma;
	private TextboxPanel sigmaAlt;
	private HotkeyPanel sigmaAltHotkey;
	private JPanel mainPanel; // Panel containing all non-advanced options
	private JPanel advPanel; // Panel containing all advanced options

	static int WINDOW_WIDTH = 560;
	static int COLUMN_WIDTH = WINDOW_WIDTH/2;
	static int PADDING = 6;
	
	private static final String TITLE_TEXT = I18n.get("settings");

	public OptionsFrame(GUI gui) {
		super(gui, TITLE_TEXT);
		this.gui = gui;
		
		// Windows that can be swapped between
		settingsPanel = new JPanel();
		settingsPanel.setOpaque(false);
		settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.Y_AXIS));
		add(settingsPanel);
		calibrationPanel = new CalibrationPanel(gui, this);
		add(calibrationPanel);
		
		// Title bar
		exitButton = getExitButton();
		titlebarPanel.addButton(exitButton);
		titlebarPanel.setFocusable(true);
		
		// Main panel
		mainPanel = new JPanel();
		mainPanel.setOpaque(false);
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.setBorder(new EmptyBorder(PADDING - 3, PADDING, PADDING, PADDING));
		settingsPanel.add(mainPanel);
		JPanel columns = new JPanel();
		columns.setOpaque(false);
		columns.setLayout(new GridLayout(1, 2));
		JPanel column1 = new JPanel();
		JPanel column2 = new JPanel();
		column1.setOpaque(false);
		column2.setOpaque(false);
		columns.add(column1);
		columns.add(column2);
		column1.setLayout(new BoxLayout(column1, BoxLayout.Y_AXIS));
		column2.setLayout(new BoxLayout(column2, BoxLayout.Y_AXIS));
		mainPanel.add(columns);
		
		// Column 1
		column1.add(new CheckboxPanel(gui, I18n.get("settings.show_nether_coordinates"), Main.preferences.showNetherCoords));
		column1.add(new CheckboxPanel(gui, I18n.get("settings.auto_reset"), Main.preferences.autoReset));
		column1.add(new CheckboxPanel(gui, I18n.get("settings.always_on_top"), Main.preferences.alwaysOnTop));
		column1.add(new CheckboxPanel(gui, I18n.get("settings.translucent_window"), Main.preferences.translucent));
		column1.add(new CheckboxPanel(gui, I18n.get("settings.notify_when_a_new_version_is_available"), Main.preferences.checkForUpdates));
		column1.add(Box.createGlue());
		
		// Column 2
		column2.add(Box.createVerticalStrut(10));
		column2.add(new RadioButtonPanel(gui, I18n.get("settings.display_stronghold_location_using"), Main.preferences.strongholdDisplayType));
		column2.add(new RadioButtonPanel(gui, I18n.get("settings.view_type"), Main.preferences.view));
		column2.add(new RadioButtonPanel(gui, I18n.get("settings.theme"), Main.preferences.theme));
		column2.add(new RadioButtonPanel(gui, I18n.get("settings.window_size"), Main.preferences.size));
		column2.add(Box.createGlue());
		
		// Advanced panel
		mainPanel.add(Box.createVerticalStrut(PADDING));
		mainPanel.add(new Divider(gui));
		mainPanel.add(Box.createVerticalStrut(PADDING));
		mainPanel.add(new CheckboxPanel(gui, I18n.get("settings.show_advanced_options"), Main.preferences.showAdvancedOptions));
		advPanel = new JPanel();
		advPanel.setOpaque(false);
		advPanel.setLayout(new GridLayout(1, 2, PADDING, 0));
		advPanel.setBorder(new EmptyBorder(0, PADDING, PADDING, PADDING));
		advPanel.setVisible(Main.preferences.showAdvancedOptions.get());
		JPanel ac1 = new JPanel();
		JPanel ac2 = new JPanel();
		ac1.setOpaque(false);
		ac2.setOpaque(false);
		advPanel.add(ac1);
		advPanel.add(ac2);
		ac1.setLayout(new BoxLayout(ac1, BoxLayout.Y_AXIS));
		ac2.setLayout(new GridBagLayout());
		settingsPanel.add(advPanel);
		
		// Left advanced column
		sigma = new TextboxPanel(gui, I18n.get("settings.standard_deviation"), Main.preferences.sigma);
		ac1.add(sigma);
		JButton calibrateButton = new FlatButton(gui, I18n.get("settings.calibrate_standard_deviation")) {
			private static final long serialVersionUID = -673676238214760361L;
			@Override
			public int getTextSize(SizePreference p) {
				return p.TEXT_SIZE_SMALL;
			}
		};
		calibrateButton.addActionListener(p -> startCalibrating());
		calibrateButton.setAlignmentX(0.5f);
		ac1.add(calibrateButton);
		ac1.add(new CheckboxPanel(gui, I18n.get("settings.enable_standard_deviation_toggle"), Main.preferences.useAltStd));
		sigmaAlt = new TextboxPanel(gui, I18n.get("settings.alt_standard_deviation"), Main.preferences.sigmaAlt);
		sigmaAlt.setEnabled(Main.preferences.useAltStd.get());
		ac1.add(sigmaAlt);
		if (KeyboardListener.registered) {
			sigmaAltHotkey = new HotkeyPanel(gui, I18n.get("settings.alt_std_on_last_angle"), Main.preferences.hotkeyAltStd);
			sigmaAltHotkey.setEnabled(Main.preferences.useAltStd.get());
			ac1.add(sigmaAltHotkey);
		}
		ac1.add(Box.createVerticalStrut(4));
		ac1.add(new Divider(gui));
		ac1.add(Box.createVerticalStrut(4));
		ac1.add(new TextboxPanel(gui, I18n.get("settings.crosshair_correction"), Main.preferences.crosshairCorrection));
		ac1.add(new CheckboxPanel(gui, I18n.get("settings.show_angle_errors"), Main.preferences.showAngleErrors));
		ac1.add(new CheckboxPanel(gui, I18n.get("settings.use_advanced_stronghold_statistics"), Main.preferences.useAdvStatistics));
		ac1.add(new CheckboxPanel(gui, I18n.get("settings.use_alternative_clipboard_reader"), Main.preferences.altClipboardReader));
		ac1.add(Box.createGlue());
		
		// Right advanced column
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridy = GridBagConstraints.RELATIVE;
		constraints.gridx = 0;
		constraints.insets = new Insets(0, 0, 4, 0);
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weightx = 1;
		if (KeyboardListener.registered) {
			ThemedLabel labelShortcuts = new ThemedLabel(gui, I18n.get("settings.keyboard_shortcuts"), false);
			labelShortcuts.setHorizontalAlignment(0);
			ac2.add(labelShortcuts, constraints);
			ac2.add(new Divider(gui), constraints);
			ac2.add(new HotkeyPanel(gui, I18n.get("settings.up_001_to_last_angle"), Main.preferences.hotkeyIncrement), constraints);
			ac2.add(new HotkeyPanel(gui, I18n.get("settings.down_001_to_last_angle"), Main.preferences.hotkeyDecrement), constraints);
			ac2.add(new HotkeyPanel(gui, I18n.get("reset"), Main.preferences.hotkeyReset), constraints);
			ac2.add(new HotkeyPanel(gui, I18n.get("undo"), Main.preferences.hotkeyUndo), constraints);
			ac2.add(new HotkeyPanel(gui, I18n.get("hide_show_window"), Main.preferences.hotkeyMinimize), constraints);
		}
		ThemedLabel labelOverlay = new ThemedLabel(gui, I18n.get("settings.overlay"), false);
		labelOverlay.setHorizontalAlignment(0);
		ac2.add(labelOverlay, constraints);
		ac2.add(new Divider(gui), constraints);
		ThemedLabel overlayExplanation = new ThemedLabel(gui, "<html>" + I18n.get("settings.overlay_explanation") + "</html>") {
			private static final long serialVersionUID = 7980539999697524316L;
			public int getTextSize(SizePreference p) {
				return p.TEXT_SIZE_SMALL;
			};
			public Dimension getPreferredSize() {
				View view = (View) getClientProperty(javax.swing.plaf.basic.BasicHTML.propertyKey);
				view.setSize(COLUMN_WIDTH, 0);
				float w = view.getPreferredSpan(View.X_AXIS);
				float h = view.getPreferredSpan(View.Y_AXIS);
				return new java.awt.Dimension((int) Math.ceil(w), (int) Math.ceil(h));
			}
		};
		ac2.add(overlayExplanation, constraints);
		ac2.add(new ThemedTextArea(gui, gui.OBS_OVERLAY.getAbsolutePath()), constraints);
		ac2.add(new CheckboxPanel(gui, I18n.get("settings.overlay_enable"), Main.preferences.useOverlay), constraints);
		constraints.weighty = 1;
		ac2.add(Box.createGlue(), constraints);
	}
	
	private void startCalibrating() {
		calibrationPanel.startCalibrating();
		settingsPanel.setVisible(false);
		titlebarPanel.setVisible(false);
		calibrationPanel.setVisible(true);
		if (KeyboardListener.registered) {
			KeyboardListener.instance.cancelConsumer();
		}
	}
	
	public void stopCalibrating() {
		settingsPanel.setVisible(true);
		titlebarPanel.setVisible(true);
		calibrationPanel.setVisible(false);
		calibrationPanel.cancel();
		sigma.updateValue();
	}
	
	public void updateBounds(GUI gui) {
		WINDOW_WIDTH = gui.size.WIDTH * 7 / 4;
		COLUMN_WIDTH = WINDOW_WIDTH/2;
		super.updateBounds(gui);
		pack();
		setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), gui.size.WINDOW_ROUNDING, gui.size.WINDOW_ROUNDING));
	}

	public void updateFontsAndColors() {
		getContentPane().setBackground(gui.theme.COLOR_NEUTRAL);
		setBackground(gui.theme.COLOR_NEUTRAL);
	}

	private FlatButton getExitButton() {
		URL iconURL = Main.class.getResource("/resources/exit_icon.png");
		ImageIcon img = new ImageIcon(iconURL);
		FlatButton button = new TitleBarButton(gui, img) {
			private static final long serialVersionUID = 4380111129291481489L;
			@Override
			public Color getHoverColor(Theme theme) {
				return theme.COLOR_EXIT_BUTTON_HOVER;
			}
		};
		button.addActionListener(p -> close());
		return button;
	}
	
	public void close() {
		setVisible(false);
		stopCalibrating();
		if (KeyboardListener.registered) {
			KeyboardListener.instance.cancelConsumer();
		}
	}
	
	public void setAdvancedOptionsEnabled(boolean b) {
		advPanel.setVisible(b);
		updateBounds(gui);
	}

	public CalibrationPanel getCalibrationPanel() {
		return calibrationPanel;
	}
	
	public void setAltSigmaEnabled(boolean b) {
		sigmaAlt.setEnabled(b);
		sigmaAlt.descLabel.updateColors(gui);
		sigmaAltHotkey.setEnabled(b);
		sigmaAltHotkey.descLabel.updateColors(gui);
	}
	
}

class CheckboxPanel extends ThemedPanel {

	private static final long serialVersionUID = -7054967229481740724L;
	
	JLabel descLabel;
	CustomCheckbox checkbox;
	BooleanPreference preference;
	
	public CheckboxPanel(GUI gui, String description, BooleanPreference preference) {
		super(gui);
		this.preference = preference;
		setLayout(new FlowLayout(FlowLayout.LEFT));
		descLabel = new ThemedLabel(gui, description) {
			private static final long serialVersionUID = 2113195400239083116L;
			@Override
			public int getTextSize(SizePreference p) {
				return p.TEXT_SIZE_SMALL;
			}
		};
		checkbox = new CustomCheckbox(preference.get()) {
			private static final long serialVersionUID = 1507233642665292025L;
			@Override
			public void onChanged(boolean ticked) {
				preference.set(ticked);
				preference.onChangedByUser(gui);
			}
		};
		add(checkbox, BorderLayout.LINE_START);
		add(descLabel, BorderLayout.CENTER);
		setOpaque(false);
	}
	
}

class TextboxPanel extends ThemedPanel {

	private static final long serialVersionUID = -7054967229481740724L;
	
	ThemedLabel descLabel;
	DecimalTextField textfield;
	FloatPreference preference;
	
	public TextboxPanel(GUI gui, String description, FloatPreference preference) {
		super(gui);
		this.preference = preference;
		setLayout(new FlowLayout(FlowLayout.LEFT));
		descLabel = new ThemedLabel(gui, description) {
			private static final long serialVersionUID = 2113195400239083116L;
			@Override
			public int getTextSize(SizePreference p) {
				return p.TEXT_SIZE_SMALL;
			}
			@Override
			public Color getForegroundColor(Theme theme) {
				if (textfield.isEnabled()) {
					return super.getForegroundColor(theme);
				}
				return theme.TEXT_COLOR_WEAK;
			}
		};
		textfield = new DecimalTextField(gui, preference.get(), preference.min(), preference.max()) {
			private static final long serialVersionUID = -1357640224921308648L;
			@Override
			public void onChanged(double newSigma) {
				preference.set((float) newSigma);
				preference.onChangedByUser(gui);
			}
		};
		
		Dimension size = textfield.getPreferredSize();
		size.width = 60;
		textfield.setPreferredSize(size);
		add(Box.createHorizontalStrut(0));
		add(descLabel);
		add(textfield);
		setOpaque(false);
	}
	
	public void updateValue() {
		textfield.setValue((double) preference.get());
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		textfield.setEnabled(enabled);
	}
	
}

class RadioButtonPanel extends ThemedPanel {

	private static final long serialVersionUID = -7054967229481740724L;
	
	JLabel descLabel;
	RadioButtonGroup radioButtomGroup;
	MultipleChoicePreference preference;
	
	public RadioButtonPanel(GUI gui, String description, MultipleChoicePreference preference) {
		super(gui);
		this.preference = preference;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		descLabel = new ThemedLabel(gui, description) {
			private static final long serialVersionUID = 2113195400239083116L;
			@Override
			public int getTextSize(SizePreference p) {
				return p.TEXT_SIZE_SMALL;
			}
		};
		radioButtomGroup = new RadioButtonGroup(gui, preference.getChoices(), preference.get()) {
			private static final long serialVersionUID = -1357640224921308648L;
			@Override
			public void onChanged(String newValue) {
				preference.set(newValue);
				preference.onChangedByUser(gui);
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
	
	public HotkeyPanel(GUI gui, String description, HotkeyPreference preference) {
		super(gui);
		this.preference = preference;
		setLayout(new GridLayout(1, 2, 10, 0));
		descLabel = new ThemedLabel(gui, description) {
			private static final long serialVersionUID = -658733822961822860L;
			@Override
			public int getTextSize(SizePreference p) {
				return p.TEXT_SIZE_SMALL;
			}
			@Override
			public Color getForegroundColor(Theme theme) {
				if (button.isEnabled()) {
					return super.getForegroundColor(theme);
				}
				return theme.TEXT_COLOR_WEAK;
			}
		};
		descLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		button = new FlatButton(gui, getKeyText()) {
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

