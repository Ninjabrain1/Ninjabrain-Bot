package ninjabrainbot.util;

/**
 * A simple utility for measuring elapsed time between steps in a process.
 * Each call to {@link #log(String)} logs the time elapsed since the previous log (or since construction).
 * Call {@link #logTotal(String)} to log the total time since construction.
 */
public class StopWatch {

	private final long startNanos;
	private long lastNanos;

	public StopWatch() {
		startNanos = System.nanoTime();
		lastNanos = startNanos;
	}

	/**
	 * Logs the time elapsed since the previous call to {@code log} (or since construction).
	 */
	public void log(String step) {
		long now = System.nanoTime();
		double elapsedMs = (now - lastNanos) / 1_000_000.0;
		lastNanos = now;
		Logger.log(String.format("[Timer] %s: %.2f ms", step, elapsedMs));
	}

	/**
	 * Logs the total time elapsed since construction.
	 */
	public void logTotal(String label) {
		long now = System.nanoTime();
		double totalMs = (now - startNanos) / 1_000_000.0;
		lastNanos = now;
		Logger.log(String.format("[Timer] %s: %.2f ms (total)", label, totalMs));
	}

}
