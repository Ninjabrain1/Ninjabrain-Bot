package ninjabrainbot.gui.frames;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import ninjabrainbot.data.IDataStateHandler;
import ninjabrainbot.data.datalock.AlwaysUnlocked;
import ninjabrainbot.data.divine.Fossil;
import ninjabrainbot.data.endereye.IThrow;
import ninjabrainbot.data.endereye.Throw;
import ninjabrainbot.gui.buttons.FlatButton;
import ninjabrainbot.gui.components.Divider;
import ninjabrainbot.gui.components.ThemedLabel;
import ninjabrainbot.gui.panels.ThemedPanel;
import ninjabrainbot.gui.settings.themeeditor.PreviewCalculatorResult;
import ninjabrainbot.gui.settings.themeeditor.PreviewDataStateHandler;
import ninjabrainbot.gui.settings.themeeditor.panels.ColorPickerPanel;
import ninjabrainbot.gui.settings.themeeditor.panels.ConfigurableColorPanel;
import ninjabrainbot.gui.settings.themeeditor.panels.FramePreviewPanel;
import ninjabrainbot.gui.style.ConfigurableColor;
import ninjabrainbot.gui.style.CustomTheme;
import ninjabrainbot.gui.style.SizePreference;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.gui.style.Theme;
import ninjabrainbot.io.preferences.MultipleChoicePreferenceDataTypes.MainViewType;
import ninjabrainbot.io.preferences.MultipleChoicePreferenceDataTypes.McVersion;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.io.preferences.UnsavedPreferences;

public class ThemeEditorFrame extends ThemedFrame {

	private static final long serialVersionUID = -7008829231721934904L;

	private ConfigurableColorPanel selectedPanel;
	private ColorPickerPanel colorPickerPanel;

	private FramePreviewPanel ninBotPreviewBasic;
	private FramePreviewPanel ninBotPreviewDetailed;

	private StyleManager previewStyleManager;
	private CustomTheme customTheme; // theme that is saved
	private CustomTheme previewTheme; // used for previewing

	public ThemeEditorFrame(StyleManager styleManager, NinjabrainBotPreferences preferences) {
		super(styleManager, preferences, "Theme Editor");
		previewTheme = new CustomTheme();
		customTheme = Theme.getCustomTheme(-1);
		previewStyleManager = new StyleManager(previewTheme, SizePreference.REGULAR);

		ThemedPanel panel = new ThemedPanel(styleManager);
		panel.setOpaque(false);
		add(panel);
		panel.setLayout(new GridLayout(1, 3));

		panel.add(createMiddlePanel(styleManager));
		panel.add(createConfigurableColorsPanel(styleManager), 0);
		panel.add(createPreviewsPanel(styleManager));

		previewStyleManager.init();
		ninBotPreviewBasic.postInit();
		ninBotPreviewDetailed.postInit();

		colorPickerPanel.whenColorChanged().subscribe(color -> onColorChanged(color));
	}

	private ThemedPanel createConfigurableColorsPanel(StyleManager styleManager) {
		ThemedPanel panel = new ThemedPanel(styleManager);
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		panel.add(createHeader(styleManager, "Select color to edit:"));
		panel.add(new Divider(styleManager));
		panel.add(Box.createVerticalStrut(10));

		for (ConfigurableColor cc : previewTheme.getConfigurableColors()) {
			ConfigurableColorPanel ccPanel = new ConfigurableColorPanel(styleManager, previewStyleManager, cc);
			ccPanel.addActionListener(__ -> setSelectedConfigurableColorPanel(ccPanel));
			if (cc == previewTheme.getConfigurableColors().get(0))
				setSelectedConfigurableColorPanel(ccPanel);
			panel.add(ccPanel);
		}
		return panel;
	}

	private ThemedPanel createMiddlePanel(StyleManager styleManager) {
		ThemedPanel panel = new ThemedPanel(styleManager);
		panel.setLayout(new GridBagLayout());
		panel.setBorder(new EmptyBorder(10, 0, 10, 0));

		colorPickerPanel = new ColorPickerPanel(styleManager);

		FlatButton selectPresetButton = new FlatButton(styleManager, "Select preset");
		selectPresetButton.addActionListener(__ -> openSelectPresetDialog());

		FlatButton saveButton = new FlatButton(styleManager, "Save");
		saveButton.addActionListener(__ -> saveTheme());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = GridBagConstraints.RELATIVE;
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weighty = 0;
		panel.add(createHeader(styleManager, "Color:"), gbc);
		panel.add(new Divider(styleManager), gbc);

		panel.add(selectPresetButton, gbc);
		panel.add(saveButton, gbc);
		panel.add(colorPickerPanel, gbc);
		gbc.weighty = 1;
		panel.add(Box.createGlue(), gbc);

		return panel;
	}

