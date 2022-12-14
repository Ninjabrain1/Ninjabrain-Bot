package ninjabrainbot.data.datalock;

public interface ILock extends AutoCloseable {

	@Override
	default void close() {
	}

	void setUndoAction();

}
