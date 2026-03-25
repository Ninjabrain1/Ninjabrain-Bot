package ninjabrainbot.io.api.interfaces;

/**
 * Common base interface for all API commands (both simple and parametrized).
 */
public interface ICommand {

	String name();

	/**
	 * Short one-line summary, used in the commands overview table.
	 */
	String summary();

	/**
	 * Detailed description, used in the per-command documentation section.
	 */
	String description();

}

