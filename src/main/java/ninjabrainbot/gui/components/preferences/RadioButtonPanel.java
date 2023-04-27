package ninjabrainbot.gui.components.preferences;

import java.util.function.Consumer;

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

	final ThemedLabel descLabel;
	final RadioButtonGroup<?> radioButtonGroup;

	public <T extends IMultipleChoicePreferenceDataType> RadioButtonPanel(StyleManager styleManager, String description, MultipleChoicePreference<T> preference) {
		this(styleManager, description, preference.getChoices(), preference.get(), preference::set);
	}

	public <T extends IMultipleChoicePreferenceDataType> RadioButtonPanel(StyleManager styleManager, String description, MultipleChoicePreference<T> preference, boolean verticalRadioButtons) {
		this(styleManager, description, preference.getChoices(), preference.get(), preference::set, verticalRadioButtons);
	}

	public <T extends IMultipleChoiceOption> RadioButtonPanel(StyleManager styleManager, String description, T[] choices, T selectedValue, Consumer<T> onChanged) {
		this(styleManager, description, choices, selectedValue, onChanged, choices.length >= 4);
	}

	public <T extends IMultipleChoiceOption> RadioButtonPanel(StyleManager styleManager, String description, T[] choices, T selectedValue, Consumer<T> onChanged, boolean verticalRadioButtons) {
		super(styleManager);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		descLabel = new ThemedLabel(styleManager, description) {
			@Override
			public int getTextSize(SizePreference p) {
				return p.TEXT_SIZE_SMALL;
			}
		};
		radioButtonGroup = new RadioButtonGroup<T>(styleManager, choices, selectedValue, verticalRadioButtons) {
			@Override
			public void onChanged(T newValue) {
				onChanged.accept(newValue);
			}
		};
		descLabel.setAlignmentX(0);
		radioButtonGroup.setAlignmentX(0);
		add(descLabel);
		add(Box.createVerticalStrut(2));
		add(radioButtonGroup);
		setOpaque(true);
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		descLabel.updateColors();
		radioButtonGroup.setEnabled(enabled);
	}
}