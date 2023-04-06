package ninjabrainbot.data.actions;

import ninjabrainbot.data.temp.IWriteLock;

public class ActionExecutor implements IActionExecutor {

	private final IWriteLock writeLock;

	public ActionExecutor(IWriteLock writeLock) {
		this.writeLock = writeLock;
	}

	@Override
	public void executeImmediately(IAction action) {
		writeLock.acquireWriteLock();
		try {
			action.execute();
		} finally {
			writeLock.releaseWriteLock();
		}
	}
}
