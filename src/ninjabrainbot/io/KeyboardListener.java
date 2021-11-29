package ninjabrainbot.io;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

public class KeyboardListener implements NativeKeyListener {
	
	public static void init() {
		try {
			GlobalScreen.registerNativeHook();
			GlobalScreen.addNativeKeyListener(new KeyboardListener());
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

	@Override
	public void nativeKeyPressed(NativeKeyEvent e) {
		System.out.println("Key Pressed: " + NativeKeyEvent.getModifiersText(e.getModifiers()));
		System.out.println("Key Pressed: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
		System.out.println(e.getKeyLocation());
	}
	
}
