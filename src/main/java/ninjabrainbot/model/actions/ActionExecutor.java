package ninjabrainbot.model.actions;

import ninjabrainbot.model.domainmodel.IWriteLock;
import ninjabrainbot.util.Assert;

public class ActionExecutor implements IActionExecutor {

	private final IWriteLock writeLock;

	private boolean disabled = false;

	public ActionExecutor(IWriteLock writeLock) {
		this.writeLock = writeLock;
	}

	@Override
	public void executeImmediately(IAction... actions) {
		if (disabled)
			return;
		if (writeLock != null)
			writeLock.acquireWriteLock();
		try {
			for (IAction action : actions) {
				action.execute();
			}
		} finally {
			if (writeLock != null)
				writeLock.releaseWriteLock();
		}
	}

	@Override
	public void disable() {
		Assert.isFalse(disabled);
		disabled = true;
	}

	@Override
	public void enable() {
		Assert.isTrue(disabled);
		disabled = false;
	}
}