	private ThemedPanel createPreviewsPanel(StyleManager styleManager) {
		ThemedPanel panel = new ThemedPanel(styleManager);
		panel.setLayout(new GridBagLayout());
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));

		ArrayList<IThrow> eyeThrows = new ArrayList<>();
		IThrow t1 = Throw.parseF3C("/execute in minecraft:overworld run tp @s 659.70 85.00 1950.30 -253.82 -31.75", 0, new AlwaysUnlocked());
		t1.addCorrection(0.01);
		t1.setStdProfileNumber(1);
		IThrow t2 = Throw.parseF3C("/execute in minecraft:overworld run tp @s -3.75 66.00 2002.63 -184.67 -31.75", 0, new AlwaysUnlocked());
		t2.addCorrection(-0.01);
		t2.setStdProfileNumber(2);
		eyeThrows.add(t1);
		eyeThrows.add(t2);
		Fossil f = new Fossil(3);

		NinjabrainBotPreferences previewPreferences1 = new NinjabrainBotPreferences(new UnsavedPreferences());
		previewPreferences1.view.set(MainViewType.BASIC);
		IDataStateHandler dataStateHandler1 = new PreviewDataStateHandler(new PreviewCalculatorResult(McVersion.PRE_119), eyeThrows, f);
		ninBotPreviewBasic = new FramePreviewPanel(new NinjabrainBotFrame(previewStyleManager, previewPreferences1, dataStateHandler1));

		NinjabrainBotPreferences previewPreferences2 = new NinjabrainBotPreferences(new UnsavedPreferences());
		previewPreferences2.view.set(MainViewType.DETAILED);
		previewPreferences2.showAngleErrors.set(true);
		IDataStateHandler dataStateHandler2 = new PreviewDataStateHandler(new PreviewCalculatorResult(McVersion.PRE_119), eyeThrows, null);
		ninBotPreviewDetailed = new FramePreviewPanel(new NinjabrainBotFrame(previewStyleManager, previewPreferences2, dataStateHandler2));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = GridBagConstraints.RELATIVE;
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weighty = 0;
		panel.add(createHeader(styleManager, "Preview:"), gbc);
		panel.add(new Divider(styleManager), gbc);
		panel.add(Box.createVerticalStrut(10), gbc);
		panel.add(ninBotPreviewBasic, gbc);
		panel.add(Box.createVerticalStrut(10), gbc);
		panel.add(ninBotPreviewDetailed, gbc);
		gbc.weighty = 1;
		panel.add(Box.createGlue(), gbc);

		return panel;
	}

	private void openSelectPresetDialog() {
		Object[] themes = Theme.getStandardThemes().toArray();
		Theme chosenTheme = (Theme) JOptionPane.showInputDialog(this, "Any unsaved changes will be lost.", "Select preset", JOptionPane.PLAIN_MESSAGE, null, themes, themes[0]);
		if (chosenTheme == null)
			return;
		previewTheme.setFromTheme(chosenTheme);
		updateComponents();
	}

	private void saveTheme() {
		customTheme.setFromTheme(previewTheme);
	}

	private void setSelectedConfigurableColorPanel(ConfigurableColorPanel configurableColorPanel) {
		if (selectedPanel != null)
			selectedPanel.setSelected(false);
		selectedPanel = configurableColorPanel;
		if (configurableColorPanel != null)
			configurableColorPanel.setSelected(true);
		colorPickerPanel.setColor(configurableColorPanel != null ? configurableColorPanel.getConfigurableColor().color.color() : Color.WHITE);
	}

	private void onColorChanged(Color color) {
		if (selectedPanel != null)
			selectedPanel.getConfigurableColor().color.set(color);
		updateComponents();
	}

	private void updateComponents() {
		previewStyleManager.currentTheme.setTheme(previewTheme);
		if (selectedPanel != null) {
			Color c = selectedPanel.getConfigurableColor().color.color();
			colorPickerPanel.setColor(c);
		}

		ninBotPreviewBasic.renderImage();
		ninBotPreviewDetailed.renderImage();
	}

	private ThemedPanel createHeader(StyleManager styleManager, String text) {
		ThemedPanel panel = new ThemedPanel(styleManager);
		panel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		ThemedLabel label = new ThemedLabel(styleManager, text, true, false);
		label.setHorizontalAlignment(SwingConstants.LEFT);
		label.setAlignmentX(0);
		panel.add(label);
		return panel;
	}

	@Override
	public void updateBounds(StyleManager styleManager) {
		super.updateBounds(styleManager);
		Dimension d = getPreferredSize();
		setSize(d.width, d.height);
		setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), styleManager.size.WINDOW_ROUNDING, styleManager.size.WINDOW_ROUNDING));
	}

	@Override
	protected void onExitButtonClicked() {
		dispose();
	}

	@Override
	public void dispose() {
		ninBotPreviewBasic.dispose();
		ninBotPreviewDetailed.dispose();
		super.dispose();
	}

}
