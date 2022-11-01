package ninjabrainbot.gui.panels.settings.themeeditor;

import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

import ninjabrainbot.gui.buttons.FlatButton;
import ninjabrainbot.gui.panels.ThemedPanel;
import ninjabrainbot.gui.style.ConfigurableColor;
import ninjabrainbot.gui.style.StyleManager;

public class ConfigurableColorPanel extends ThemedPanel {

	private static final long serialVersionUID = 8202127698667220002L;

	private ConfigurableColor configurableColor;

	private FlatButton colorName;

	public ConfigurableColorPanel(StyleManager styleManager, StyleManager previewStyleManager, ConfigurableColor configurableColor) {
		super(styleManager);
		this.configurableColor = configurableColor;

		ThemedPanel colorPreview = new ColorPreviewPanel(previewStyleManager, configurableColor);
		colorName = new FlatButton(styleManager, configurableColor.name);

		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		add(colorPreview);
		add(colorName);

		setBorder(new EmptyBorder(2, 2, 2, 2));
	}

	public ConfigurableColor getConfigurableColor() {
		return configurableColor;
	}

	public void addActionListener(ActionListener l) {
		colorName.addActionListener(l);
	}

	public void setSelected(boolean b) {
		setBorder(b ? new BevelBorder(BevelBorder.LOWERED) : new EmptyBorder(2, 2, 2, 2));
	}
}

class ColorPreviewPanel extends ThemedPanel {

	private static final long serialVersionUID = -204692569559526638L;

	public ColorPreviewPanel(StyleManager styleManager, ConfigurableColor configurableColor) {
		super(styleManager);
		setBackgroundColor(configurableColor.color);
	}

	@Override
	public void updateSize(StyleManager styleManager) {
		super.updateSize(styleManager);
		int textSize = getTextSize(styleManager.size);
		setPreferredSize(new Dimension(textSize * 2, textSize * 2));
	}
}
