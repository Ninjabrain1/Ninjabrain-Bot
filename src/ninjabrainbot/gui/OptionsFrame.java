package ninjabrainbot.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.geom.RoundRectangle2D;
import java.net.URL;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

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
import ninjabrainbot.gui.components.TitleBarButton;
import ninjabrainbot.io.BooleanPreference;
import ninjabrainbot.io.FloatPreference;
import ninjabrainbot.io.MultipleChoicePreference;

public class OptionsFrame extends ThemedFrame {

	private static final long serialVersionUID = 8033865173874423916L;

	private GUI gui;
	private JPanel settingsPanel;
	private CalibrationPanel calibrationPanel;
	private FlatButton exitButton;
	private JPanel mainPanel; // Panel containing all non-advanced options
	private JPanel advPanel; // Panel containing all advanced options

	static final int WINDOW_WIDTH = 560;
	static final int COLUMN_WIDTH = WINDOW_WIDTH/2;
	static final int PADDING = 6;
	
	private static final String TITLE_TEXT = "Settings";

	public OptionsFrame(GUI gui) {
		super(gui, TITLE_TEXT);
		this.gui = gui;
		
		// Windows that can be swapped between
		settingsPanel = new JPanel();
		settingsPanel.setOpaque(false);
		settingsPanel.setLayout(null);
		add(settingsPanel);
		calibrationPanel = new CalibrationPanel(gui, this);
		add(calibrationPanel);
		
		// Title bar
		exitButton = getExitButton();
		titlebarPanel.addButton(exitButton);
		
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
		columns.add(column1, BorderLayout.LINE_START);
		columns.add(column2, BorderLayout.CENTER);
		column1.setLayout(new BoxLayout(column1, BoxLayout.Y_AXIS));
		column2.setLayout(new BoxLayout(column2, BoxLayout.Y_AXIS));
		mainPanel.add(columns);
		
		// Column 1
		column1.add(new CheckboxPanel(gui, "Show nether coordinates", Main.preferences.showNetherCoords));
		column1.add(new CheckboxPanel(gui, "Auto reset when idle for 15 minutes", Main.preferences.autoReset));
		column1.add(new CheckboxPanel(gui, "Always on top", Main.preferences.alwaysOnTop));
		column1.add(new CheckboxPanel(gui, "Translucent window", Main.preferences.translucent));
		column1.add(new CheckboxPanel(gui, "Notify when a new version is available", Main.preferences.checkForUpdates));
		
		// Column 2
		column2.add(Box.createVerticalStrut(10));
		column2.add(new RadioButtonPanel(gui, "Display stronghold location using", Main.preferences.strongholdDisplayType));
		column2.add(new RadioButtonPanel(gui, "Theme", Main.preferences.theme));
		column2.add(Box.createGlue());
		column2.add(Box.createGlue());
		column2.add(Box.createGlue());
		
		// Advanced panel
		mainPanel.add(Box.createVerticalStrut(PADDING));
		mainPanel.add(new Divider(gui));
		mainPanel.add(Box.createVerticalStrut(PADDING));
		mainPanel.add(new CheckboxPanel(gui, "Show advanced options", Main.preferences.showAdvancedOptions));
		advPanel = new JPanel();
		advPanel.setOpaque(false);
		advPanel.setLayout(new BoxLayout(advPanel, BoxLayout.Y_AXIS));
		advPanel.setBorder(new EmptyBorder(0, PADDING, PADDING, PADDING));
		advPanel.setVisible(Main.preferences.showAdvancedOptions.get());
		settingsPanel.add(advPanel);
		
		advPanel.add(new TextboxPanel(gui, "Standard deviation: ", Main.preferences.sigma));
		JButton calibrateButton = new FlatButton(gui, "Calibrate standard deviation") {
			private static final long serialVersionUID = -673676238214760361L;
			@Override
			public int getTextSize(TextSizePreference p) {
				return p.SETTINGS_TEXT_SIZE;
			}
			
		};
		calibrateButton.addActionListener(p -> startCalibrating());
		calibrateButton.setAlignmentX(1);
		advPanel.add(calibrateButton);
		advPanel.add(new CheckboxPanel(gui, "Show angle errors", Main.preferences.showAngleErrors));
		
	}
	
