package ninjabrainbot.gui.components.preferences;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;

import ninjabrainbot.gui.components.labels.ThemedLabel;
import ninjabrainbot.gui.components.panels.ThemedPanel;
import ninjabrainbot.gui.style.SizePreference;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.io.preferences.IMultipleChoicePreferenceDataType;
import ninjabrainbot.io.preferences.MultipleChoicePreference;

public class RadioButtonPanel extends ThemedPanel {

	JLabel descLabel;
	RadioButtonGroup radioButtonGroup;
	MultipleChoicePreference<?> preference;

	public RadioButtonPanel(StyleManager styleManager, String description, MultipleChoicePreference<?> preference) {
		super(styleManager);
		this.preference = preference;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		descLabel = new ThemedLabel(styleManager, description) {
			@Override
			public int getTextSize(SizePreference p) {
				return p.TEXT_SIZE_SMALL;
			}
		};
		radioButtonGroup = new RadioButtonGroup(styleManager, preference.getChoices(), preference.get(), preference.getChoices().length >= 4) {
			@Override
			public void onChanged(IMultipleChoicePreferenceDataType newValue) {
				preference.set(newValue);
			}
		};
		descLabel.setAlignmentX(0);
		radioButtonGroup.setAlignmentX(0);
		add(descLabel);
		add(Box.createVerticalStrut(2));
		add(radioButtonGroup);
		setOpaque(true);
	}

}