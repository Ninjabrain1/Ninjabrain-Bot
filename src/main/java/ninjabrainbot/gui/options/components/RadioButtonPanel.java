package ninjabrainbot.gui.options.components;

import javax.swing.BoxLayout;
import javax.swing.JLabel;

import ninjabrainbot.gui.components.ThemedLabel;
import ninjabrainbot.gui.options.RadioButtonGroup;
import ninjabrainbot.gui.panels.ThemedPanel;
import ninjabrainbot.gui.style.SizePreference;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.io.preferences.IMultipleChoicePreferenceDataType;
import ninjabrainbot.io.preferences.MultipleChoicePreference;

public class RadioButtonPanel extends ThemedPanel {

	private static final long serialVersionUID = -7054967229481740724L;

	JLabel descLabel;
	RadioButtonGroup radioButtomGroup;
	MultipleChoicePreference<?> preference;

	public RadioButtonPanel(StyleManager styleManager, String description, MultipleChoicePreference<?> preference) {
		super(styleManager);
		this.preference = preference;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		descLabel = new ThemedLabel(styleManager, description) {
			private static final long serialVersionUID = 2113195400239083116L;

			@Override
			public int getTextSize(SizePreference p) {
				return p.TEXT_SIZE_SMALL;
			}
		};
		radioButtomGroup = new RadioButtonGroup(styleManager, preference.getChoices(), preference.get(), preference.getChoices().length >= 4) {
			private static final long serialVersionUID = -1357640224921308648L;

			@Override
			public void onChanged(IMultipleChoicePreferenceDataType newValue) {
				preference.set(newValue);
			}
		};
		descLabel.setAlignmentX(0);
		radioButtomGroup.setAlignmentX(0);
		add(descLabel);
		add(radioButtomGroup);
		setOpaque(false);
	}

}