package ninjabrainbot.gui.options.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.Box;

import ninjabrainbot.gui.components.DecimalTextField;
import ninjabrainbot.gui.components.ThemedLabel;
import ninjabrainbot.gui.panels.ThemedPanel;
import ninjabrainbot.gui.style.SizePreference;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.gui.style.WrappedColor;
import ninjabrainbot.io.preferences.FloatPreference;

public class FloatPreferencePanel extends ThemedPanel {

	private static final long serialVersionUID = -7054967229481740724L;

	public ThemedLabel descLabel;
	DecimalTextField textfield;
	FloatPreference preference;

	WrappedColor disabledCol;

	public FloatPreferencePanel(StyleManager styleManager, String description, FloatPreference preference) {
		super(styleManager);
		this.preference = preference;
		setLayout(new FlowLayout(FlowLayout.LEFT));
		descLabel = new ThemedLabel(styleManager, description) {
			private static final long serialVersionUID = 2113195400239083116L;

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
			private static final long serialVersionUID = -1357640224921308648L;

			@Override
			public void onChanged(double newSigma) {
				preference.set((float) newSigma);
			}
		};

		Dimension size = textfield.getPreferredSize();
		size.width = 60;
		textfield.setPreferredSize(size);
		add(Box.createHorizontalStrut(0));
		add(descLabel);
		add(textfield);
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

}