package ninjabrainbot.gui.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.border.MatteBorder;

import ninjabrainbot.Main;
import ninjabrainbot.calculator.Throw;
import ninjabrainbot.gui.GUI;
import ninjabrainbot.gui.SizePreference;
import ninjabrainbot.gui.Theme;
import ninjabrainbot.util.I18n;

/**
 * JComponent for showing a Throw.
 */
public class JThrowPanelHeader extends ThemedPanel {

	private static final long serialVersionUID = -1159034678242273505L;
	
	private JLabel x;
	private JLabel z;
	private JLabel alpha;
	private JLabel error;
	
	private boolean errorsEnabled;
	
	public JThrowPanelHeader(GUI gui) {
		this(gui, null);
	}

	public JThrowPanelHeader(GUI gui, Throw t) {
		super(gui, true);
		setOpaque(true);
		errorsEnabled = Main.preferences.showAngleErrors.get();
		x = new JLabel("x", 0);
		z = new JLabel("z", 0);
		alpha = new JLabel(I18n.get("angle"), 0);
		error = new JLabel(I18n.get("error"), 0);
		add(x);
		add(z);
		add(alpha);
		add(error);
		setLayout(null);
	}
	
	public void setAngleErrorsEnabled(boolean e) {
		errorsEnabled = e;
	}

	@Override
	public void setFont(Font font) {
		super.setFont(font);
		if (x != null)
			x.setFont(font);
		if (z != null)
			z.setFont(font);
		if (alpha != null)
			alpha.setFont(font);
		if (error != null)
			error.setFont(font);
	}

	@Override
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		int w = width - 2*0 - height;
		int y0 = -1;
		if (!errorsEnabled) {
			if (this.x != null)
				this.x.setBounds(0, y0, w / 3, height);
			if (this.z != null)
				this.z.setBounds(0 + w / 3, y0, w / 3, height);
			if (this.alpha != null)
				this.alpha.setBounds(0 + 2 * w / 3, y0, w / 3, height);
		} else {
			if (this.x != null)
				this.x.setBounds(0, y0, w / 4, height);
			if (this.z != null)
				this.z.setBounds(0 + w / 4, y0, w / 4, height);
			if (this.alpha != null)
				this.alpha.setBounds(0 + 2 * w / 4, y0, w / 4, height);
			if (this.error != null)
				this.error.setBounds(0 + 3 * w / 4, y0, w / 4, height);
		}
		error.setVisible(errorsEnabled);
	}
	
	@Override
	public void updateSize(GUI gui) {
		super.updateSize(gui);
		setPreferredSize(new Dimension(gui.size.WIDTH, gui.size.TEXT_SIZE_SMALL + gui.size.PADDING_THIN * 2));
	}
	
	@Override
	public void updateColors(GUI gui) {
		super.updateColors(gui);
		setBorder(new MatteBorder(0, 0, 2, 0, gui.theme.COLOR_STRONGEST));
	}
	
	@Override
	public void setForeground(Color fg) {
		super.setForeground(fg);
		if (x != null)
			x.setForeground(fg);
		if (z != null)
			z.setForeground(fg);
		if (alpha != null)
			alpha.setForeground(fg);
		if (error != null)
			error.setForeground(fg);
	}

	@Override
	public int getTextSize(SizePreference p) {
		return p.TEXT_SIZE_SMALL;
	}

	@Override
	public Color getBackgroundColor(Theme theme) {
		return theme.COLOR_STRONG;
	}

	@Override
	public Color getForegroundColor(Theme theme) {
		return theme.TEXT_COLOR_STRONG;
	}

}
