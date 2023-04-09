package ninjabrainbot.data.actions;

import ninjabrainbot.data.domainmodel.IWriteLock;

public class ActionExecutor implements IActionExecutor {

	private final IWriteLock writeLock;

	public ActionExecutor(IWriteLock writeLock) {
		this.writeLock = writeLock;
	}

	@Override
	public void executeImmediately(IAction... actions) {
		if (writeLock != null)
			writeLock.acquireWriteLock();
		try {
			for (IAction action : actions)
				action.execute();
		} finally {
			if (writeLock != null)
				writeLock.releaseWriteLock();
		}
	}
}
