package ninjabrainbot.gui.frames;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.geom.RoundRectangle2D;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import ninjabrainbot.data.IDataStateHandler;
import ninjabrainbot.gui.components.Divider;
import ninjabrainbot.gui.components.ThemedLabel;
import ninjabrainbot.gui.panels.ThemedPanel;
import ninjabrainbot.gui.panels.settings.themeeditor.ColorPickerPanel;
import ninjabrainbot.gui.panels.settings.themeeditor.ConfigurableColorPanel;
import ninjabrainbot.gui.panels.settings.themeeditor.FramePreviewPanel;
import ninjabrainbot.gui.panels.settings.themeeditor.PreviewCalculatorResult;
import ninjabrainbot.gui.panels.settings.themeeditor.PreviewDataStateHandler;
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
	private CustomTheme customTheme;

	public ThemeEditorFrame(StyleManager styleManager, NinjabrainBotPreferences preferences) {
		super(styleManager, preferences, "Theme Editor");
		customTheme = Theme.getCustomTheme();
		previewStyleManager = new StyleManager(customTheme, SizePreference.REGULAR);

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
		
		for (ConfigurableColor cc : customTheme.getConfigurableColors()) {
			ConfigurableColorPanel ccPanel = new ConfigurableColorPanel(styleManager, previewStyleManager, cc);
			ccPanel.addActionListener(__ -> setSelectedConfigurableColorPanel(ccPanel));
			if (cc == customTheme.getConfigurableColors().get(0))
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

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = GridBagConstraints.RELATIVE;
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weighty = 0;
		panel.add(createHeader(styleManager, "Color:"), gbc);
		panel.add(new Divider(styleManager), gbc);
		panel.add(colorPickerPanel, gbc);
		gbc.weighty = 1;
		panel.add(Box.createGlue(), gbc);

		return panel;
	}

	private ThemedPanel createPreviewsPanel(StyleManager styleManager) {
		ThemedPanel panel = new ThemedPanel(styleManager);
		panel.setLayout(new GridBagLayout());
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));

		NinjabrainBotPreferences previewPreferences1 = new NinjabrainBotPreferences(new UnsavedPreferences());
		previewPreferences1.view.set(MainViewType.BASIC);
		IDataStateHandler dataStateHandler1 = new PreviewDataStateHandler(new PreviewCalculatorResult(McVersion.PRE_119));
		ninBotPreviewBasic = new FramePreviewPanel(new NinjabrainBotFrame(previewStyleManager, previewPreferences1, dataStateHandler1));

		NinjabrainBotPreferences previewPreferences2 = new NinjabrainBotPreferences(new UnsavedPreferences());
		previewPreferences2.view.set(MainViewType.DETAILED);
		IDataStateHandler dataStateHandler2 = new PreviewDataStateHandler(new PreviewCalculatorResult(McVersion.PRE_119));
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
		previewStyleManager.currentTheme.setTheme(customTheme);

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

}
