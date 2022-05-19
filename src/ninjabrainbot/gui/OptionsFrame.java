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
import ninjabrainbot.gui.components.FlatButton;
import ninjabrainbot.gui.components.RadioButtonGroup;
import ninjabrainbot.gui.components.ThemedFrame;
import ninjabrainbot.gui.components.ThemedLabel;
import ninjabrainbot.gui.components.ThemedPanel;
import ninjabrainbot.gui.components.ThemedTabbedPane;
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
	private ThemedTabbedPane tabbedPane;
	private CalibrationPanel calibrationPanel;
	private FlatButton exitButton;
	private TextboxPanel sigma;
	private TextboxPanel sigmaAlt;
	private HotkeyPanel sigmaAltHotkey;
	private TextboxPanel overlayResetDelay;
	
	static int WINDOW_WIDTH = 560;
	static int COLUMN_WIDTH = WINDOW_WIDTH/2;
	static int PADDING = 6;
	
	private static final String TITLE_TEXT = I18n.get("settings");

	public OptionsFrame(GUI gui) {
		super(gui, TITLE_TEXT);
		this.gui = gui;
		setLayout(null);
		tabbedPane = new ThemedTabbedPane(gui);
		add(tabbedPane);
		calibrationPanel = new CalibrationPanel(gui, this);
		add(calibrationPanel);
		tabbedPane.addTab(I18n.get("settings.basic"), getBasicPanel());
		tabbedPane.addTab(I18n.get("settings.advanced"), getAdvancedPanel());
		tabbedPane.addTab(I18n.get("settings.keyboard_shortcuts"), getHotkeyPanel());
		tabbedPane.addTab(I18n.get("settings.overlay"), getOBSPanel());
		
		// Title bar
		exitButton = getExitButton();
		titlebarPanel.addButton(exitButton);
		titlebarPanel.setFocusable(true);
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
		sigma = new TextboxPanel(gui, I18n.get("settings.standard_deviation"), Main.preferences.sigma);
		column1.add(sigma);
		JButton calibrateButton = new FlatButton(gui, I18n.get("settings.calibrate_standard_deviation")) {
			private static final long serialVersionUID = -673676238214760361L;
			@Override
			public int getTextSize(SizePreference p) {
				return p.TEXT_SIZE_SMALL;
			}
		};
		calibrateButton.addActionListener(p -> startCalibrating());
		calibrateButton.setAlignmentX(0.5f);
		column1.add(calibrateButton);
		column1.add(new CheckboxPanel(gui, I18n.get("settings.enable_standard_deviation_toggle"), Main.preferences.useAltStd));
		sigmaAlt = new TextboxPanel(gui, I18n.get("settings.alt_standard_deviation"), Main.preferences.sigmaAlt);
		sigmaAlt.setEnabled(Main.preferences.useAltStd.get());
		column1.add(sigmaAlt);
		if (KeyboardListener.registered) {
			sigmaAltHotkey = new HotkeyPanel(gui, I18n.get("settings.alt_std_on_last_angle"), Main.preferences.hotkeyAltStd);
			sigmaAltHotkey.setEnabled(Main.preferences.useAltStd.get());
			column1.add(sigmaAltHotkey);
		}
		column2.add(new TextboxPanel(gui, I18n.get("settings.crosshair_correction"), Main.preferences.crosshairCorrection));
		column2.add(new CheckboxPanel(gui, I18n.get("settings.show_angle_errors"), Main.preferences.showAngleErrors));
		column2.add(new CheckboxPanel(gui, I18n.get("settings.show_angle_updates"), Main.preferences.showAngleUpdates));
		column2.add(new CheckboxPanel(gui, I18n.get("settings.use_advanced_stronghold_statistics"), Main.preferences.useAdvStatistics));
		column2.add(new CheckboxPanel(gui, I18n.get("settings.use_alternative_clipboard_reader"), Main.preferences.altClipboardReader));
		column2.add(Box.createGlue());
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
			ac2.add(new HotkeyPanel(gui, I18n.get("settings.up_001_to_last_angle"), Main.preferences.hotkeyIncrement), constraints);
			ac2.add(new HotkeyPanel(gui, I18n.get("settings.down_001_to_last_angle"), Main.preferences.hotkeyDecrement), constraints);
			ac2.add(new HotkeyPanel(gui, I18n.get("reset"), Main.preferences.hotkeyReset), constraints);
			ac2.add(new HotkeyPanel(gui, I18n.get("undo"), Main.preferences.hotkeyUndo), constraints);
			ac2.add(new HotkeyPanel(gui, I18n.get("lock"), Main.preferences.hotkeyLock), constraints);
			ac2.add(new HotkeyPanel(gui, I18n.get("hide_show_window"), Main.preferences.hotkeyMinimize), constraints);
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
		ThemedLabel overlayExplanation = new ThemedLabel(gui, "<html>" + I18n.get("settings.overlay_explanation") + "</html>") {
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
		ac2.add(new ThemedTextArea(gui, gui.OBS_OVERLAY.getAbsolutePath()), constraints);
		constraints.insets = new Insets(0, 0, 0, 0);
		ac2.add(new CheckboxPanel(gui, I18n.get("settings.overlay_enable"), Main.preferences.useOverlay), constraints);
		ac2.add(new CheckboxPanel(gui, I18n.get("settings.overlay_hide_locked"), Main.preferences.overlayHideWhenLocked), constraints);
		ac2.add(new CheckboxPanel(gui, I18n.get("settings.overlay_auto_hide"), Main.preferences.overlayAutoHide), constraints);
		overlayResetDelay = new TextboxPanel(gui, I18n.get("settings.overlay_auto_hide_duration"), Main.preferences.overlayHideDelay);
		overlayResetDelay.setEnabled(Main.preferences.overlayAutoHide.get());
		ac2.add(overlayResetDelay, constraints);
		
		constraints.weighty = 1;
		ac2.add(Box.createGlue(), constraints);
		return ac2;
	}
	
	private void startCalibrating() {
		calibrationPanel.startCalibrating();
		tabbedPane.setVisible(false);
		titlebarPanel.setVisible(false);
		calibrationPanel.setVisible(true);
		updateBounds(gui);
		if (KeyboardListener.registered) {
			KeyboardListener.instance.cancelConsumer();
		}
	}
	
	public void stopCalibrating() {
		tabbedPane.setVisible(true);
		titlebarPanel.setVisible(true);
		calibrationPanel.setVisible(false);
		calibrationPanel.cancel();
		updateBounds(gui);
		sigma.updateValue();
	}
	
	public void updateBounds(GUI gui) {
		WINDOW_WIDTH = gui.size.WIDTH * 7 / 4;
		COLUMN_WIDTH = WINDOW_WIDTH/2;
		int titleBarHeight = titlebarPanel.getPreferredSize().height;
		int panelHeight = tabbedPane.getPreferredSize().height;
		titlebarPanel.setBounds(0, 0, WINDOW_WIDTH, titleBarHeight);
		super.updateBounds(gui);
		if (!calibrationPanel.isVisible()) {
			tabbedPane.setBounds(0, titleBarHeight, WINDOW_WIDTH, panelHeight);
			setSize(WINDOW_WIDTH, titleBarHeight + panelHeight);
		} else {
			calibrationPanel.setBounds(0, 0, WINDOW_WIDTH, 2 * panelHeight + titleBarHeight);
			setSize(WINDOW_WIDTH, titleBarHeight + 2 * panelHeight);
		}
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

	public CalibrationPanel getCalibrationPanel() {
		return calibrationPanel;
	}
	
	public void setAltSigmaEnabled(boolean b) {
		sigmaAlt.setEnabled(b);
		sigmaAlt.descLabel.updateColors(gui);
		sigmaAltHotkey.setEnabled(b);
		sigmaAltHotkey.descLabel.updateColors(gui);
	}
	
	public void setOverlayAutoHideEnabled(boolean b) {
		overlayResetDelay.setEnabled(b);
		overlayResetDelay.descLabel.updateColors(gui);
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
		setLayout(new GridLayout(1, 2, 0, 0));
		setBorder(new EmptyBorder(0, 10, 0, 0));
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
		descLabel.setHorizontalAlignment(SwingConstants.LEFT);
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

