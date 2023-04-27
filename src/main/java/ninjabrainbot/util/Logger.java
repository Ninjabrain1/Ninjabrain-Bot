package ninjabrainbot.util;

public class Logger {

	public static boolean enabled = true;

	public static void log(String s) {
		if (enabled)
			System.out.format("[%s] %s\n", Thread.currentThread().getName(), s);
	}

	public static void log(Object o) {
		log(o == null ? null : o.toString());
	}

}
