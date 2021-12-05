package ninjabrainbot.gui.components;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JRadioButton;

import ninjabrainbot.Main;
import ninjabrainbot.gui.GUI;
import ninjabrainbot.gui.SizePreference;
import ninjabrainbot.gui.Theme;

public class RadioButtonGroup extends ThemedPanel {
	
	private static final long serialVersionUID = 7355615566096074105L;

	public RadioButtonGroup(GUI gui, String[] options, String selected) {
		super(gui);
		setOpaque(false);
        ButtonGroup group = new ButtonGroup();
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JRadioButton btn = (JRadioButton) e.getSource();
                onChanged(btn.getText());
            }
        };
        for (String s : options) {
        	JRadioButton button = new ThemedRadioButton(gui, s);
        	if (selected == s)
        		button.setSelected(true);
        	button.addActionListener(listener);
        	group.add(button);
        	add(button);
        	add(Box.createHorizontalStrut(10));
        }
 	}
	
	public void onChanged(String newValue) {
		
	}
	
}
class ThemedRadioButton extends JRadioButton implements ThemedComponent {

	private static final long serialVersionUID = 528589569573225972L;
	
	private static ImageIcon icon = new ImageIcon(Main.class.getResource("/resources/radio_icon.png"));
	private static ImageIcon selected_icon = new ImageIcon(Main.class.getResource("/resources/radio_selected_icon.png"));
	private static ImageIcon pressed_icon = new ImageIcon(Main.class.getResource("/resources/radio_pressed_icon.png"));
	private static ImageIcon rollover_icon = new ImageIcon(Main.class.getResource("/resources/radio_rollover_icon.png"));
	private static ImageIcon selected_rollover_icon = new ImageIcon(Main.class.getResource("/resources/radio_selected_rollover_icon.png"));
	
	public ThemedRadioButton(GUI gui, String text) {
		super(text);
		gui.registerThemedComponent(this);
		setOpaque(false);
		setBorderPainted(false);
		setFocusPainted(false);
		setFocusable(false);
		// Icons
		setIcon(icon);
		setSelectedIcon(selected_icon);
		setPressedIcon(pressed_icon);
		setRolloverIcon(rollover_icon);
		setRolloverSelectedIcon(selected_rollover_icon);
	}
	
	public final void updateSize(GUI gui) {
		setFont(gui.fontSize(getTextSize(gui.size), true));
	}
	
	public final void updateColors(GUI gui) {
		Color bg = getBackgroundColor(gui.theme);
		if (bg != null)
			setBackground(bg);
		Color fg = getForegroundColor(gui.theme);
		if (fg != null)
			setForeground(fg);
	}
	
	public int getTextSize(SizePreference p) {
		return p.TEXT_SIZE_SMALL;
	}
	
	public Color getBackgroundColor(Theme theme) {
		return null;
	}
	
	public Color getForegroundColor(Theme theme) {
		return theme.TEXT_COLOR_STRONG;
	}
	
}