package ninjabrainbot.io;

import java.util.Scanner;

import ninjabrainbot.io.preferences.HotkeyPreference;
import ninjabrainbot.util.Logger;


      // Reads key events from stdin for external tools (e.g. tuxinjector, which embed NBB and can't rely on JNativeHook/XRecord for key delivery.)



public class StdinKeyReader {

	public static void start() {
		Thread thread = new Thread(() -> {
			Logger.log("StdinKeyReader: thread started, waiting for stdin");
			try (Scanner scanner = new Scanner(System.in)) {
				while (scanner.hasNextLine()) {
					String line = scanner.nextLine().trim();
					if (!line.startsWith("KEY "))
						continue;
					String[] parts = line.split(" ");
					if (parts.length < 3)
						continue;
					try {
						int rawCode = Integer.parseInt(parts[1]);
						int x11Mods = Integer.parseInt(parts[2]);
						int jnhCode = parts.length >= 4 ? Integer.parseInt(parts[3]) : -1;
						int jnhMods = x11ModsToJnh(x11Mods);
						dispatchKey(rawCode, jnhCode, jnhMods);
					} catch (NumberFormatException ignored) {
					}
				}
			}
			Logger.log("StdinKeyReader: stdin closed");
		}, "StdinKeyReader");
		thread.setDaemon(true);
		thread.start();
	}

	private static void dispatchKey(int rawCode, int jnhCode, int jnhMods) {
		for (HotkeyPreference hotkey : HotkeyPreference.hotkeys) {
			// Try rawCode match first (new bindings), then JNH keycode match after (old bindings)
			if (hotkey.isRawKeyMatching(rawCode, jnhMods) || hotkey.isJnhCodeMatching(jnhCode, jnhMods)) {
				hotkey.execute();
			}
		}
	}


      // Convert the X11 modifier mask to a JNativeHook modifier mask. (Sets both left and right variants so either side matches.)

	private static int x11ModsToJnh(int x11) {
		int jnh = 0;
		if ((x11 & 1) != 0) jnh |= 1 | 2;        // Shift -> SHIFT_L | SHIFT_R
		if ((x11 & 4) != 0) jnh |= 4 | 8;        // Control -> CTRL_L | CTRL_R
		if ((x11 & 8) != 0) jnh |= 16 | 32;      // Mod1 (Alt) -> ALT_L | ALT_R
		if ((x11 & 64) != 0) jnh |= 64 | 128;    // Mod4 (Super/Windows) -> META_L | META_R
		return jnh;
	}
}
