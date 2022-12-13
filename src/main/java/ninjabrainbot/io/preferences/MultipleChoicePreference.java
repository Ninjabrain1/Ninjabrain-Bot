package ninjabrainbot.io.preferences;

import java.util.HashMap;

import ninjabrainbot.event.Modifiable;

public class MultipleChoicePreference<T extends IMultipleChoicePreferenceDataType> extends Modifiable<T> {

	IPreferenceSource pref;

	String key;
	IMultipleChoicePreferenceDataType value;
	IMultipleChoicePreferenceDataType[] choices;

	HashMap<Integer, IMultipleChoicePreferenceDataType> id2choice;
	HashMap<IMultipleChoicePreferenceDataType, Integer> choice2id;

	public MultipleChoicePreference(String key, IMultipleChoicePreferenceDataType defaultValue, int[] ids, IMultipleChoicePreferenceDataType[] choices, IPreferenceSource pref) {
		this.pref = pref;
		this.key = key;
		this.choices = choices;
		id2choice = new HashMap<Integer, IMultipleChoicePreferenceDataType>();
		choice2id = new HashMap<IMultipleChoicePreferenceDataType, Integer>();
		for (int i = 0; i < ids.length; i++) {
			id2choice.put(ids[i], choices[i]);
			choice2id.put(choices[i], ids[i]);
		}
		value = id2choice.get(pref.getInt(key, choice2id.get(defaultValue)));
	}

	public MultipleChoicePreference(String key, String defaultValue, int[] ids, String[] choices, IMultipleChoicePreferenceDataType[] enumChoices, IPreferenceSource pref) {
		this(key, nameToEnum(defaultValue, enumChoices), ids, nameToEnum(choices, enumChoices), pref);
	}

	private static IMultipleChoicePreferenceDataType nameToEnum(String name, IMultipleChoicePreferenceDataType[] enumChoices) {
		for (IMultipleChoicePreferenceDataType choice : enumChoices) {
			if (choice.choiceName() == name)
				return choice;
		}
		throw new Error("Name " + name + " not found in enum.");
	}

	private static IMultipleChoicePreferenceDataType[] nameToEnum(String[] choices, IMultipleChoicePreferenceDataType[] enumChoices) {
		IMultipleChoicePreferenceDataType[] choices_ = new IMultipleChoicePreferenceDataType[choices.length];
		for (int i = 0; i < choices_.length; i++) {
			choices_[i] = nameToEnum(choices[i], enumChoices);
		}
		return choices_;
	}

	@SuppressWarnings("unchecked")
	public T get() {
		return (T) value;
	}

	@SuppressWarnings("unchecked")
	public void set(IMultipleChoicePreferenceDataType value) {
		this.value = value;
		pref.putInt(key, choice2id.get(value));
		notifySubscribers((T) value);
	}

	public IMultipleChoicePreferenceDataType[] getChoices() {
		return choices;
	}

}