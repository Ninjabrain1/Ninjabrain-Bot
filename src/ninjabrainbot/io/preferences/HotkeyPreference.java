package ninjabrainbot.io.preferences;

import java.util.ArrayList;
import java.util.prefs.Preferences;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;

import ninjabrainbot.event.ISubscribable;
import ninjabrainbot.event.ObservableProperty;

public class HotkeyPreference {

	public static ArrayList<HotkeyPreference> hotkeys = new ArrayList<HotkeyPreference>();

	Preferences pref;

	IntPreference modifier;
	IntPreference code;

	private ObservableProperty<NativeKeyEvent> whenTriggered;

	public HotkeyPreference(String key, Preferences pref) {
		this.pref = pref;
		modifier = new IntPreference(key + "_modifier", -1, pref);
		code = new IntPreference(key + "_code", -1, pref);
		hotkeys.add(this);
		whenTriggered = new ObservableProperty<NativeKeyEvent>();
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

	public final void execute(NativeKeyEvent e) {
		whenTriggered.notifySubscribers(e);
	}

	public ISubscribable<NativeKeyEvent> whenTriggered() {
		return whenTriggered;
	}

}