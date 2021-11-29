package ninjabrainbot.io;

import java.util.HashMap;
import java.util.prefs.Preferences;

import ninjabrainbot.gui.GUI;

public class MultipleChoicePreference {

	Preferences pref;

	String key;
	String value;
	String[] choices;
	
	HashMap<Integer, String> id2choice;
	HashMap<String, Integer> choice2id;	
	
	public MultipleChoicePreference(String key, String defaultValue, int[] ids, String[] choices, Preferences pref) {
			this.pref = pref;
			this.key = key;
			this.choices = choices;
			id2choice = new HashMap<Integer, String>();
			choice2id = new HashMap<String, Integer>();
			for (int i = 0; i < ids.length; i++) {
				id2choice.put(ids[i], choices[i]);
				choice2id.put(choices[i], ids[i]);
			}
			value = id2choice.get(pref.getInt(key, choice2id.get(defaultValue)));
		}

	public String get() {
		return value;
	}

	public void set(String value) {
		this.value = value;
		pref.putInt(key, choice2id.get(value));
	}
	
	public String[] getChoices() {
		return choices;
	}
	
	public void onChangedByUser(GUI gui) {}

}