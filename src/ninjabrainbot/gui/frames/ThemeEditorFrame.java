package ninjabrainbot.gui.frames;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.geom.RoundRectangle2D;

import javax.swing.BoxLayout;

import ninjabrainbot.gui.panels.ThemedPanel;
import ninjabrainbot.gui.panels.settings.themeeditor.ColorPickerPanel;
import ninjabrainbot.gui.panels.settings.themeeditor.ConfigurableColorPanel;
import ninjabrainbot.gui.style.ConfigurableColor;
import ninjabrainbot.gui.style.StyleManager;

public class ThemeEditorFrame extends ThemedFrame {

	private static final long serialVersionUID = -7008829231721934904L;
	
	private ConfigurableColorPanel selectedPanel;
	private ColorPickerPanel colorPickerPanel;

	public ThemeEditorFrame(StyleManager styleManager, String title) {
		super(styleManager, title);
		ThemedPanel panel = new ThemedPanel(styleManager);
		add(panel);
		panel.setLayout(new FlowLayout());

		colorPickerPanel = new ColorPickerPanel(styleManager);

		panel.add(getConfigurableColorsPanel(styleManager));
		panel.add(colorPickerPanel);

		colorPickerPanel.whenColorChanged().subscribe(color -> onColorChanged(styleManager, color));
	}

	private ThemedPanel getConfigurableColorsPanel(StyleManager styleManager) {
		ThemedPanel panel = new ThemedPanel(styleManager);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		for (ConfigurableColor cc : styleManager.getCustomTheme().getConfigurableColors()) {
			ConfigurableColorPanel ccPanel = new ConfigurableColorPanel(styleManager, cc);
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
	
	private void onColorChanged(StyleManager styleManager, Color color) {
		if (selectedPanel != null)
		selectedPanel.getConfigurableColor().color.set(color);
		styleManager.currentTheme.setTheme(styleManager.getCustomTheme());
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
