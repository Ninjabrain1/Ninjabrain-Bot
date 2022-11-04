package ninjabrainbot.gui.frames;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.geom.RoundRectangle2D;

import javax.swing.BoxLayout;

import ninjabrainbot.data.IDataStateHandler;
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

public class ThemeEditorFrame extends ThemedFrame {

	private static final long serialVersionUID = -7008829231721934904L;

	private ConfigurableColorPanel selectedPanel;
	private ColorPickerPanel colorPickerPanel;
	private FramePreviewPanel ninBotPreview;

	private StyleManager previewStyleManager;
	private CustomTheme customTheme;

	public ThemeEditorFrame(StyleManager styleManager, String title) {
		super(styleManager, title);
		customTheme = Theme.getCustomTheme();
		previewStyleManager = new StyleManager(customTheme, SizePreference.REGULAR);

		ThemedPanel panel = new ThemedPanel(styleManager);
		panel.setOpaque(false);
		add(panel);
		panel.setLayout(new FlowLayout());

		colorPickerPanel = new ColorPickerPanel(styleManager);

		IDataStateHandler dataStateHandler = new PreviewDataStateHandler(new PreviewCalculatorResult());
		ninBotPreview = new FramePreviewPanel(new NinjabrainBotFrame(previewStyleManager, dataStateHandler.getDataState(), dataStateHandler));

		panel.add(getConfigurableColorsPanel(styleManager));
		panel.add(colorPickerPanel);
		panel.add(ninBotPreview);

		previewStyleManager.init();
		ninBotPreview.postInit();

		colorPickerPanel.whenColorChanged().subscribe(color -> onColorChanged(color));
	}

	private ThemedPanel getConfigurableColorsPanel(StyleManager styleManager) {
		ThemedPanel panel = new ThemedPanel(styleManager);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		for (ConfigurableColor cc : customTheme.getConfigurableColors()) {
			ConfigurableColorPanel ccPanel = new ConfigurableColorPanel(styleManager, previewStyleManager, cc);
			ccPanel.addActionListener(__ -> setSelectedConfigurableColorPanel(ccPanel));
			panel.add(ccPanel);
		}
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

		ninBotPreview.renderImage();
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
