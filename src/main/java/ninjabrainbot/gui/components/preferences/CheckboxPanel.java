package ninjabrainbot.gui.components.preferences;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.Box;

import ninjabrainbot.gui.buttons.WikiButton;
import ninjabrainbot.gui.components.labels.ThemedLabel;
import ninjabrainbot.gui.components.panels.ThemedPanel;
import ninjabrainbot.gui.frames.OptionsFrame;
import ninjabrainbot.gui.style.SizePreference;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.gui.style.theme.WrappedColor;
import ninjabrainbot.io.preferences.BooleanPreference;

public class CheckboxPanel extends ThemedPanel {

	public final ThemedLabel descLabel;
	CustomCheckbox checkbox;
	final BooleanPreference preference;

	WrappedColor disabledCol;

	public CheckboxPanel(StyleManager styleManager, String description, BooleanPreference preference) {
		super(styleManager);
		this.preference = preference;
		setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		CheckboxPanel t = this;
		descLabel = new ThemedLabel(styleManager, "<html>" + description + "</html>") {

			@Override
			public int getTextSize(SizePreference p) {
				return p.TEXT_SIZE_SMALL;
			}

			public Dimension getPreferredSize() {
				Dimension superPreferredSize = super.getPreferredSize();
				int preferredWidth = Math.min(superPreferredSize.width, t.getWidth() - 2 * OptionsFrame.PADDING - 32);
				return new Dimension(preferredWidth, superPreferredSize.height);
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

	public CheckboxPanel withWikiButton(WikiButton wikiButton) {
		add(Box.createHorizontalStrut(OptionsFrame.PADDING));
		add(wikiButton);
		return this;
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		checkbox.setEnabled(enabled);
	}

}