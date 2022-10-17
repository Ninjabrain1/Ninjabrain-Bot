package ninjabrainbot.data.datalock;

public class AlwaysUnlocked implements IModificationLock {

	@Override
	public boolean isLocked() {
		return false;
	}

}
