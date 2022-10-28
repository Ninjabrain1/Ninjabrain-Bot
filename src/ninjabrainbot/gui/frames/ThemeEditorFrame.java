package ninjabrainbot.gui.frames;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.geom.RoundRectangle2D;

import ninjabrainbot.gui.panels.ThemedPanel;
import ninjabrainbot.gui.panels.settings.ColorPickerPanel;
import ninjabrainbot.gui.style.StyleManager;

public class ThemeEditorFrame extends ThemedFrame {

	private static final long serialVersionUID = -7008829231721934904L;

	public ThemeEditorFrame(StyleManager styleManager, String title) {
		super(styleManager, title);
		ThemedPanel panel = new ThemedPanel(styleManager);
		add(panel);
		panel.setLayout(new FlowLayout());

		panel.add(new ColorPickerPanel(styleManager));
	}

	@Override
	public void updateBounds(StyleManager styleManager) {
		super.updateBounds(styleManager);
		Dimension d = getPreferredSize();
		setSize(d.width, d.height);
		setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), styleManager.size.WINDOW_ROUNDING, styleManager.size.WINDOW_ROUNDING));
	}

	@Override
	public void updateFontsAndColors(StyleManager styleManager) {
		getContentPane().setBackground(styleManager.theme.COLOR_NEUTRAL);
		setBackground(styleManager.theme.COLOR_NEUTRAL);
	}

	@Override
	protected void onExitButtonClicked() {
		dispose();
	}

}
