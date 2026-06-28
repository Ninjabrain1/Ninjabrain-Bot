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
	final IntPreference rawCode;

	private final ObservableProperty<HotkeyPreference> whenTriggered;

	public HotkeyPreference(String key, IPreferenceSource pref) {
		this.pref = pref;
		modifier = new IntPreference(key + "_modifier", -1, pref);
		code = new IntPreference(key + "_code", -1, pref);
		rawCode = new IntPreference(key + "_raw", -1, pref);
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
		boolean matches = getCode() == code && (getModifier() & nativeKeyEvent.getModifiers()) == getModifier();
		// Auto-populate rawCode for existing bindings that predate rawCode storage
		if (matches && rawCode.get() == -1) {
			rawCode.set(nativeKeyEvent.getRawCode());
		}
		return matches;
	}

      // Match against a raw X11 keycode + JNH-format modifiers. (Used by StdinKeyReader for external key forwarding.)
	
	public boolean isRawKeyMatching(int raw, int modifiers) {
		if (rawCode.get() == -1) return false;
		return rawCode.get() == raw && (getModifier() & modifiers) == getModifier();
	}


      // Match against a JNH virtual keycode (no keyLocation shift) + JNH-format modifiers. (Fallback for old bindings that don't have rawCode stored yet.)


	public boolean isJnhCodeMatching(int jnhCode, int modifiers) {
		if (jnhCode == -1) return false;
		return getCode() == jnhCode && (getModifier() & modifiers) == getModifier();
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
		rawCode.set(nativeKeyEvent.getRawCode());
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
