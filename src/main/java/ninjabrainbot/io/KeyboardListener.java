package ninjabrainbot.io;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import com.github.kwhat.jnativehook.mouse.NativeMouseWheelEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseWheelListener;
import ninjabrainbot.io.preferences.BooleanPreference;
import ninjabrainbot.io.preferences.HotkeyPreference;

import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class KeyboardListener implements NativeKeyListener, NativeMouseWheelListener {

	public static boolean registered = false;
	public static KeyboardListener instance;

	private final BooleanPreference useAltClipboardReader;

	Consumer<NativeKeyEvent> consumer;
	Consumer<NativeKeyEvent> releaseConsumer;
	Consumer<NativeMouseWheelEvent> mouseWheelConsumer;
	final ClipboardReader clipboardReader;
	boolean f3Held = false;

	public final Set<Integer> pressedKeys = new HashSet<>();

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
			GlobalScreen.addNativeMouseWheelListener(instance);
		}
	}

	KeyboardListener(ClipboardReader clipboardReader, BooleanPreference useAltClipboardReader) {
		super();
		this.clipboardReader = clipboardReader;
		this.useAltClipboardReader = useAltClipboardReader;
	}

	public synchronized void setConsumer(Consumer<NativeKeyEvent> consumer, Consumer<NativeKeyEvent> releaseConsumer, Consumer<NativeMouseWheelEvent> mouseWheelConsumer) {
		if (this.consumer != null) {
			this.consumer.accept(null);
		}
		if (this.releaseConsumer != null) {
			this.releaseConsumer.accept(null);
		}
		if (this.mouseWheelConsumer != null) {
			this.mouseWheelConsumer.accept(null);
		}
		this.consumer = consumer;
		this.releaseConsumer = releaseConsumer;
		this.mouseWheelConsumer = mouseWheelConsumer;
	}

	public synchronized void cancelConsumer() {
		if (this.consumer != null) {
			this.consumer.accept(null);
			this.consumer = null;
		}
		if (this.releaseConsumer != null) {
			this.releaseConsumer.accept(null);
			this.releaseConsumer = null;
		}
		if (this.mouseWheelConsumer != null) {
			this.mouseWheelConsumer.accept(null);
			this.mouseWheelConsumer = null;
		}
	}

	@Override
	public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
		int keyCode = HotkeyPreference.getPlatformSpecificKeyCode(nativeKeyEvent);
		pressedKeys.add(keyCode);

		if (consumer != null) {
			consumer.accept(nativeKeyEvent);
			return;
		}

		for (HotkeyPreference hotkeyPreference : HotkeyPreference.hotkeys) {
			if (hotkeyPreference.isKeyEventMatching(nativeKeyEvent, pressedKeys)) {
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
		pressedKeys.remove(HotkeyPreference.getPlatformSpecificKeyCode(e));
		if (releaseConsumer != null) {
			releaseConsumer.accept(e);
		}
		if (useAltClipboardReader.get() && e.getRawCode() == KeyEvent.VK_F3) {
			f3Held = false;
		}
	}

	@Override
	public void nativeMouseWheelMoved(NativeMouseWheelEvent nativeMouseWheelEvent) {
		if (mouseWheelConsumer != null) {
			mouseWheelConsumer.accept(nativeMouseWheelEvent);
			return;
		}
		int scrollCode = nativeMouseWheelEvent.getWheelRotation() < 0 ? HotkeyPreference.SCROLL_UP : HotkeyPreference.SCROLL_DOWN;
		for (HotkeyPreference hotkeyPreference : HotkeyPreference.hotkeys) {
			if (hotkeyPreference.isMouseWheelMatching(scrollCode, pressedKeys)) {
				hotkeyPreference.execute();
			}
		}
	}
}
