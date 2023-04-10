package ninjabrainbot.io.preferences;

import java.util.ArrayList;

import ninjabrainbot.event.ISubscribable;
import ninjabrainbot.event.ObservableProperty;

public class HotkeyPreference {

	public static ArrayList<HotkeyPreference> hotkeys = new ArrayList<HotkeyPreference>();

	IPreferenceSource pref;

	IntPreference modifier;
	IntPreference code;

	private final ObservableProperty<HotkeyPreference> whenTriggered;

	public HotkeyPreference(String key, IPreferenceSource pref) {
		this.pref = pref;
		modifier = new IntPreference(key + "_modifier", -1, pref);
		code = new IntPreference(key + "_code", -1, pref);
		hotkeys.add(this);
		whenTriggered = new ObservableProperty<>();
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

	public final void execute() {
		whenTriggered.notifySubscribers(this);
	}

	public ISubscribable<HotkeyPreference> whenTriggered() {
		return whenTriggered;
	}

}