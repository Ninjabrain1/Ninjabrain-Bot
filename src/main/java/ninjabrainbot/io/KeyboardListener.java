package ninjabrainbot.io;

import java.awt.event.KeyEvent;
import java.util.function.Consumer;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import ninjabrainbot.io.preferences.BooleanPreference;
import ninjabrainbot.io.preferences.HotkeyPreference;

public class KeyboardListener implements NativeKeyListener {

	public static boolean registered = false;
	public static KeyboardListener instance;

	private final BooleanPreference useAltClipboardReader;

	Consumer<NativeKeyEvent> consumer;
	final ClipboardReader clipboardReader;
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

	public static void init(ClipboardReader clipboardReader, BooleanPreference useAltClipboardReader) {
		if (registered) {
			instance = new KeyboardListener(clipboardReader, useAltClipboardReader);
			GlobalScreen.addNativeKeyListener(instance);
		}
	}

	KeyboardListener(ClipboardReader clipboardReader, BooleanPreference useAltClipboardReader) {
		super();
		this.clipboardReader = clipboardReader;
		this.useAltClipboardReader = useAltClipboardReader;
	}

	public synchronized void setConsumer(Consumer<NativeKeyEvent> consumer) {
		if (this.consumer != null) {
			this.consumer.accept(null);
		}
		this.consumer = consumer;
	}

	public synchronized void cancelConsumer() {
		if (this.consumer != null) {
			this.consumer.accept(null);
			this.consumer = null;
		}
	}

	@Override
	public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
		int keyCode = nativeKeyEvent.getKeyCode();
		if (keyCode == NativeKeyEvent.VC_SHIFT || keyCode == NativeKeyEvent.VC_CONTROL || keyCode == NativeKeyEvent.VC_ALT)
			return;

		if (consumer != null) {
			consumer.accept(nativeKeyEvent);
			consumer = null;
			return;
		}
		for (HotkeyPreference hotkeyPreference : HotkeyPreference.hotkeys) {
			if (hotkeyPreference.isKeyEventMatching(nativeKeyEvent)) {
				hotkeyPreference.execute();
			}
		}
		// Alt clipboard reader
		if (useAltClipboardReader.get()) {
			if (nativeKeyEvent.getRawCode() == KeyEvent.VK_F3) {
				f3Held = true;
			} else if (f3Held && nativeKeyEvent.getRawCode() == KeyEvent.VK_C) {
				clipboardReader.forceRead();
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
