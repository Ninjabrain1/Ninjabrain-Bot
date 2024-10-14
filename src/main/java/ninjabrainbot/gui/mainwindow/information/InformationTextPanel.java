package ninjabrainbot.gui.mainwindow.information;

import java.awt.BorderLayout;
import java.util.Objects;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import ninjabrainbot.Main;
import ninjabrainbot.gui.components.labels.SmallThemedLabel;
import ninjabrainbot.gui.components.panels.ThemedPanel;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.gui.style.theme.WrappedColor;
import ninjabrainbot.model.information.InformationMessage;
import ninjabrainbot.model.information.InformationMessageSeverity;

public class InformationTextPanel extends ThemedPanel {

	private static final int iconMarginsHorizontal = 6, iconMarginsVertical = 4;

	private final WrappedColor borderColor;
	private final JLabel icon;
	private final JLabel textLabel;

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
		icon.setIcon(getIcon(informationMessage.severity));
		textLabel.setText("<html>" + informationMessage.message + "</html>");
	}

	@Override
	public void updateColors() {
		super.updateColors();
		setBorder(new MatteBorder(0, 0, 1, 0, borderColor.color()));
	}

	private ImageIcon getIcon(InformationMessageSeverity informationMessageSeverity) {
		String imagePath = null;
		switch (informationMessageSeverity) {
			case INFO:
				imagePath = "/info_icon.png";
				break;
			case WARNING:
				imagePath = "/warning_icon.png";
				break;
			case ERROR:
				imagePath = "/warning_icon.png";
				break;
		}
		return new ImageIcon(Objects.requireNonNull(Main.class.getResource(imagePath)));
	}

}
