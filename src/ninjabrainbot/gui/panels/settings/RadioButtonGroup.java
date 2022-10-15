package ninjabrainbot.gui.panels.settings;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JRadioButton;

import ninjabrainbot.Main;
import ninjabrainbot.gui.components.ThemedComponent;
import ninjabrainbot.gui.panels.ThemedPanel;
import ninjabrainbot.gui.style.SizePreference;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.gui.style.Theme;

public class RadioButtonGroup extends ThemedPanel {
	
	private static final long serialVersionUID = 7355615566096074105L;

	public RadioButtonGroup(StyleManager styleManager, String[] options, String selected) {
		super(styleManager);
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
        	JRadioButton button = new ThemedRadioButton(styleManager, s);
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
	
	public ThemedRadioButton(StyleManager styleManager, String text) {
		super(text);
		styleManager.registerThemedComponent(this);
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
	
	public final void updateSize(StyleManager styleManager) {
		setFont(styleManager.fontSize(getTextSize(styleManager.size), true));
	}
	
	public final void updateColors(StyleManager styleManager) {
		Color bg = getBackgroundColor(styleManager.theme);
		if (bg != null)
			setBackground(bg);
		Color fg = getForegroundColor(styleManager.theme);
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