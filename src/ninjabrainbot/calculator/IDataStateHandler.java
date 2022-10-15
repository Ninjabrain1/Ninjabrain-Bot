package ninjabrainbot.calculator;

public interface IDataStateHandler {

	void reset();

	void resetIfNotLocked();

	void undo();

	void undoIfNotLocked();

	void changeLastAngleIfNotLocked(double delta);

	void toggleAltStdOnLastThrowIfNotLocked();

}
