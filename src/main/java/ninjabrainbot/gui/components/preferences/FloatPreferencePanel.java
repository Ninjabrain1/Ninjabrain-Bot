package ninjabrainbot.gui.components.preferences;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.text.DecimalFormat;
import java.util.Collections;

import javax.swing.Box;
import javax.swing.JSpinner;

import ninjabrainbot.gui.components.inputfields.DecimalTextField;
import ninjabrainbot.gui.components.labels.ThemedLabel;
import ninjabrainbot.gui.components.panels.ThemedPanel;
import ninjabrainbot.gui.frames.OptionsFrame;
import ninjabrainbot.gui.style.SizePreference;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.gui.style.theme.WrappedColor;
import ninjabrainbot.io.preferences.FloatPreference;

public class FloatPreferencePanel extends ThemedPanel {

	public ThemedLabel descLabel;
	DecimalTextField textfield;
	FloatPreference preference;
	DecimalFormat format;

	WrappedColor disabledCol;

	public FloatPreferencePanel(StyleManager styleManager, String description, FloatPreference preference) {
		super(styleManager);
		this.preference = preference;
//		setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		setLayout(new BorderLayout());
		descLabel = new ThemedLabel(styleManager, description) {
			@Override
			public int getTextSize(SizePreference p) {
				return p.TEXT_SIZE_SMALL;
			}
			@Override
			public Color getForegroundColor() {
				if (textfield.isEnabled()) {
					return super.getForegroundColor();
				}
				return disabledCol.color();
			}
		};
		textfield = new DecimalTextField(styleManager, preference.get(), preference.min(), preference.max()) {
			@Override
			public void onChanged(double newValue) {
				preference.set(Float.parseFloat(format.format(newValue)));
			}
		};
		this.setDecimals(4);

		Dimension size = textfield.getPreferredSize();
		size.width = 80;
		textfield.setPreferredSize(size);
		add(descLabel, BorderLayout.WEST);
		add(Box.createGlue(), BorderLayout.CENTER);
		add(textfield, BorderLayout.EAST);
		setOpaque(false);

		disabledCol = styleManager.currentTheme.TEXT_COLOR_WEAK;
	}

	public void updateValue() {
		textfield.setValue((double) preference.get());
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		textfield.setEnabled(enabled);
	}

	public void setWidth(int width) {
		Dimension size = textfield.getPreferredSize();
		size.width = width;
		textfield.setPreferredSize(size);
	}

	public void setDecimals(int decimals) {
		String newFormat = "#";
		if (decimals > 0) {
			newFormat += "." + String.join("", Collections.nCopies(decimals, "#"));
		}
		this.format = new DecimalFormat(newFormat);
		textfield.setEditor(new JSpinner.NumberEditor(textfield, newFormat));
	}

}