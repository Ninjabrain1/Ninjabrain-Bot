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

import ninjabrainbot.gui.style.SizePreference;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.gui.style.WrappedColor;

public class DecimalTextField extends JSpinner implements ThemedComponent {

	private static final long serialVersionUID = 1167120412326064670L;

	private WrappedColor bgCol;
	private WrappedColor fgCol;

	public DecimalTextField(StyleManager styleManager, float value, float min, float max) {
		super(new SpinnerNumberModel(value, -1e7, 1e7, min));
		setBorder(BorderFactory.createEmptyBorder());
		setAlignmentX(1);
		setFocusable(false);
		hideSpinnerArrows();
		ChangeListener listener = new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				try {
					double newSigma = (double) ((JSpinner) e.getSource()).getValue();
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
		styleManager.registerThemedComponent(this);

		bgCol = styleManager.currentTheme.COLOR_STRONG;
		fgCol = styleManager.currentTheme.TEXT_COLOR_STRONG;
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
	public void updateSize(StyleManager styleManager) {
		setFont(styleManager.fontSize(getTextSize(styleManager.size), true));
	}

	@Override
	public void updateColors() {
		Color bg = getBackgroundColor();
		if (bg != null)
			setBackground(bg);
		Color fg = getForegroundColor();
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

	public void setBackgroundColor(WrappedColor color) {
		bgCol = color;
	}

	public void setForegroundColor(WrappedColor color) {
		fgCol = color;
	}

	protected Color getBackgroundColor() {
		return bgCol.color();
	}

	protected Color getForegroundColor() {
		return fgCol.color();
	}

}
