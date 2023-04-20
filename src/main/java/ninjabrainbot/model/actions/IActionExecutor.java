package ninjabrainbot.model.actions;

public interface IActionExecutor {

	void executeImmediately(IAction... actions);

	void disable();

	void enable();

}
