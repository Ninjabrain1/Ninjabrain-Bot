package ninjabrainbot.gui.panels.information;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;

import ninjabrainbot.Main;
import ninjabrainbot.data.information.InformationMessage;
import ninjabrainbot.gui.components.SmallThemedLabel;
import ninjabrainbot.gui.panels.ThemedPanel;
import ninjabrainbot.gui.style.StyleManager;

@SuppressWarnings("serial")
public class InformationTextPanel extends ThemedPanel {

	final InformationMessage informationMessage;

	public InformationTextPanel(StyleManager styleManager, InformationMessage informationMessage) {
		super(styleManager);
		this.informationMessage = informationMessage;
		setLayout(new BorderLayout(6, 0));
		setBorder(new EmptyBorder(0, 6, 0, 6));
		
		add(new JLabel(new ImageIcon(Main.class.getResource("/warning_icon.png"))), BorderLayout.WEST);
		add(new SmallThemedLabel(styleManager, "<html>" + informationMessage.message + "</html>"), BorderLayout.CENTER);
	}

}
