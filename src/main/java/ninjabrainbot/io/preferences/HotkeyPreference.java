package ninjabrainbot.io.preferences;

import java.util.ArrayList;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.sun.jna.Platform;
import ninjabrainbot.event.ISubscribable;
import ninjabrainbot.event.ObservableProperty;

public class HotkeyPreference {

	public static final int SCROLL_UP = -10;
	public static final int SCROLL_DOWN = -11;

	public static final ArrayList<HotkeyPreference> hotkeys = new ArrayList<HotkeyPreference>();

	final IPreferenceSource pref;

	final IntPreference modifier;
	final IntPreference code;
	final IntPreference code2;

	private final ObservableProperty<HotkeyPreference> whenTriggered;

	public HotkeyPreference(String key, IPreferenceSource pref) {
		this.pref = pref;
		modifier = new IntPreference(key + "_modifier", -1, pref);
		code = new IntPreference(key + "_code", -1, pref);
		code2 = new IntPreference(key + "_code2", -1, pref);
		hotkeys.add(this);
		whenTriggered = new ObservableProperty<>();
	}

	public int getCode() {
		return code.get();
	}

	public int getCode2() {
		return code2.get();
	}

	public int getModifier() {
		return modifier.get();
	}

	public boolean isKeyEventMatching(NativeKeyEvent nativeKeyEvent, java.util.Set<Integer> pressedKeys) {
		int code = getPlatformSpecificKeyCode(nativeKeyEvent);
		if (this.code.get() != code && this.code2.get() != code)
			return false;

		if ((getModifier() & nativeKeyEvent.getModifiers()) != getModifier())
			return false;

		if (this.code2.get() == -1)
			return true;

		int otherCode = (this.code.get() == code) ? this.code2.get() : this.code.get();
		return pressedKeys.contains(otherCode);
	}

	public boolean isMouseWheelMatching(int scrollCode, java.util.Set<Integer> pressedKeys) {
		if (this.code.get() != scrollCode)
			return false;

		if (this.code2.get() == -1)
			return true;

		return pressedKeys.contains(this.code2.get());
	}

	public synchronized void setCode(int value) {
		code.set(value);
	}

	public synchronized void setCode2(int value) {
		code2.set(value);
	}

	public synchronized void setModifier(int value) {
		modifier.set(value);
	}

	public synchronized void setHotkey(NativeKeyEvent nativeKeyEvent, int code2) {
		setCode(getPlatformSpecificKeyCode(nativeKeyEvent));
		setCode2(code2);
		setModifier(nativeKeyEvent.getModifiers());
	}

	public synchronized void setHotkey(int scrollCode, int code2) {
		setCode(scrollCode);
		setCode2(code2);
		setModifier(0);
	}

	public final void execute() {
		whenTriggered.notifySubscribers(this);
	}

	public ISubscribable<HotkeyPreference> whenTriggered() {
		return whenTriggered;
	}

	public static int getPlatformSpecificKeyCode(NativeKeyEvent nativeKeyEvent) {
		if (Platform.isLinux() || Platform.isMac()){
			int keyCode = nativeKeyEvent.getKeyCode();
			boolean isValidKeyCode = keyCode != 0;
			return isValidKeyCode ? keyCode | (nativeKeyEvent.getKeyLocation() << 16) : nativeKeyEvent.getRawCode();
		}
		return nativeKeyEvent.getRawCode();
	}
}