	private void startCalibrating() {
		calibrationPanel.startCalibrating();
		settingsPanel.setVisible(false);
		titlebarPanel.setVisible(false);
		calibrationPanel.setVisible(true);
	}
	
	public void stopCalibrating() {
		settingsPanel.setVisible(true);
		titlebarPanel.setVisible(true);
		calibrationPanel.setVisible(false);
		calibrationPanel.cancel();
	}
	
	public void updateBounds(GUI gui) {
		int width = WINDOW_WIDTH;
		int h1 = mainPanel.getPreferredSize().height;
		int h2 = Main.preferences.showAdvancedOptions.get() ? advPanel.getPreferredSize().height : 0;
		int height = GUI.TITLE_BAR_HEIGHT + h1 + h2;
		setShape(new RoundRectangle2D.Double(0, 0, width, height, GUI.WINDOW_ROUNDING, GUI.WINDOW_ROUNDING));
		setSize(width, height);
		settingsPanel.setSize(width, height);
		calibrationPanel.setSize(width, height);
//		titlebarPanel.setBounds(0, 0, width, GUI.TITLE_BAR_HEIGHT);
//		titletextLabel.setBounds((GUI.TITLE_BAR_HEIGHT - gui.textSize.TITLE_BAR_TEXT_SIZE)/2, 0, 150, GUI.TITLE_BAR_HEIGHT);
//		exitButton.setBounds(width - GUI.TITLE_BAR_BUTTON_WH - (GUI.TITLE_BAR_HEIGHT - GUI.TITLE_BAR_BUTTON_WH)/2, (GUI.TITLE_BAR_HEIGHT - GUI.TITLE_BAR_BUTTON_WH)/2, GUI.TITLE_BAR_BUTTON_WH, GUI.TITLE_BAR_BUTTON_WH);
		mainPanel.setBounds(0, GUI.TITLE_BAR_HEIGHT, WINDOW_WIDTH, h1);
		advPanel.setBounds(0, GUI.TITLE_BAR_HEIGHT + h1, width, h2);
		super.updateBounds(gui);
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
		button.addActionListener(p -> setVisible(false));
		return button;
	}
	
	public void setAdvancedOptionsEnabled(boolean b) {
		advPanel.setVisible(b);
		updateBounds(gui);
	}

	public CalibrationPanel getCalibrationPanel() {
		return calibrationPanel;
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
		descLabel = new ThemedLabel(gui, "<html>"+ description +"</html>") {
			private static final long serialVersionUID = 2113195400239083116L;
			@Override
			public int getTextSize(TextSizePreference p) {
				return p.SETTINGS_TEXT_SIZE;
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
		add(checkbox);
		add(descLabel);
		setOpaque(false);
	}
	
}

class TextboxPanel extends ThemedPanel {

	private static final long serialVersionUID = -7054967229481740724L;
	
	JLabel descLabel;
	DecimalTextField textfield;
	FloatPreference preference;
	
	public TextboxPanel(GUI gui, String description, FloatPreference preference) {
		super(gui);
		this.preference = preference;
		setLayout(new FlowLayout(FlowLayout.LEFT));
		descLabel = new ThemedLabel(gui, "<html>"+ description +"</html>") {
			private static final long serialVersionUID = 2113195400239083116L;
			@Override
			public int getTextSize(TextSizePreference p) {
				return p.SETTINGS_TEXT_SIZE;
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
		size.width = 50;
		textfield.setPreferredSize(size);
		add(Box.createHorizontalStrut(0));
		add(descLabel);
		add(textfield);
		setOpaque(false);
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
		descLabel = new ThemedLabel(gui, "<html>"+ description +"</html>") {
			private static final long serialVersionUID = 2113195400239083116L;
			@Override
			public int getTextSize(TextSizePreference p) {
				return p.SETTINGS_TEXT_SIZE;
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

