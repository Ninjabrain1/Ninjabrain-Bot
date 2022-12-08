package ninjabrainbot.gui.settings.themeeditor.panels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;

import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

import ninjabrainbot.gui.buttons.FlatButton;
import ninjabrainbot.gui.panels.ThemedPanel;
import ninjabrainbot.gui.panels.settings.ColorPreviewPanel;
import ninjabrainbot.gui.style.ConfigurableColor;
import ninjabrainbot.gui.style.StyleManager;

public class ConfigurableColorPanel extends ThemedPanel {

	private static final long serialVersionUID = 8202127698667220002L;

	private ConfigurableColor configurableColor;

	private FlatButton colorName;

	public ConfigurableColorPanel(StyleManager styleManager, StyleManager previewStyleManager, ConfigurableColor configurableColor) {
		super(styleManager);
		this.configurableColor = configurableColor;

		ThemedPanel colorPreview = new ColorPreviewPanel(previewStyleManager, configurableColor.color);
		colorName = new LeftAlignedButton(styleManager, configurableColor.name);

		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridy = 0;
		gbc.gridx = GridBagConstraints.RELATIVE;
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0;
		add(colorPreview, gbc);
		gbc.weightx = 1;
		add(colorName, gbc);

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

class LeftAlignedButton extends FlatButton {

	private static final long serialVersionUID = 1439936402421359939L;

	LeftAlignedButton(StyleManager styleManager, String name) {
		super(styleManager, "  " + name);
		label.setHorizontalAlignment(SwingConstants.LEFT);
	}

}
