package ninjabrainbot.io.api.commands;

/**
 * Common base interface for all API commands (both simple and parametrized).
 */
public interface IApiCommand {

	String name();

	String description();

}

