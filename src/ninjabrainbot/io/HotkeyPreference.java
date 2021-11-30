package ninjabrainbot.io;

import java.util.ArrayList;
import java.util.prefs.Preferences;

import ninjabrainbot.gui.GUI;

public class HotkeyPreference {
	
	public static ArrayList<HotkeyPreference> hotkeys = new ArrayList<HotkeyPreference>();
	
	Preferences pref;

	IntPreference modifier;
	IntPreference code;

	public HotkeyPreference(String key, Preferences pref) {
		this.pref = pref;
		modifier = new IntPreference(key + "_modifier", -1, pref);
		code = new IntPreference(key + "_code", -1, pref);
		hotkeys.add(this);
	}

	public int getCode() {
		return code.get();
	}
	
	public int getModifier() {
		return modifier.get();
	}
	
	public synchronized void setCode(int value) {
		code.set(value);
	}
	
	public synchronized void setModifier(int value) {
		modifier.set(value);
	}

	public void onChangedByUser(GUI gui) { }
	
	public void execute(GUI gui) { }

}