package ninjabrainbot.gui.components.preferences;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.Box;

import ninjabrainbot.gui.components.labels.ThemedLabel;
import ninjabrainbot.gui.frames.OptionsFrame;
import ninjabrainbot.gui.components.panels.ThemedPanel;
import ninjabrainbot.gui.style.SizePreference;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.gui.style.theme.WrappedColor;
import ninjabrainbot.io.preferences.BooleanPreference;

public class CheckboxPanel extends ThemedPanel {

	private static final long serialVersionUID = -7054967229481740724L;

	public ThemedLabel descLabel;
	CustomCheckbox checkbox;
	BooleanPreference preference;

	WrappedColor disabledCol;

	public CheckboxPanel(StyleManager styleManager, String description, BooleanPreference preference) {
		super(styleManager);
		this.preference = preference;
		setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		CheckboxPanel t = this;
		descLabel = new ThemedLabel(styleManager, "<html>" + description + "</html>") {
			private static final long serialVersionUID = 2113195400239083116L;

			@Override
			public int getTextSize(SizePreference p) {
				return p.TEXT_SIZE_SMALL;
			}

			public Dimension getPreferredSize() {
				return new Dimension(t.getWidth() - 2 * OptionsFrame.PADDING - 32, super.getPreferredSize().height);
			}

			@Override
			public Color getForegroundColor() {
				if (checkbox.isEnabled()) {
					return super.getForegroundColor();
				}
				return disabledCol.color();
			}
		};
		checkbox = new CustomCheckbox(preference.get()) {
			private static final long serialVersionUID = 1507233642665292025L;

			@Override
			public void onChanged(boolean ticked) {
				preference.set(ticked);
			}
		};
		add(checkbox);
		add(Box.createHorizontalStrut(OptionsFrame.PADDING));
		add(descLabel);
		setOpaque(true);

		disabledCol = styleManager.currentTheme.TEXT_COLOR_WEAK;
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		checkbox.setEnabled(enabled);
	}

}