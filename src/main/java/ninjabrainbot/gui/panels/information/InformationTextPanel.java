package ninjabrainbot.gui.panels.information;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import ninjabrainbot.Main;
import ninjabrainbot.data.information.InformationMessage;
import ninjabrainbot.data.information.InformationType;
import ninjabrainbot.gui.components.SmallThemedLabel;
import ninjabrainbot.gui.panels.ThemedPanel;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.gui.style.WrappedColor;

@SuppressWarnings("serial")
public class InformationTextPanel extends ThemedPanel {

	private static final int iconMarginsHorizontal = 6, iconMarginsVertical = 4;

	private WrappedColor borderColor;
	private JLabel icon;
	private JLabel textLabel;

	public InformationTextPanel(StyleManager styleManager) {
		super(styleManager);
		setLayout(new BorderLayout(0, 0));
		borderColor = styleManager.currentTheme.COLOR_DIVIDER;
		icon = new JLabel();
		icon.setBorder(new EmptyBorder(iconMarginsVertical, iconMarginsHorizontal, iconMarginsVertical, iconMarginsHorizontal));
		textLabel = new SmallThemedLabel(styleManager);
		add(icon, BorderLayout.WEST);
		add(textLabel, BorderLayout.CENTER);
	}

	public void setInformationMessage(InformationMessage informationMessage) {
		icon.setIcon(getIcon(informationMessage.type));
		textLabel.setText("<html>" + informationMessage.message + "</html>");
	}

	@Override
	public void updateColors() {
		super.updateColors();
		setBorder(new MatteBorder(0, 0, 1, 0, borderColor.color()));
	}

	private ImageIcon getIcon(InformationType informationType) {
		String imagePath = null;
		switch (informationType) {
		case Info:
			imagePath = "/info_icon.png";
			break;
		case Warning:
			imagePath = "/warning_icon.png";
			break;
		case Error:
			imagePath = null;
			break;
		}
		return new ImageIcon(Main.class.getResource(imagePath));
	}

}
