package ninjabrainbot.gui.components.preferences;

import java.awt.Cursor;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;

import ninjabrainbot.Main;

public class CustomCheckbox extends JCheckBox {

	private static final long serialVersionUID = -6503070977470692912L;

	private static ImageIcon icon = new ImageIcon(Main.class.getResource("/checkbox_icon.png"));
	private static ImageIcon selected_icon = new ImageIcon(Main.class.getResource("/checkbox_selected_icon.png"));
	private static ImageIcon pressed_icon = new ImageIcon(Main.class.getResource("/checkbox_pressed_icon.png"));
	private static ImageIcon rollover_icon = new ImageIcon(Main.class.getResource("/checkbox_rollover_icon.png"));
	private static ImageIcon selected_rollover_icon = new ImageIcon(Main.class.getResource("/checkbox_selected_rollover_icon.png"));

	public CustomCheckbox() {
		this(false);
	}

	public CustomCheckbox(boolean ticked) {
		setSelected(ticked);
		setBorderPainted(false);
		setBorderPaintedFlat(false);
		setFocusPainted(false);
		setFocusable(false);
		setOpaque(false);
		setIcon(icon);
		setSelectedIcon(selected_icon);
		setPressedIcon(pressed_icon);
		setRolloverIcon(rollover_icon);
		setRolloverSelectedIcon(selected_rollover_icon);
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				boolean ticked = (boolean) ((JCheckBox) e.getSource()).isSelected();
				onChanged(ticked);
			}
		});
	}

	public void onChanged(boolean ticked) {
	}

}
