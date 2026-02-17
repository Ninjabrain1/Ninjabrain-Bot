package ninjabrainbot.io.preferences;

import java.util.ArrayList;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.sun.jna.Platform;
import ninjabrainbot.event.ISubscribable;
import ninjabrainbot.event.ObservableProperty;

public class HotkeyPreference {

	public static final ArrayList<HotkeyPreference> hotkeys = new ArrayList<HotkeyPreference>();

	final IPreferenceSource pref;

	final IntPreference modifier;
	final IntPreference code;

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

	public boolean isKeyEventMatching(NativeKeyEvent nativeKeyEvent) {
		int code = getPlatformSpecificKeyCode(nativeKeyEvent);
		return getCode() == code && (getModifier() & nativeKeyEvent.getModifiers()) == getModifier();
	}

	public synchronized void setCode(int value) {
		code.set(value);
	}

	public synchronized void setModifier(int value) {
		modifier.set(value);
	}

	public synchronized void setHotkey(NativeKeyEvent nativeKeyEvent) {
		setCode(getPlatformSpecificKeyCode(nativeKeyEvent));
		setModifier(nativeKeyEvent.getModifiers());
	}

	public final void execute() {
		whenTriggered.notifySubscribers(this);
	}

	public ISubscribable<HotkeyPreference> whenTriggered() {
		return whenTriggered;
	}

	private static int getPlatformSpecificKeyCode(NativeKeyEvent nativeKeyEvent) {
		if (Platform.isLinux() || Platform.isMac()){
			int keyCode = nativeKeyEvent.getKeyCode();
			boolean isValidKeyCode = keyCode != 0;
			return isValidKeyCode ? keyCode | (nativeKeyEvent.getKeyLocation() << 16) : nativeKeyEvent.getRawCode();
		}
		return nativeKeyEvent.getRawCode();
	}
}