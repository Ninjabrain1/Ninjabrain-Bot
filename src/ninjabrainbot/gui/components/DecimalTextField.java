package ninjabrainbot.gui.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicSpinnerUI;

import ninjabrainbot.gui.GUI;
import ninjabrainbot.gui.SizePreference;
import ninjabrainbot.gui.Theme;

public class DecimalTextField extends JSpinner implements ThemedComponent {
	
	private static final long serialVersionUID = 1167120412326064670L;

	public DecimalTextField(GUI gui, float value, float min, float max) {
		super(new SpinnerNumberModel(value, 0, 1e7, min));
		setBorder(BorderFactory.createEmptyBorder());
		setAlignmentX(1);
		setFocusable(false);
		hideSpinnerArrows();
		ChangeListener listener = new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				try {
					double newSigma = (double)((JSpinner)e.getSource()).getValue();
					if (newSigma > max) {
						newSigma = max;
						setValue(newSigma);
					} else if (newSigma < min) {
						newSigma = min;
						setValue(newSigma);
					}
					onChanged(newSigma);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		};
		addChangeListener(listener);
		gui.registerThemedComponent(this);
	}
	
	public void hideSpinnerArrows() {
		Dimension d = getPreferredSize();
		setUI(new BasicSpinnerUI() {
			protected Component createNextButton() {
				return null;
			}
			protected Component createPreviousButton() {
				return null;
			}
		});
		setPreferredSize(d);
	}

	public void onChanged(double newSigma) {
	}

	@Override
	public void updateSize(GUI gui) {
		setFont(gui.fontSize(getTextSize(gui.size), true));
	}

	@Override
	public void updateColors(GUI gui) {
		Color bg = getBackgroundColor(gui.theme);
		if (bg != null)
			setBackground(bg);
		Color fg = getForegroundColor(gui.theme);
		if (fg != null) {
			setForeground(fg);
		}
	}
	
	@Override
	public void setBackground(Color bg) {
		super.setBackground(bg);
		getEditor().getComponent(0).setBackground(bg);
	}
	
	@Override
	public void setForeground(Color bg) {
		super.setForeground(bg);
		JFormattedTextField field = (JFormattedTextField) getEditor().getComponent(0);
		field.setForeground(bg);
		field.setCaretColor(bg);
	}
	
	public int getTextSize(SizePreference p) {
		return p.TEXT_SIZE_SMALL;
	}
	
	public Color getBackgroundColor(Theme theme) {
		return theme.COLOR_STRONG;
	}
	
	public Color getForegroundColor(Theme theme) {
		return theme.TEXT_COLOR_STRONG;
	}
	
}
