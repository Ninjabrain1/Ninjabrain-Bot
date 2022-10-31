package ninjabrainbot.gui.frames;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.geom.RoundRectangle2D;

import ninjabrainbot.gui.panels.ThemedPanel;
import ninjabrainbot.gui.panels.settings.themeeditor.ColorPickerPanel;
import ninjabrainbot.gui.style.StyleManager;

public class ThemeEditorFrame extends ThemedFrame {

	private static final long serialVersionUID = -7008829231721934904L;

	public ThemeEditorFrame(StyleManager styleManager, String title) {
		super(styleManager, title);
		ThemedPanel panel = new ThemedPanel(styleManager);
		add(panel);
		panel.setLayout(new FlowLayout());

		ColorPickerPanel colorPickerPanel = new ColorPickerPanel(styleManager);
		
		panel.add(colorPickerPanel);
		
		colorPickerPanel.whenColorChanged().subscribe(color -> onColorChanged(styleManager, color));
	}
	
	private void onColorChanged(StyleManager styleManager, Color color) {
		styleManager.getCustomTheme().COLOR_NEUTRAL.set(color);
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
