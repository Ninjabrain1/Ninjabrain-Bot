package ninjabrainbot.gui.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Locale;

import javax.swing.JLabel;
import javax.swing.border.MatteBorder;

import ninjabrainbot.calculator.DivineContext;
import ninjabrainbot.gui.GUI;
import ninjabrainbot.gui.SizePreference;
import ninjabrainbot.gui.Theme;
import ninjabrainbot.util.I18n;

/**
 * JComponent for showing a Throw.
 */
public class DivineContextPanel extends ThemedPanel {

	private static final long serialVersionUID = -1522335220282509326L;
	
	private DivineContext dc;
	private JLabel label;
	private FlatButton removeButton;

	public DivineContextPanel(GUI gui) {
		this(gui, null);
	}

	public DivineContextPanel(GUI gui, DivineContext dc) {
		super(gui);
		setOpaque(true);
		label = new JLabel((String) null, 0);
		removeButton = new FlatButton(gui, "–") {
			static final long serialVersionUID = -7702064148275208581L;
			@Override
			public Color getHoverColor(Theme theme) {
				return theme.COLOR_REMOVE_BUTTON_HOVER;
			}
			@Override
			public Color getBackgroundColor(Theme theme) {
				return theme.COLOR_STRONGER;
			}
		};
		add(removeButton);
		add(label);
		setLayout(null);
		setDivineContext(dc);
		removeButton.addActionListener(p -> gui.removeDivineContext());
	}
	
	@Override
	public void setFont(Font font) {
		super.setFont(font);
		if (label != null)
			label.setFont(font);
		if (removeButton != null)
			removeButton.setFont(font);
	}

	@Override
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		int w = width - height;
		if (this.label != null)
			this.label.setBounds(0, 0, w, height);
		if (this.removeButton != null)
			this.removeButton.setBounds(w, 0, height, height-1);
	}

	@Override
	public void setForeground(Color fg) {
		super.setForeground(fg);
		if (label != null)
			label.setForeground(fg);
	}
	
	@Override
	public void updateColors(GUI gui) {
		setBorder(new MatteBorder(0, 0, 1, 0, gui.theme.COLOR_STRONGEST));
		super.updateColors(gui);
	}
	
	@Override
	public void updateSize(GUI gui) {
		super.updateSize(gui);
		setPreferredSize(new Dimension(gui.size.WIDTH, gui.size.TEXT_SIZE_SMALL + gui.size.PADDING_THIN * 2));
	}
	
	public void setDivineContext(DivineContext dc) {
		if (dc == null) {
			label.setText(null);
		} else {
			label.setText(I18n.get("divine") + I18n.get("fossil_number", dc.fossil.x));
			removeButton.setVisible(true); 
		}
		this.dc = dc;
	}
	
	public boolean hasDivineContext() {
		return dc != null;
	}

	@Override
	public int getTextSize(SizePreference p) {
		return p.TEXT_SIZE_SMALL;
	}
	
	@Override
	public Color getBackgroundColor(Theme theme) {
		return theme.COLOR_STRONGER;
	}
	
	@Override
	public Color getForegroundColor(Theme theme) {
		return theme.TEXT_COLOR_SLIGHTLY_STRONG;
	}

	
}
