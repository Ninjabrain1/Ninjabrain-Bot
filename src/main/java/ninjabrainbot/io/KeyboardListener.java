package ninjabrainbot.io;

import java.awt.event.KeyEvent;
import java.util.function.BiConsumer;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import ninjabrainbot.io.preferences.BooleanPreference;
import ninjabrainbot.io.preferences.HotkeyPreference;

public class KeyboardListener implements NativeKeyListener {

	public static boolean registered = false;
	public static KeyboardListener instance;

	private BooleanPreference useAltClipboardReader;

	BiConsumer<Integer, Integer> consumer;
	ClipboardReader clr;
	boolean f3Held = false;

	public static void preInit() {
		try {
			System.setProperty("jnativehook.lib.path", System.getProperty("java.io.tmpdir"));
			GlobalScreen.registerNativeHook();
			registered = true;
		} catch (UnsatisfiedLinkError er) {
			er.printStackTrace();
		} catch (NativeHookException ex) {
			System.err.println("There was a problem registering the native hook.");
			System.err.println(ex.getMessage());
		}
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				try {
					GlobalScreen.unregisterNativeHook();
				} catch (NativeHookException e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static void init(ClipboardReader clr, BooleanPreference useAltClipboardReader) {
		if (registered) {
			instance = new KeyboardListener(clr, useAltClipboardReader);
			GlobalScreen.addNativeKeyListener(instance);
		}
	}

	KeyboardListener(ClipboardReader clr, BooleanPreference useAltClipboardReader) {
		super();
		this.clr = clr;
		this.useAltClipboardReader = useAltClipboardReader;
	}

	public synchronized void setConsumer(BiConsumer<Integer, Integer> consumer) {
		if (this.consumer != null) {
			this.consumer.accept(-1, -1);
		}
		this.consumer = consumer;
	}

	public synchronized void cancelConsumer() {
		if (this.consumer != null) {
			this.consumer.accept(-1, -1);
			this.consumer = null;
		}
	}

	@Override
	public void nativeKeyPressed(NativeKeyEvent e) {
		int c = e.getKeyCode();
		if (c == NativeKeyEvent.VC_SHIFT || c == NativeKeyEvent.VC_CONTROL || c == NativeKeyEvent.VC_ALT)
			return;
		if (consumer != null) {
			consumer.accept(e.getRawCode(), e.getModifiers());
			consumer = null;
			return;
		}
		for (HotkeyPreference h : HotkeyPreference.hotkeys) {
			if (h.getCode() == e.getRawCode() && (h.getModifier() & e.getModifiers()) == h.getModifier()) {
				h.execute(e);
			}
		}
		// Alt clipboard reader
		if (useAltClipboardReader.get()) {
			if (e.getRawCode() == KeyEvent.VK_F3) {
				f3Held = true;
			} else if (f3Held && e.getRawCode() == KeyEvent.VK_C) {
				clr.forceRead();
			}
		}
	}

	@Override
	public void nativeKeyReleased(NativeKeyEvent e) {
		if (useAltClipboardReader.get() && e.getRawCode() == KeyEvent.VK_F3) {
			f3Held = false;
		}
	}

}
