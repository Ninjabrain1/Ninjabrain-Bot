package ninjabrainbot.gui.components;

import java.awt.Color;
import java.awt.Font;
import java.util.Locale;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;

import ninjabrainbot.Main;
import ninjabrainbot.calculator.Throw;
import ninjabrainbot.gui.GUI;
import ninjabrainbot.gui.TextSizePreference;
import ninjabrainbot.gui.Theme;

/**
 * JComponent for showing a Throw.
 */
public class JThrowPanel extends ThemedPanel {

	private static final long serialVersionUID = -1522335220282509326L;
	
	private Throw t;
	private JLabel x;
	private JLabel z;
	private JLabel alpha;
	private JLabel correction;
	private JLabel error;
	private FlatButton removeButton;

	private boolean errorsEnabled;
	private int correctionSgn;
	private Color colorNeg, colorPos;

	public JThrowPanel(GUI gui, int i) {
		this(gui, i, null);
	}

	public JThrowPanel(GUI gui, int i, Throw t) {
		super(gui);
		setOpaque(true);
		errorsEnabled = Main.preferences.showAngleErrors.get();
		x = new JLabel((String) null, 0);
		z = new JLabel((String) null, 0);
		alpha = new JLabel((String) null, 0);
		correction = new JLabel((String) null, 0);
		error = new JLabel((String) null, 0);
		removeButton = new FlatButton(gui, "–") {
			static final long serialVersionUID = -7702064148275208581L;
			@Override
			public Color getHoverColor(Theme theme) {
				return theme.COLOR_REMOVE_BUTTON_HOVER;
			}
			@Override
			public Color getBackgroundColor(Theme theme) {
				return theme.COLOR_NEUTRAL;
			}
		};
		add(removeButton);
		add(x);
		add(z);
		add(alpha);
		add(correction);
		add(error);
		setLayout(null);
		setThrow(t);
		this.t = t;
		removeButton.addActionListener(p -> gui.removeThrow(this.t));
	}
	
	public void setAngleErrorsEnabled(boolean e) {
		errorsEnabled = e;
	}

	public void setError(double d) {
		error.setText(String.format(Locale.US, "%.3f", d));
	}
	
	public void setError(String s) {
		error.setText(s);
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
		if (correction != null)
			correction.setFont(font);
		if (error != null)
			error.setFont(font);
		if (removeButton != null)
			removeButton.setFont(font);
	}

	@Override
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		int w = width - 2*GUI.THROW_PANEL_PADDING - height;
		if (!errorsEnabled) {
			if (this.x != null)
				this.x.setBounds(GUI.THROW_PANEL_PADDING, 0, w / 3, height);
			if (this.z != null)
				this.z.setBounds(GUI.THROW_PANEL_PADDING + w / 3, 0, w / 3, height);
			if (this.alpha != null) {
				if (correctionSgn != 0) {
					int w1 = w / 3 * 3 / 4;
					int dx = w / 3 * 1 / 8;
					this.alpha.setBounds(GUI.THROW_PANEL_PADDING + 2 * w / 3 - dx, 0, w1, height);
					this.alpha.setHorizontalAlignment(SwingConstants.RIGHT);
					this.correction.setBounds(GUI.THROW_PANEL_PADDING + 2 * w / 3 + w1 - dx, 0, w1, height);
					this.correction.setHorizontalAlignment(SwingConstants.LEFT);
				} else {
					this.alpha.setBounds(GUI.THROW_PANEL_PADDING + 2 * w / 3, 0, w / 3, height);
					this.alpha.setHorizontalAlignment(SwingConstants.CENTER);
				}
			}
			if (this.removeButton != null)
				this.removeButton.setBounds(w, 0, height, height-1);
		} else {
			if (this.x != null)
				this.x.setBounds(GUI.THROW_PANEL_PADDING, 0, w / 4, height);
			if (this.z != null)
				this.z.setBounds(GUI.THROW_PANEL_PADDING + w / 4, 0, w / 4, height);
			if (this.alpha != null) {
				if (correctionSgn != 0) {
					int w1 = w / 4 * 3 / 4;
					int dx = w / 4 * 1 / 8;
					this.alpha.setBounds(GUI.THROW_PANEL_PADDING + 2 * w / 4 - dx, 0, w1, height);
					this.alpha.setHorizontalAlignment(SwingConstants.RIGHT);
					this.correction.setBounds(GUI.THROW_PANEL_PADDING + 2 * w / 4 + w1 - dx, 0, w1, height);
					this.correction.setHorizontalAlignment(SwingConstants.LEFT);
				} else {
					this.alpha.setBounds(GUI.THROW_PANEL_PADDING + 2 * w / 4, 0, w / 4, height);
					this.alpha.setHorizontalAlignment(SwingConstants.CENTER);
				}
			}
			if (this.error != null)
				this.error.setBounds(GUI.THROW_PANEL_PADDING + 3 * w / 4, 0, w / 4, height);
			if (this.removeButton != null)
				this.removeButton.setBounds(w, 0, height, height-1);
		}
		error.setVisible(errorsEnabled);
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
		if (correction != null)
			correction.setForeground(correctionSgn > 0 ? colorPos : colorNeg);
		if (error != null)
			error.setForeground(fg);
	}
	
	@Override
	public void updateColors(GUI gui) {
		colorNeg = gui.theme.COLOR_NEGATIVE;
		colorPos = gui.theme.COLOR_POSITIVE;
		setBorder(new MatteBorder(0, 0, 1, 0, gui.theme.COLOR_STRONGER));
		super.updateColors(gui);
	}

	public void setThrow(Throw t) {
		if (t == null) {
			x.setText(null);
			z.setText(null);
			alpha.setText(null);
			correction.setText(null);
			removeButton.setVisible(false);
			correctionSgn = 0;
		} else {
			x.setText(String.format(Locale.US, "%.2f", t.x));
			z.setText(String.format(Locale.US, "%.2f", t.z));
			alpha.setText(String.format(Locale.US, "%.2f", t.alpha - t.correction));
			correctionSgn = Math.abs(t.correction) < 1e-7 ? 0 : (t.correction > 0 ? 1 : -1);
			if (correctionSgn != 0) {
				correction.setText(String.format(Locale.US, t.correction > 0 ? "+%.2f" : "%.2f", t.correction));
				correction.setForeground(t.correction > 0 ? colorPos : colorNeg);
			} else {
				correction.setText(null);
			}
			removeButton.setVisible(true); 
		}
		this.t = t;
	}

	@Override
	public int getTextSize(TextSizePreference p) {
		return p.THROW_TEXT_SIZE;
	}
	
	@Override
	public Color getBackgroundColor(Theme theme) {
		return theme.COLOR_NEUTRAL;
	}
	
	@Override
	public Color getForegroundColor(Theme theme) {
		return theme.TEXT_COLOR_NEUTRAL;
	}

	
}
